package com.auth.domain.Users.controller;

import com.auth.domain.Users.services.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private IUserService userService;
}
