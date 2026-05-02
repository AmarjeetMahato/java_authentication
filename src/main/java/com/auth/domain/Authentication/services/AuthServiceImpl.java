package com.auth.domain.Authentication.services;

import com.auth.domain.Authentication.dto.LoginDto;
import com.auth.domain.Authentication.dto.RegisterDto;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.repository.UserRepsoitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  IAuthService {

    private final UserRepsoitory userRepsoitory;

    @Override
    public void registerUser(RegisterDto registerDto) {

        // ✅ Check if email already exists
        if (userRepsoitory.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // ✅ Convert DTO → Entity
        User user = User.builder()
                .Email(registerDto.getEmail())
                .build();

        // ✅ Save entity (NOT string)
        userRepsoitory.save(user);
    }

    @Override
    public String loginUser(LoginDto loginDto) {
        return "";
    }
}
