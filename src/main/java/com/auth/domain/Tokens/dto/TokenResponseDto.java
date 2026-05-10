package com.auth.domain.Tokens.dto;

import com.auth.domain.Tokens.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDto {

    private String id;

    private String tokenValue;

    private TokenType tokenType;

    private boolean revoked;

    private boolean expired;

    private LocalDateTime expiryDate;

    private String userId;

    private String userEmail;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String message;
}
