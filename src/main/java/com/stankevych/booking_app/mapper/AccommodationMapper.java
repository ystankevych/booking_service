package com.stankevych.booking_app.mapper;

import com.stankevych.booking_app.config.MapperConfig;
import com.stankevych.booking_app.dto.accommodation.AccommodationRequestDto;
import com.stankevych.booking_app.dto.accommodation.AccommodationResponseDto;
import com.stankevych.booking_app.model.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    Accommodation toAccommodation(AccommodationRequestDto requestDto);

    AccommodationResponseDto toAccommodationDto(Accommodation accommodation);

    List<AccommodationResponseDto> toAccommodationDtoList(List<Accommodation> accommodations);

    void updateAccommodation(AccommodationRequestDto requestDto,
                             @MappingTarget Accommodation accommodation);
}
