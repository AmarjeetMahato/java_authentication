package com.auth.domain.Tokens.services;

import com.auth.domain.Tokens.dto.TokenDto;
import com.auth.domain.Tokens.dto.TokenResponseDto;
import com.auth.domain.Tokens.dto.TokenUpdateDto;

public interface ITokenService {

    TokenResponseDto createToken(TokenDto tokenDto);

    TokenResponseDto updateToken(String tokenId, TokenUpdateDto updateDto);

    TokenResponseDto getByTokenValue(String tokenValue);

    void revokeToken(String tokenId);
}
