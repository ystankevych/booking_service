package com.stankevych.booking_app.service.impl;

import com.stankevych.booking_app.dto.booking.BookingResponseDto;
import com.stankevych.booking_app.dto.booking.CreateBookingRequestDto;
import com.stankevych.booking_app.exception.BookingException;
import com.stankevych.booking_app.exception.EntityNotFoundException;
import com.stankevych.booking_app.mapper.BookingMapper;
import com.stankevych.booking_app.model.Accommodation;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.repository.AccommodationRepository;
import com.stankevych.booking_app.repository.BookingRepository;
import com.stankevych.booking_app.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponseDto createBooking(User user, CreateBookingRequestDto requestDto) {
        var accommodation = accommodationRepository.findById(requestDto.accommodationId())
                .orElseThrow(() -> new EntityNotFoundException("""
                        No such accommodation with id: %s""".formatted(requestDto.accommodationId())));
        checkAccommodationAvailability(accommodation,
                requestDto.checkInDate(), requestDto.checkOutDate());
        checkPendingBooking(user.getId(), Booking.Status.PENDING);
        var booking = bookingMapper.toBooking(requestDto, user);
        accommodation.addBooking(booking);
        accommodationRepository.save(accommodation);
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
        var booking = bookingRepository.findByIdAndUserIdAndStatusIs(bookingId,
                userId, Booking.Status.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("""
                        There is no unpaid booking with id '%d' 
                        for user with id '%d'.""".formatted(bookingId, userId)));
        bookingRepository.delete(booking);
    }

    private void checkAccommodationAvailability(Accommodation accommodation,
                                                LocalDate from, LocalDate to) {
        if (!accommodation.isAvailableOnDates(from, to)) {
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
