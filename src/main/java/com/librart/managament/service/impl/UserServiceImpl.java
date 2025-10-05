package com.librart.managament.service.impl;

import com.librart.managament.config.CustomUserDetails;
import com.librart.managament.dto.UserDTO;
import com.librart.managament.exception.ResourceAlreadyExistsException;
import com.librart.managament.exception.ResourceNotFoundException;
import com.librart.managament.jwt.JwtService;
import com.librart.managament.model.Role;
import com.librart.managament.model.User;
import com.librart.managament.repository.UserRepository;
import com.librart.managament.response.*;
import com.librart.managament.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse register(UserDTO userDTO) {
        User user = this.convertUserDTOToEntity(userDTO);
        if (this.userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("User Already Exists!");
        }
        user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        User userSaved = this.userRepository.save(user);
        UserDTO userDTO1 = this.convertEntityToUserDTO(userSaved);
        UserResponse response = this.toResponse(userDTO1);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = this.jwtService.generateToken(customUserDetails);
        String refreshToken = this.jwtService.refreshToken(customUserDetails);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDTO(response)
                .build();
    }

    @Override
    public JwtResponse login(AuthRequest authRequest) {
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password!");
        }
        User user = this.userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        UserDTO userDTO = this.convertEntityToUserDTO(user);
        UserResponse response = this.toResponse(userDTO);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = this.jwtService.generateToken(customUserDetails);
        String refreshToken = this.jwtService.refreshToken(customUserDetails);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDTO(response)
                .build();
    }

    @Override
    public JwtResponse refreshToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Refresh token missing");
        }

        try {
            // 1. Extract username/email from refresh token
            String username = jwtService.extractUsername(token);
            if (username == null) {
                throw new RuntimeException("Invalid refresh token");
            }

            // 2. Fetch user
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

            // 3. Validate refresh token
            CustomUserDetails userDetails = new CustomUserDetails(user);
            if (!jwtService.isTokenValid(token, userDetails)) {
                throw new RuntimeException("Refresh token expired or invalid!");
            }

            // 4. Generate new tokens
            String accessToken = jwtService.generateToken(userDetails);
            String newRefreshToken = jwtService.refreshToken(userDetails);

            // 5. Prepare response
            UserDTO userDTO = convertEntityToUserDTO(user);
            UserResponse response = toResponse(userDTO);

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .userDTO(response)
                    .build();

        } catch (ResourceNotFoundException e) {
            // Token invalid → return 401
            throw new RuntimeException("Unauthorized: " + e.getMessage());
        } catch (Exception e) {
            // Any other error → log and throw 400
            throw new RuntimeException("Refresh token failed: " + e.getMessage());
        }
    }


    @Override
    public boolean validateToken(String token) {
        return this.jwtService.validateToken(token);
    }

    @Override
    public UserResponse findById(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        UserDTO userDTO = this.convertEntityToUserDTO(user);
        return UserResponse.builder()
                .userId(userDTO.getUserId())
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .role(userDTO.getRole().name())
                .build();
    }

    @Override
    public UserResponse updateUser(UpdateUser userDTO, String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        if (user.getUserId().equals(userId)) {
            user.setFullName(userDTO.getFullName());
            user.setEmail(userDTO.getEmail());
            user.setPhoneNumber(userDTO.getPhoneNumber());

        }
        User updateUser = this.userRepository.save(user);
        return UserResponse.builder()
                .userId(updateUser.getUserId())
                .fullName(updateUser.getFullName())
                .email(updateUser.getEmail())
                .phoneNumber(updateUser.getPhoneNumber())
                .build();
    }

    @Override
    public List<UserResponse> findAllUsers() {
        List<User> userList = this.userRepository.findAll();

        return userList.stream()
                .map(user -> UserResponse.builder()
                        .userId(user.getUserId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .role(user.getRole().name())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<UserResponse> searchUsers(String fullName) {
        List<User> searchs = this.userRepository.findByFullNameContainingIgnoreCase(fullName);
        return searchs.stream()
                .map(this::convertEntityToUserDTO)
                .map(userDTO -> UserResponse.builder()
                        .userId(userDTO.getUserId())
                        .fullName(userDTO.getFullName())
                        .email(userDTO.getEmail())
                        .phoneNumber(userDTO.getPhoneNumber())
                        .role(userDTO.getRole().name())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse deleteUser(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        this.userRepository.delete(user);
        return ApiResponse.builder().message("User deleted!").build();
    }

    @Override
    public UserResponse createUser(UserDTO userDTO) {
        User user = this.convertUserDTOToEntity(userDTO);
        if (this.userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists!");

        }
        user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        User userSaved = this.userRepository.save(user);
        UserDTO userDTO1 = this.convertEntityToUserDTO(userSaved);
        return UserResponse.builder()
                .userId(userDTO1.getUserId())
                .fullName(userDTO1.getFullName())
                .email(userDTO1.getEmail())
                .phoneNumber(userDTO1.getPhoneNumber())
                .role(userDTO1.getRole().name())
                .build();

    }


    private UserDTO convertEntityToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    private User convertUserDTOToEntity(UserDTO userDTO) {
        return User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }

    public UserResponse toResponse(UserDTO dto) {
        if (dto == null) return null;

        return UserResponse.builder()
                .userId(dto.getUserId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole().name())
                .build();
    }

}
