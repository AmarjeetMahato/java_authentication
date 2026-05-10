package com.auth.domain.Authentication.services;

import com.auth.domain.Authentication.dto.AuthResponseDto;
import com.auth.domain.Authentication.dto.LoginDto;
import com.auth.domain.Authentication.dto.RegisterDto;
import com.auth.domain.Authentication.dto.VerifyEmailDto;
import com.auth.domain.Device.dto.DeviceDto;
import com.auth.domain.Device.entity.Device;
import com.auth.domain.Device.repository.DeviceRepository;
import com.auth.domain.Device.service.IDeviceService;
import com.auth.domain.Emails.Services.VerificationEmailImpl;
import com.auth.domain.Tokens.dto.TokenDto;
import com.auth.domain.Tokens.dto.TokenResponseDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.enums.TokenType;
import com.auth.domain.Tokens.repository.TokenRepository;
import com.auth.domain.Tokens.services.ITokenService;
import com.auth.domain.Tokens.services.TokenServiceImpl;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.repository.UserRepsoitory;
import com.auth.globalExceptions.BadRequestException;
import com.auth.security.IJwtService;
import com.auth.security.JwtServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  IAuthService {

    private final UserRepsoitory userRepsoitory;
    private  final ITokenService tokenService;
    private  final TokenRepository tokenRepository;
    private  final VerificationEmailImpl  verificationEmail;
    private  final IJwtService jwtService;
    private  final IDeviceService deviceService;
    private  final DeviceRepository deviceRepository;

    @Override
    public void registerUser(RegisterDto registerDto) {

        // ✅ Check if email already exists
        if (userRepsoitory.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // ✅ Convert DTO → Entity
        // ✅ Create user
        User user = User.builder()
                .Email(registerDto.getEmail())
                .emailVerified(false)
                .isActive(true)
                .build();

        // ✅ Save entity (NOT string)
        User savedUser =   userRepsoitory.save(user);

        // ✅ Generate verification token
        String GenerateVerificationToken = UUID.randomUUID().toString();

        // ✅ Create verification token
        TokenDto tokenDto = TokenDto.builder()
                .tokenValue(GenerateVerificationToken)
                .build();

        // ✅ Save token using token service
        tokenService.createToken(tokenDto);

        // ✅ Send email
        verificationEmail.sendVerificationEmail(
                savedUser.getEmail(),
                tokenDto.getTokenValue()
        );
    }

    @Override
    @Transactional
    public AuthResponseDto verifyEmail(VerifyEmailDto verifyEmailDto, HttpServletRequest request) {

        // ✅ Find token
        Token savedToken = tokenRepository
                .findByTokenValue(verifyEmailDto.getToken())
                .orElseThrow(() ->
                        new RuntimeException("Invalid verification token"));

        // ✅ Check token type
        if (savedToken.getTokenType() != TokenType.EMAIL_VERIFICATION) {
            throw new RuntimeException("Invalid token type");
        }

        // ✅ Check revoked
        if (savedToken.isRevoked()) {
            throw new BadRequestException("Token revoked");
        }

        // ✅ Check expired
        if (savedToken.isExpired() ||
                savedToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new BadRequestException("Token expired");
        }

        User user = savedToken.getUser();

        // ✅ Mark email verified
        user.setEmailVerified(true);

        userRepsoitory.save(user);

        // ✅ Expire verification token
        savedToken.setExpired(true);
        savedToken.setRevoked(true);

        tokenRepository.save(savedToken);


        DeviceDto deviceDto = DeviceDto.builder()

                .deviceId(UUID.randomUUID().toString())
                .deviceName(request.getHeader("User-Agent"))
                .deviceType("DESKTOP")
                .os(request.getHeader("sec-ch-ua-platform"))
                .browser(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .build();

        deviceService.registerDevice(deviceDto, user);

        // ✅ Generate JWT
        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        // ✅ Store refresh token
        Token refreshDbToken = Token.builder()
                .tokenValue(refreshToken)
                .tokenType(TokenType.REFRESH)
                .revoked(false)
                .expired(false)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .user(user)
                .build();

        tokenRepository.save(refreshDbToken);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .emailVerified(true)
                .message("Email verified successfully")
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDto loginUser(LoginDto loginDto) {

        // ✅ Find user
        User user = userRepsoitory
                .findByEmail(loginDto.getEmail())
                .orElseThrow(() ->
                        new BadRequestException(
                                "User not found with this email"
                        ));

        // ✅ Check verified
        if (!user.isEmailVerified()) {
            throw new BadRequestException(
                    "Please verify your email first"
            );
        }

        // ✅ Check active
        if (!user.isActive()) {
            throw new BadRequestException(
                    "User account is inactive"
            );
        }

        // ✅ Generate login token
        String loginToken = UUID.randomUUID().toString();

        // ✅ Save login token
        Token token = Token.builder()
                .tokenValue(loginToken)
                .tokenType(TokenType.LOGIN)
                .revoked(false)
                .expired(false)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        tokenRepository.save(token);

        // ✅ Send login email
        verificationEmail.sendVerificationLoginEmail(user.getEmail(), loginToken);

        // ✅ Response
        return AuthResponseDto.builder()
                .emailVerified(true)
                .message(
                        "Login verification email sent successfully"
                )
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDto verifyLoginToken(VerifyEmailDto verifyEmailDto, HttpServletRequest request) {

        // ✅ Find token
        Token savedToken = tokenRepository.findByTokenValue(verifyEmailDto.getToken())
                     .orElseThrow(() ->  new BadRequestException("Invalid login token"));

        // ✅ Check token type
        if (savedToken.getTokenType() != TokenType.LOGIN) {
            throw new BadRequestException(
                    "Invalid token type"
            );
        }

        // ✅ Check revoked
        if (savedToken.isRevoked()) {
            throw new BadRequestException("Token revoked");
        }

        // ✅ Check expired
        if (savedToken.isExpired() || savedToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new BadRequestException("Token expired");
        }

        User user = savedToken.getUser();

        // ✅ Expire login token
        savedToken.setExpired(true);
        savedToken.setRevoked(true);

        tokenRepository.save(savedToken);

        // ✅ Generate JWT
        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        // ✅ Create device DTO
        DeviceDto deviceDto = DeviceDto.builder()

                .deviceId(UUID.randomUUID().toString())
                .deviceName(request.getHeader("User-Agent"))
                .deviceType("DESKTOP")
                .os(request.getHeader("sec-ch-ua-platform"))
                .browser(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .build();

        // ✅ Register device
        deviceService.registerDevice(deviceDto, user);

        // ✅ Save refresh token
        Token refreshDbToken = Token.builder()
                .tokenValue(refreshToken)
                .tokenType(TokenType.REFRESH)
                .revoked(false)
                .expired(false)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .user(user)
                .build();

        tokenRepository.save(refreshDbToken);

        // ✅ Response
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .emailVerified(true)
                .message("Login successful")
                .build();
    }

    @Override
    @Transactional
    public void logoutDevice(String deviceId) {

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() ->
                        new BadRequestException("Device not found"));

        device.setIsActive(false);
        device.setRefreshToken(null);
        device.setExpiresAt(LocalDateTime.now());

        deviceRepository.save(device);

        tokenRepository.findAllByUserAndTokenType(device.getUser(), TokenType.REFRESH)
                .forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
    }


    @Override
    @Transactional
    public void logoutAllDevices(String userId) {

        if (userId == null || userId.isBlank()) {
            throw new BadRequestException("User ID is required");
        }

        // ✅ Fetch all devices of user
        List<Device> devices = deviceRepository.findAllByUser_Id(userId);

        if (devices.isEmpty()) {
            throw new BadRequestException("No devices found for user");
        }

        // ✅ Deactivate all devices
        devices.forEach(device -> {
            device.setIsActive(false);
            device.setRefreshToken(null);
            device.setExpiresAt(LocalDateTime.now());
        });

        deviceRepository.saveAll(devices);

        // ✅ Revoke all refresh tokens
        List<Token> refreshTokens = tokenRepository.findAllByUser_IdAndTokenType(
                        userId,
                        TokenType.REFRESH
                );

        if (!refreshTokens.isEmpty()) {
            refreshTokens.forEach(token -> {
                token.setRevoked(true);
                token.setExpired(true);
            });

            tokenRepository.saveAll(refreshTokens);
        }
    }


}
