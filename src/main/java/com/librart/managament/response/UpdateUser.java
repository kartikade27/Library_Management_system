package com.librart.managament.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUser {

    @NotBlank(message = "Full name is required !")
    private String fullName;

    @Email(message = "Email should be required !")
    @NotBlank(message = "Email is required !")
    private String email;

    @NotBlank(message = "phone number is required !")
    private String phoneNumber;

    private String role;
}
