package com.auth.domain.Tokens.services;

import com.auth.domain.Tokens.dto.TokenDto;
import com.auth.domain.Tokens.dto.TokenResponseDto;
import com.auth.domain.Tokens.dto.TokenUpdateDto;
import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Tokens.mapper.TokenMapper;
import com.auth.domain.Tokens.repository.TokenRepository;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.repository.UserRepsoitory;
import com.auth.globalExceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements  ITokenService {

    private  final TokenRepository tokenRepository;
    private  final UserRepsoitory userRepsoitory;
    private  final TokenMapper tokenMapper;

    @Override
    public TokenResponseDto createToken(TokenDto tokenDto) {
        User user = userRepsoitory.findById(tokenDto.getUserId()).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );

        // ✅ Convert DTO -> Entity
        Token token = tokenMapper.toEntity(tokenDto);

        // ✅ Attach user
        token.setUser(user);

        // ✅ Save token
        Token savedToken = tokenRepository.save(token);

        // ✅ Response
        TokenResponseDto responseDto = tokenMapper.tokenResponseDto(savedToken);

        responseDto.setMessage("Token created successfully");

        return  responseDto;
    }

    @Override
    public TokenResponseDto updateToken(String tokenId,TokenUpdateDto updateDto) {
        // ✅ Find existing token
        Token existingToken =  tokenRepository.findById(tokenId).orElseThrow(() ->
                new ResourceNotFoundException("Token not found"));

        // ✅ Update token fields
         tokenMapper.toUpdateEntity(updateDto,existingToken);

        // ✅ Save updated token
        Token updateToken =  tokenRepository.save(existingToken);

        // ✅ Response
        TokenResponseDto tokenResponseDto = tokenMapper.tokenResponseDto(updateToken);

        tokenResponseDto.setMessage("Token updated successfully");

        return  tokenResponseDto;

    }

    @Override
    public TokenResponseDto getByTokenValue(String tokenValue) {
        Token token = tokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() ->
                        new RuntimeException("Token not found"));

        TokenResponseDto responseDto =
                tokenMapper.tokenResponseDto(token);

        responseDto.setMessage("Token fetched successfully");

        return responseDto;
    }

    @Override
    public void revokeToken(String tokenId) {

        Token token = tokenRepository.findById(tokenId)
                .orElseThrow(() ->
                        new RuntimeException("Token not found"));

        token.setRevoked(true);
        token.setExpired(true);

        tokenRepository.save(token);
    }
}
