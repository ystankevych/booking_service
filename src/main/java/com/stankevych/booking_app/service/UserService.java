package com.stankevych.booking_app.service;

import com.stankevych.booking_app.dto.user.*;
import com.stankevych.booking_app.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserWithRolesResponseDto updateUserRoles(Long userId, UpdateUserByRoleIdDto requestDto);

    UserResponseDto getMyProfileInfo(User user);

    UserResponseDto updateMyProfile(UpdateUserRequestDto requestDto, User user);
}
