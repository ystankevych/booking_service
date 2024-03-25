package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.user.UserLoginRequestDto;
import com.stankevych.booking_app.dto.user.UserLoginResponseDto;
import com.stankevych.booking_app.dto.user.UserRegistrationRequestDto;
import com.stankevych.booking_app.dto.user.UserResponseDto;
import com.stankevych.booking_app.security.AuthenticationService;
import com.stankevych.booking_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @GetMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
