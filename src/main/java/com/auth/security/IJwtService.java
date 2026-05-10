package com.auth.security;


import com.auth.domain.Users.entity.User;

public interface IJwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractEmail(String token);

    boolean isTokenValid(String token, User user);
}
