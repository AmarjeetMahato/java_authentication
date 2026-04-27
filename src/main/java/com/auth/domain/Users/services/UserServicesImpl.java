package com.auth.domain.Users.services;

import com.auth.domain.Users.repository.UserRepsoitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServicesImpl {

    private UserRepsoitory userRepsoitory;
}
