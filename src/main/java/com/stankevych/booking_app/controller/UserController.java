package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.user.UpdateUserByRoleIdDto;
import com.stankevych.booking_app.dto.user.UpdateUserRequestDto;
import com.stankevych.booking_app.dto.user.UserResponseDto;
import com.stankevych.booking_app.dto.user.UserWithRolesResponseDto;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public UserWithRolesResponseDto updateRoles(@PathVariable Long id,
                                                @RequestBody UpdateUserByRoleIdDto requestDto) {
        return userService.updateUserRoles(id, requestDto);
    }

    @GetMapping("/me")
    public UserResponseDto getMyProfileInfo(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return userService.getMyProfileInfo(user);
    }

    @PutMapping("/me")
    public UserResponseDto updateMyProfile(@RequestBody UpdateUserRequestDto requestDto,
                                           Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return userService.updateMyProfile(requestDto, user);
    }
}
