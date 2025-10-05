package com.librart.managament.service;

import com.librart.managament.dto.UserDTO;
import com.librart.managament.response.*;

import java.util.List;

public interface UserService {

    JwtResponse register(UserDTO userDTO);

    JwtResponse login(AuthRequest authRequest);

    JwtResponse refreshToken(String token);

    boolean validateToken(String token);

    UserResponse findById(String userId);

    UserResponse updateUser(UpdateUser userDTO, String userId);

    List<UserResponse> findAllUsers();

    ApiResponse deleteUser(String userId);

    List<UserResponse> searchUsers(String fullName);

    UserResponse createUser(UserDTO userDTO);

}
