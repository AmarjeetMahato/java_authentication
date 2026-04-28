package com.auth.domain.Users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Pattern(
            regexp = "MALE|FEMALE|OTHER",
            message = "Gender must be MALE, FEMALE, or OTHER"
    )
    private String gender;

    @Size(max = 500, message = "Profile picture URL too long")
    private String profilePic;

    private Boolean isActive;

}
