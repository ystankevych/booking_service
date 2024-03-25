package com.stankevych.booking_app.mapper;

import com.stankevych.booking_app.config.MapperConfig;
import com.stankevych.booking_app.dto.booking.BookingResponseDto;
import com.stankevych.booking_app.dto.booking.CreateBookingRequestDto;
import com.stankevych.booking_app.model.Accommodation;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}
