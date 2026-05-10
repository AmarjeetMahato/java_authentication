package com.auth.domain.Users.services;

import com.auth.domain.Users.dto.UpdateUserDto;
import com.auth.domain.Users.dto.UserResponseDto;
import com.auth.domain.Users.entity.User;
import com.auth.domain.Users.mapper.UserMapper;
import com.auth.domain.Users.repository.UserRepsoitory;
import com.auth.globalExceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements  IUserService {

    private final UserRepsoitory userRepsoitory;
    private final UserMapper  userMapper;

    @Override
    public UserResponseDto UpdateUser(UpdateUserDto updateUserDto, String userId) {

        User user =  userRepsoitory.findById(userId).orElseThrow(
                ()-> new  ResourceNotFoundException("User not found with Id")
        );

    }

    @Override
    public UserResponseDto getUserById(String userId) {

        User user =  userRepsoitory.findById(userId).orElseThrow(
                ()-> new  ResourceNotFoundException("User not found with Id")
        );
        return  userMapper.toResponse(user);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user =  userRepsoitory.findByEmail(email).orElseThrow(
                ()-> new  ResourceNotFoundException("User not found with email")
        );
        return  userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user =  userRepsoitory.findById(userId).orElseThrow(
                ()-> new  ResourceNotFoundException("User not found with email")
        );
        userRepsoitory.delete(user);
    }
}
