package com.librart.managament.response;

import com.librart.managament.dto.UserDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private UserResponse userDTO;
}
