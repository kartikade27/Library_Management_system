package com.librart.managament.dto;

import com.librart.managament.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String userId;

    @NotBlank(message = "Full name is required!")
    private String fullName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email should be valid!")
    private String email;

    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    @NotBlank(message = "Password is required!")
    private String password;

    private Role role;
}
