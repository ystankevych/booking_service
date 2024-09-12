package com.stankevych.bookingapp.controller;

import com.stankevych.bookingapp.dto.user.UpdateUserByRoleIdDto;
import com.stankevych.bookingapp.dto.user.UpdateUserRequestDto;
import com.stankevych.bookingapp.dto.user.UserResponseDto;
import com.stankevych.bookingapp.dto.user.UserWithRolesResponseDto;
import com.stankevych.bookingapp.model.User;
import com.stankevych.bookingapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public UserWithRolesResponseDto updateRoles(@PathVariable @Positive Long id,
                                                @RequestBody @Valid UpdateUserByRoleIdDto request) {
        return userService.updateUserRoles(id, request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    public UserResponseDto getMyProfileInfo(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return userService.getMyProfileInfo(user);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CUSTOMER')")
    public UserResponseDto updateMyProfile(@RequestBody @Valid UpdateUserRequestDto requestDto,
                                           Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return userService.updateMyProfile(requestDto, user);
    }
}
