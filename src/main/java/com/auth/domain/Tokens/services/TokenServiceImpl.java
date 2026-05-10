package com.auth.domain.Tokens.services;

import com.auth.domain.Tokens.dto.TokenDto;
import com.auth.domain.Tokens.dto.TokenResponseDto;
import com.auth.domain.Tokens.dto.TokenUpdateDto;
import com.auth.domain.Tokens.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements  ITokenService {

    private  final TokenRepository tokenRepository;

    @Override
    public TokenResponseDto createToken(TokenDto tokenDto) {
        return null;
    }

    @Override
    public TokenResponseDto updateToken(TokenUpdateDto updateDto) {
        return null;
    }
}
