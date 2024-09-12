package com.stankevych.bookingapp.mapper;

import com.stankevych.bookingapp.config.MapperConfig;
import com.stankevych.bookingapp.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.bookingapp.dto.payment.PaymentResponseDto;
import com.stankevych.bookingapp.model.Payment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "id", target = "id")
    PaymentResponseDto toDto(Payment payment);

    PaymentDtoWithoutSession toDtoWithoutSession(Payment payment);

    List<PaymentDtoWithoutSession> toDtoListWithoutSession(List<Payment> payments);
}
