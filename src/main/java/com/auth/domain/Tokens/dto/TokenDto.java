package com.auth.domain.Tokens.dto;

import com.auth.domain.Tokens.enums.TokenType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {

    @NotBlank(message = "Token value is required")
    @Size(min = 10, message = "Token value must be at least 10 characters")
    private String tokenValue;

    @NotNull(message = "Token type is required")
    private TokenType tokenType;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;

    @NotBlank(message = "User ID is required")
    private String userId;
}
