package com.auth.domain.Authentication.services;

import com.auth.domain.Authentication.dto.AuthResponseDto;
import com.auth.domain.Authentication.dto.LoginDto;
import com.auth.domain.Authentication.dto.RegisterDto;
import com.auth.domain.Authentication.dto.VerifyEmailDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {

    void  registerUser(RegisterDto registerDto);

    AuthResponseDto loginUser(LoginDto loginDto);

    AuthResponseDto verifyEmail(VerifyEmailDto verifyEmailDto, HttpServletRequest request);

    AuthResponseDto verifyLoginToken(VerifyEmailDto verifyEmailDto, HttpServletRequest request);

    // 🔥 Logout current device/session
    void logoutDevice(String deviceId);

    // 🔥 Logout all devices (security reset)
    void logoutAllDevices(String userId);

}

