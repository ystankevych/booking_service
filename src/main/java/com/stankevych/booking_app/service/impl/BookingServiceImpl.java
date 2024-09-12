package com.stankevych.booking_app.service.impl;

import com.stankevych.booking_app.dto.booking.BookingResponseDto;
import com.stankevych.booking_app.dto.booking.CreateBookingRequestDto;
import com.stankevych.booking_app.dto.booking.UpdateBookingRequestDto;
import com.stankevych.booking_app.exception.BookingException;
import com.stankevych.booking_app.exception.EntityNotFoundException;
import com.stankevych.booking_app.mapper.BookingMapper;
import com.stankevych.booking_app.model.Accommodation;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.Payment;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.repository.AccommodationRepository;
import com.stankevych.booking_app.repository.BookingRepository;
import com.stankevych.booking_app.service.BookingService;
import com.stankevych.booking_app.service.PaymentService;
import com.stankevych.booking_app.service.TelegramNotificationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;
    private final PaymentService paymentService;
    private final TelegramNotificationService notificationService;

    @Override
    @Transactional
    public BookingResponseDto createBooking(User user, CreateBookingRequestDto requestDto) {
        var accommodation = accommodationRepository
                .findById(requestDto.accommodationId())
                .orElseThrow(() -> new EntityNotFoundException("""
                        No such accommodation with id: %s"""
                        .formatted(requestDto.accommodationId())));

        checkAccommodationAvailability(accommodation,
                requestDto.checkInDate(), requestDto.checkOutDate());
        checkPendingBooking(user.getId(), Booking.Status.PENDING);

        var booking = bookingMapper.toBooking(requestDto, accommodation, user);
        bookingRepository.save(booking);
        notificationService.notifyAboutCreatedBooking(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBooking(User user, Long id, UpdateBookingRequestDto requestDto) {
        var booking = bookingRepository
                .findByIdAndUserIdAndStatusIs(id, user.getId(), Booking.Status.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("""
                        No such booking with id '%s' for user with id '%s'"""
                        .formatted(id, user.getId())));

        checkAccommodationAvailability(booking.getAccommodation(),
                requestDto.checkInDate(), requestDto.checkOutDate());

        if (booking.getPayment() != null) {
            paymentService.expirePaymentSession(booking.getPayment());
            booking.setPayment(null);
        }

        bookingMapper.updateBooking(requestDto, booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllByUserAndStatus(Long userId, Booking.Status status) {
        return bookingMapper.toDtoList(bookingRepository
                .findAllByUserIdAndStatus(userId, status));
    }

    @Override
    public List<BookingResponseDto> getUsersBookings(Long userId) {
        return bookingMapper.toDtoList(bookingRepository
                .findAllByUserId(userId));
    }

    @Override
    public void deleteBooking(Long userId, Long bookingId) {
        bookingRepository.findByIdAndUserIdAndStatusIs(bookingId, userId, Booking.Status.PENDING)
                .ifPresent(b -> {
                    if (b.getPayment() != null) {
                        paymentService.expirePaymentSession(b.getPayment());
                    }
                    bookingRepository.delete(b);
                });
    }

    private void checkAccommodationAvailability(Accommodation accommodation,
                                                LocalDate from, LocalDate to) {
        int count = bookingRepository.countBookingsOnDate(accommodation.getId(),
                from, to);
        if (count >= accommodation.getAvailability()) {
            throw new BookingException("""
                    No available accommodation with id '%d' on dates '%s-%s'"""
                    .formatted(accommodation.getId(), from, to));
        }
    }

    private void checkPendingBooking(Long userId, Booking.Status status) {
        if (bookingRepository.existsByUserIdAndStatusIs(userId, status)) {
            throw new BookingException("""
                    An unpaid booking for the user with id '%d' was detected"""
                    .formatted(userId));
        }
    }
}
