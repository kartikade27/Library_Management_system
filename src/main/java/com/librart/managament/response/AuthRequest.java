package com.librart.managament.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Email is required!")
    @Email(message = "Email should be valid!")
    private String email;
    @NotBlank(message = "Password is required!")
    private String password;
}
