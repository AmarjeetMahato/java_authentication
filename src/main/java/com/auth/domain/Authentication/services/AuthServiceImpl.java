package com.auth.domain.Authentication.services;

import com.auth.domain.Users.repository.UserRepsoitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private UserRepsoitory userRepsoitory;
}
