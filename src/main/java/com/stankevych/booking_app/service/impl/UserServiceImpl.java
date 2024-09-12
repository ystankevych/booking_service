package com.stankevych.booking_app.service.impl;

import com.stankevych.booking_app.dto.user.UpdateUserByRoleIdDto;
import com.stankevych.booking_app.dto.user.UpdateUserRequestDto;
import com.stankevych.booking_app.dto.user.UserRegistrationRequestDto;
import com.stankevych.booking_app.dto.user.UserResponseDto;
import com.stankevych.booking_app.dto.user.UserWithRolesResponseDto;
import com.stankevych.booking_app.exception.EntityNotFoundException;
import com.stankevych.booking_app.exception.RegistrationException;
import com.stankevych.booking_app.mapper.UserMapper;
import com.stankevych.booking_app.model.Role;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.repository.RoleRepository;
import com.stankevych.booking_app.repository.UserRepository;
import com.stankevych.booking_app.service.UserService;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Long CUSTOMER_ROLE_ID = 3L;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private Role customerRole;

    @PostConstruct
    public void init() {
        customerRole = roleRepository.getReferenceById(CUSTOMER_ROLE_ID);
    }

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (repository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with email '%s' already exists"
                    .formatted(requestDto.email()));
        }
        var user = userMapper.toUser(requestDto);
        user.addRole(customerRole);
        user.setPassword(encoder.encode(requestDto.password()));
        repository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserWithRolesResponseDto updateUserRoles(Long userId, UpdateUserByRoleIdDto requestDto) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("""
                        User with id '%d' doesn't exist""".formatted(userId)));
        user.setRoles(getRolesById(requestDto.rolesId()));
        return userMapper.toDtoWithRoles(user);
    }

    @Override
    public UserResponseDto getMyProfileInfo(User user) {
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateMyProfile(UpdateUserRequestDto requestDto, User user) {
        if (!requestDto.email().equals(user.getEmail()) && repository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with email '%s' already exists"
                    .formatted(requestDto.email()));
        }
        userMapper.updateUser(requestDto, user);
        return userMapper.toDto(user);
    }

    private Set<Role> getRolesById(Set<Long> rolesId) {
        return rolesId.stream()
                .map(roleRepository::getReferenceById)
                .collect(Collectors.toSet());
    }
}
