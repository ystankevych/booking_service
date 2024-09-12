package com.stankevych.bookingapp.service;

import com.stankevych.bookingapp.dto.user.UpdateUserByRoleIdDto;
import com.stankevych.bookingapp.dto.user.UpdateUserRequestDto;
import com.stankevych.bookingapp.dto.user.UserRegistrationRequestDto;
import com.stankevych.bookingapp.dto.user.UserResponseDto;
import com.stankevych.bookingapp.dto.user.UserWithRolesResponseDto;
import com.stankevych.bookingapp.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserWithRolesResponseDto updateUserRoles(Long userId, UpdateUserByRoleIdDto requestDto);

    UserResponseDto getMyProfileInfo(User user);

    UserResponseDto updateMyProfile(UpdateUserRequestDto requestDto, User user);
}
