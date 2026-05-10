package com.auth.domain.Tokens.mapper;

import com.auth.domain.Tokens.dto.TokenDto;
import com.auth.domain.Tokens.dto.TokenResponseDto;
import com.auth.domain.Tokens.dto.TokenUpdateDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Users.entity.User;
import org.springframework.stereotype.Component;


@Component
public class TokenMapper {

    /**
     * Convert TokenDto -> Token Entity
     */
    public Token toEntity(TokenDto tokenDto) {

        if (tokenDto == null) {
            return null;
        }

        return Token.builder()
                .tokenType(tokenDto.getTokenType())
                .tokenValue(tokenDto.getTokenValue())
                .expiryDate(tokenDto.getExpiryDate())
                .revoked(false)
                .expired(false)
                .build();
    }

    /**
     * Update existing Token Entity using TokenUpdateDto
     */
    public void toUpdateEntity(TokenUpdateDto tokenUpdateDto, Token token) {

        if (tokenUpdateDto == null || token == null) {
            return;
        }

        if (tokenUpdateDto.getTokenValue() != null) {
            token.setTokenValue(tokenUpdateDto.getTokenValue());
        }

        if (tokenUpdateDto.getTokenType() != null) {
            token.setTokenType(tokenUpdateDto.getTokenType());
        }

        if (tokenUpdateDto.getRevoked() != null) {
            token.setRevoked(tokenUpdateDto.getRevoked());
        }

        if (tokenUpdateDto.getExpired() != null) {
            token.setExpired(tokenUpdateDto.getExpired());
        }

        if (tokenUpdateDto.getExpiryDate() != null) {
            token.setExpiryDate(tokenUpdateDto.getExpiryDate());
        }
    }

    /**
     * Convert Token Entity -> TokenResponseDto
     */
    public TokenResponseDto tokenResponseDto(Token token) {

        if (token == null) {
            return null;
        }

        User user = token.getUser();

        return TokenResponseDto.builder()
                .id(token.getId())
                .tokenValue(token.getTokenValue())
                .tokenType(token.getTokenType())
                .revoked(token.isRevoked())
                .expired(token.isExpired())
                .expiryDate(token.getExpiryDate())
                .createdAt(token.getCreatedAt())
                .updatedAt(token.getUpdatedAt())
                .userId(user != null ? user.getId() : null)
                .userEmail(user != null ? user.getEmail() : null)
                .message("Token fetched successfully")
                .build();
    }
}