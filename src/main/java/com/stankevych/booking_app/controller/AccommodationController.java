package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.accommodation.AccommodationRequestDto;
import com.stankevych.booking_app.dto.accommodation.AccommodationResponseDto;
import com.stankevych.booking_app.service.AccommodationService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AccommodationResponseDto createAccommodation(@RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.createAccommodation(requestDto);
    }

    @GetMapping
    public List<AccommodationResponseDto> getAll(@PageableDefault Pageable pageable) {
        return accommodationService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AccommodationResponseDto getAccommodationById(@PathVariable Long id) {
        return accommodationService.getAccommodationById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public AccommodationResponseDto updateAccommodation(
            @PathVariable Long id,
            @RequestBody AccommodationRequestDto requestDto) {
        return accommodationService.updateAccommodationById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteAccommodation(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }
}
