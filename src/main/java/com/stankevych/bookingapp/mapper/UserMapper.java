package com.stankevych.bookingapp.mapper;

import com.stankevych.bookingapp.config.MapperConfig;
import com.stankevych.bookingapp.dto.user.UpdateUserRequestDto;
import com.stankevych.bookingapp.dto.user.UserRegistrationRequestDto;
import com.stankevych.bookingapp.dto.user.UserResponseDto;
import com.stankevych.bookingapp.dto.user.UserWithRolesResponseDto;
import com.stankevych.bookingapp.model.Role;
import com.stankevych.bookingapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
