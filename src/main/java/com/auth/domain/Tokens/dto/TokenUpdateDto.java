package com.auth.domain.Tokens.dto;


import com.auth.domain.Tokens.enums.TokenType;
import jakarta.validation.constraints.Future;
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
public class TokenUpdateDto {

    @Size(min = 10, message = "Token value must be at least 10 characters")
    private String tokenValue;

    private TokenType tokenType;

    private Boolean revoked;

    private Boolean expired;

    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;
}