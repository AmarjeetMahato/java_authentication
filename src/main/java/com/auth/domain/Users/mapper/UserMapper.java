package com.auth.domain.Users.mapper;

import com.auth.domain.Tokens.entity.Token;
import com.auth.domain.Users.dto.UserDto;
import com.auth.domain.Users.dto.UserResponseDto;
import com.auth.domain.Users.entity.User;
import com.auth.globalExceptions.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto userDto){
        if(userDto == null) return  null;

        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .Email(userDto.getEmail())
                .phone(userDto.getPhone())
                .gender(userDto.getGender())
                .profilePic(userDto.getProfilePic())
                .provider(userDto.getProvider())
                .isActive(userDto.isActive())
                .build();
    }


    public UserResponseDto toResponse(User user){

        if(user == null){
             throw new BadRequestException("user should not be null");
        }

        return  UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profilePic(user.getProfilePic())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

    }


}
