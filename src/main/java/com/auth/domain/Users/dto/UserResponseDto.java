package com.auth.domain.Users.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String id;

    private String firstName;
    private String lastName;

    private String email;
    private String phone;

    private String profilePic;
    private String gender;

    private boolean isActive;
    private boolean emailVerified;

    private String provider;

    private Set<String> roles; // Only role names (safe)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
