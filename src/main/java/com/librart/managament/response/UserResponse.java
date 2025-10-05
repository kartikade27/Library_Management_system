package com.librart.managament.response;

import com.librart.managament.model.Role;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String role;
}
