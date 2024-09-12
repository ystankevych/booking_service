package com.stankevych.bookingapp.service.impl;

import com.stankevych.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.stankevych.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.stankevych.bookingapp.exception.EntityNotFoundException;
import com.stankevych.bookingapp.mapper.AccommodationMapper;
import com.stankevych.bookingapp.model.Booking;
import com.stankevych.bookingapp.model.Payment;
import com.stankevych.bookingapp.repository.AccommodationRepository;
import com.stankevych.bookingapp.service.AccommodationService;
import com.stankevych.bookingapp.service.PaymentService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository repository;
    private final AccommodationMapper mapper;
    private final PaymentService paymentService;

    @Override
    public AccommodationResponseDto createAccommodation(AccommodationRequestDto requestDto) {
        var accommodation = mapper.toAccommodation(requestDto);
        return mapper.toAccommodationDto(repository.save(accommodation));
    }

    @Override
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return mapper
                .toAccommodationDtoList(repository.findAll(pageable).toList());
    }

    @Override
    public AccommodationResponseDto getAccommodationById(Long id) {
        return mapper.toAccommodationDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("""
                        Can't find accommodation by id: '%s'""".formatted(id))));
    }

    @Override
    public AccommodationResponseDto updateAccommodationById(Long id,
                                                            AccommodationRequestDto requestDto) {
        var accommodation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("""
                        Can't find accommodation by id: '%s'"""
                        .formatted(id)));
        mapper.updateAccommodation(requestDto, accommodation);
        return mapper.toAccommodationDto(repository.save(accommodation));
    }

    @Override
    public void deleteAccommodation(Long id) {
        repository.findById(id)
                .ifPresent(accommodation -> {
                    accommodation.getBookings().stream()
                            .map(Booking::getPayment)
                            .filter(Objects::nonNull)
                            .filter(p -> p.getStatus() == Payment.Status.PENDING)
                            .forEach(paymentService::expirePaymentSession);
                    repository.delete(accommodation);
                });
    }
}
