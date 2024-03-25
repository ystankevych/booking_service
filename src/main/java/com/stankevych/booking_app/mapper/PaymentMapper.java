package com.stankevych.booking_app.mapper;

import com.stankevych.booking_app.config.MapperConfig;
import com.stankevych.booking_app.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.booking_app.dto.payment.PaymentResponseDto;
import com.stankevych.booking_app.model.Payment;
import com.stripe.model.checkout.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "id", target = "id")
    PaymentResponseDto toDto(Payment payment);

    PaymentDtoWithoutSession toDtoWithoutSession(Payment payment);

    List<PaymentDtoWithoutSession> toDtoListWithoutSession(List<Payment> payments);
}
