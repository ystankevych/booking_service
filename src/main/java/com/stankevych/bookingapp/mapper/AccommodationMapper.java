package com.stankevych.bookingapp.mapper;

import com.stankevych.bookingapp.config.MapperConfig;
import com.stankevych.bookingapp.dto.accommodation.AccommodationRequestDto;
import com.stankevych.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.stankevych.bookingapp.model.Accommodation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AccommodationMapper {
    Accommodation toAccommodation(AccommodationRequestDto requestDto);

    AccommodationResponseDto toAccommodationDto(Accommodation accommodation);

    List<AccommodationResponseDto> toAccommodationDtoList(List<Accommodation> accommodations);

    void updateAccommodation(AccommodationRequestDto requestDto,
                             @MappingTarget Accommodation accommodation);
}
