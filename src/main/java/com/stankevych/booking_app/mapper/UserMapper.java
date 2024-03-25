package com.stankevych.booking_app.mapper;

import com.stankevych.booking_app.config.MapperConfig;
import com.stankevych.booking_app.dto.user.*;
import com.stankevych.booking_app.model.Role;
import com.stankevych.booking_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "rolesId", source = "roles")
    UserWithRolesResponseDto toDtoWithRoles(User user);

    void updateUser(UpdateUserRequestDto requestDto, @MappingTarget User user);

    default Long roleToId(Role role) {
        return role.getId();
    }
}
