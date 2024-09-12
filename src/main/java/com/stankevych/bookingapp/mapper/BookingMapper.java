package com.stankevych.bookingapp.mapper;

import com.stankevych.bookingapp.config.MapperConfig;
import com.stankevych.bookingapp.dto.booking.BookingResponseDto;
import com.stankevych.bookingapp.dto.booking.CreateBookingRequestDto;
import com.stankevych.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.stankevych.bookingapp.model.Accommodation;
import com.stankevych.bookingapp.model.Booking;
import com.stankevych.bookingapp.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(source = "accommodation", target = "accommodation")
    @Mapping(target = "id", ignore = true)
    Booking toBooking(CreateBookingRequestDto requestDto,
                      Accommodation accommodation, User user);

    @Mapping(source = "id", target = "bookingId")
    @Mapping(source = "accommodation.id", target = "accommodationId")
    @Mapping(source = "user.id", target = "userId")
    BookingResponseDto toDto(Booking booking);

    List<BookingResponseDto> toDtoList(List<Booking> bookings);

    void updateBooking(UpdateBookingRequestDto requestDto, @MappingTarget Booking booking);
}
