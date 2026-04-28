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

    private UserRepsoitory userRepsoitory;

    @Override
    public void registerUser(RegisterDto registerDto, request HttpRequest) {

        User exitsingUser = userRepsoitory.findById(registerDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email already resister "));


        userRepsoitory.save(registerDto.getEmail());

    }

    @Override
    public String loginUser(LoginDto loginDto) {
        return "";
    }
}
