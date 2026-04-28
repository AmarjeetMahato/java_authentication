package com.auth.domain.Authentication.services;

import com.auth.domain.Authentication.dto.LoginDto;
import com.auth.domain.Authentication.dto.RegisterDto;

public interface IAuthService {

    void  registerUser(RegisterDto registerDto);

    String loginUser(LoginDto loginDto);
}
