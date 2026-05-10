package com.auth.domain.Users.services;

import com.auth.domain.Users.dto.UpdateUserDto;
import com.auth.domain.Users.dto.UserResponseDto;

public interface IUserService {

    UserResponseDto UpdateUser(UpdateUserDto updateUserDto,String userId);

    UserResponseDto getUserById(String userId);

    UserResponseDto getUserByEmail(String email);

    void  deleteUser(String userId);



}
