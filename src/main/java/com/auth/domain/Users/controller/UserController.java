package com.auth.domain.Users.controller;
import com.auth.domain.Users.dto.UserResponseDto;
import com.auth.domain.Users.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

     GetMapping("/{userId}/get_user")
     public ResponseEntity<UserResponseDto>  fetch_User(@PathVariable String  userId){
         UserResponseDto user = userService.getUserById(userId);
         return ResponseEntity.status(HttpStatus.OK).body(user);
     }


}
