package com.stankevych.booking_app.service;

import com.stankevych.booking_app.dto.accommodation.AccommodationRequestDto;
import com.stankevych.booking_app.dto.accommodation.AccommodationResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponseDto createAccommodation(AccommodationRequestDto accommodation);

    List<AccommodationResponseDto> getAll(Pageable pageable);

    AccommodationResponseDto getAccommodationById(Long id);

    AccommodationResponseDto updateAccommodationById(Long id, AccommodationRequestDto requestDto);

    void deleteAccommodation(Long id);
}
