package com.stankevych.bookingapp.controller;

import com.stankevych.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.stankevych.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.stankevych.bookingapp.service.AccommodationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
@Validated
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationResponseDto createAccommodation(
            @RequestBody @Valid AccommodationRequestDto requestDto) {
        return accommodationService.createAccommodation(requestDto);
    }

    @GetMapping
    public List<AccommodationResponseDto> getAll(@PageableDefault Pageable pageable) {
        return accommodationService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AccommodationResponseDto getAccommodationById(@PathVariable @Positive Long id) {
        return accommodationService.getAccommodationById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AccommodationResponseDto updateAccommodation(
            @PathVariable @Positive Long id,
            @RequestBody @Valid AccommodationRequestDto requestDto) {
        return accommodationService.updateAccommodationById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccommodation(@PathVariable @Positive Long id) {
        accommodationService.deleteAccommodation(id);
    }
}
