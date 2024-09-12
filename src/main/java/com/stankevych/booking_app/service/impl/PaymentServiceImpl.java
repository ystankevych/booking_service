package com.stankevych.booking_app.service.impl;

import static com.stankevych.booking_app.model.Payment.Status.PAID;
import static com.stankevych.booking_app.model.Payment.Status.PENDING;

import com.stankevych.booking_app.dto.payment.CanceledPaymentResponseDto;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .findByIdAndUserIdAndStatusIsAndUnpaidTermAfter(bookingId, userId,
                        Booking.Status.PENDING, LocalDateTime.now())
                .orElseThrow(() -> new EntityNotFoundException("""
                        No unpaid booking with id '%d" for user with id '%d' found"""
                        .formatted(bookingId, userId)));

        var payment = new Payment(booking);
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
    public CanceledPaymentResponseDto cancelPayment(String sessionId) {
        var payment = paymentRepository
                .findBySessionIdAndStatusIs(sessionId, PENDING)
                .orElseThrow(() -> new PaymentException("""
                        No unpaid payment with session id '%s' found"""
                        .formatted(sessionId)));
        expirePaymentSession(payment);
        var canceledPaymentResponse = new CanceledPaymentResponseDto(payment.getBooking().getId(),
                payment.getBooking().getUnpaidTerm());
        paymentRepository.delete(payment);
        return canceledPaymentResponse;
    }

    @Override
    public void expirePaymentSession(Payment payment) {
        stripeService.expireSession(payment.getSessionId());
    }

    private void checkSessionCompletion(String sessionId) {
        if (!stripeService.isSessionPaid(sessionId)) {
            throw new PaymentException("Unpaid session found");
        }
    }
}
