package com.stankevych.booking_app.service.impl;

import static com.stankevych.booking_app.model.Payment.Status.PAID;
import static com.stankevych.booking_app.model.Payment.Status.PENDING;

import com.stankevych.booking_app.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.booking_app.dto.payment.PaymentResponseDto;
import com.stankevych.booking_app.exception.EntityNotFoundException;
import com.stankevych.booking_app.exception.PaymentException;
import com.stankevych.booking_app.mapper.PaymentMapper;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.Payment;
import com.stankevych.booking_app.repository.BookingRepository;
import com.stankevych.booking_app.repository.PaymentRepository;
import com.stankevych.booking_app.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final BookingRepository bookingRepository;
    private final StripeServiceImpl stripeService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDto createPayment(Long bookingId, Long userId) {
        var booking = bookingRepository
                .findByIdAndUserIdAndStatusIs(bookingId, userId, Booking.Status.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("""
                        No unpaid booking with id '%d" for user with id '%d' found"""
                        .formatted(bookingId, userId)));

        var payment = createPaymentForBooking(booking);
        var session = stripeService.createSession(payment);
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentDtoWithoutSession> findAllByUserId(Long userId) {
        return paymentMapper
                .toDtoListWithoutSession(paymentRepository.findAllByBookingUserId(userId));
    }

    @Override
    @Transactional
    public PaymentDtoWithoutSession completePayment(String sessionId) {
        checkSessionCompletion(sessionId);
        var payment = paymentRepository
                .findBySessionIdAndStatusIs(sessionId, PENDING)
                .orElseThrow(() -> new PaymentException("""
                        No unpaid payment with session id '%s' found"""
                        .formatted(sessionId)));
        payment.setStatus(PAID);
        payment.getBooking()
                .setStatus(Booking.Status.CONFIRMED);
        return paymentMapper.toDtoWithoutSession(payment);
    }

    @Override
    public PaymentDtoWithoutSession cancelPayment(String sessionId) {
        var payment = paymentRepository
                .findBySessionIdAndStatusIs(sessionId, PENDING)
                .orElseThrow(() -> new PaymentException("""
                        No unpaid payment with session id '%s' found"""
                        .formatted(sessionId)));
        stripeService.expireSession(sessionId);
        return paymentMapper
                .toDtoWithoutSession(payment);
    }

    private void checkSessionCompletion(String sessionId) {
        if (!stripeService.isSessionPaid(sessionId)) {
            throw new PaymentException("Unpaid session found");
        }
    }

    private Payment createPaymentForBooking(Booking booking) {
        if (booking.getPayment() != null) {
            return booking.getPayment();
        }
        return new Payment(booking);
    }
}
