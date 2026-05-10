package com.auth.domain.Authentication.controller;

import com.auth.domain.Authentication.dto.AuthResponseDto;
import com.auth.domain.Authentication.dto.LoginDto;
import com.auth.domain.Authentication.dto.RegisterDto;
import com.auth.domain.Authentication.dto.VerifyEmailDto;
import com.auth.domain.Authentication.services.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        authService.registerUser(registerDto);
        return ResponseEntity.ok("User registered successfully. Please verify your email.");
    }

    // ---------------- LOGIN REQUEST ----------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.loginUser(loginDto));
    }

    // ---------------- VERIFY EMAIL ----------------
    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponseDto> verifyEmail(
            @Valid @RequestBody VerifyEmailDto verifyEmailDto,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(authService.verifyEmail(verifyEmailDto, request));
    }

    // ---------------- VERIFY LOGIN TOKEN ----------------
    @PostMapping("/verify-login")
    public ResponseEntity<AuthResponseDto> verifyLoginToken(
            @Valid @RequestBody VerifyEmailDto verifyEmailDto,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                authService.verifyLoginToken(verifyEmailDto, request)
        );
    }

    // ---------------- LOGOUT DEVICE ----------------
    @PostMapping("/logout/device/{deviceId}")
    public ResponseEntity<String> logoutDevice(@PathVariable String deviceId) {
        authService.logoutDevice(deviceId);
        return ResponseEntity.ok("Device logged out successfully");
    }

    // ---------------- LOGOUT ALL DEVICES ----------------
    @PostMapping("/logout/all/{userId}")
    public ResponseEntity<String> logoutAllDevices(@PathVariable String userId) {
        authService.logoutAllDevices(userId);
        return ResponseEntity.ok("All devices logged out successfully");
    }
}