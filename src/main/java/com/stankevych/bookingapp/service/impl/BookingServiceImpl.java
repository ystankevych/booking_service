package com.stankevych.bookingapp.service.impl;

import com.stankevych.bookingapp.dto.booking.BookingResponseDto;
import com.stankevych.bookingapp.dto.booking.CreateBookingRequestDto;
import com.stankevych.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.stankevych.bookingapp.exception.BookingException;
import com.stankevych.bookingapp.exception.EntityNotFoundException;
import com.stankevych.bookingapp.mapper.BookingMapper;
import com.stankevych.bookingapp.model.Accommodation;
import com.stankevych.bookingapp.model.Booking;
import com.stankevych.bookingapp.model.User;
import com.stankevych.bookingapp.repository.AccommodationRepository;
import com.stankevych.bookingapp.repository.BookingRepository;
import com.stankevych.bookingapp.service.BookingService;
import com.stankevych.bookingapp.service.PaymentService;
import com.stankevych.bookingapp.service.TelegramNotificationService;
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
    public BookingResponseDto updateBooking(User user, Long id, UpdateBookingRequestDto request) {
        var booking = bookingRepository
                .findByIdAndUserIdAndStatusIs(id, user.getId(), Booking.Status.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("""
                        No such booking with id '%s' for user with id '%s'"""
                        .formatted(id, user.getId())));

        checkAccommodationAvailability(booking.getAccommodation(),
                request.checkInDate(), request.checkOutDate());

        if (booking.getPayment() != null) {
            paymentService.expirePaymentSession(booking.getPayment());
            booking.setPayment(null);
        }

        bookingMapper.updateBooking(request, booking);
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
