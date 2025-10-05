package com.librart.managament.controller;

import com.librart.managament.dto.UserDTO;
import com.librart.managament.response.AuthRequest;
import com.librart.managament.response.JwtResponse;
import com.librart.managament.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody @Valid UserDTO userDTO) {
        JwtResponse register = this.userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        JwtResponse login = this.userService.login(authRequest);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam String token) {

         try {
             JwtResponse jwtResponse = this.userService.refreshToken(token);
             return ResponseEntity.ok(jwtResponse);
         }catch (RuntimeException e){
             return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }

    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValidToken = this.userService.validateToken(token);
        return ResponseEntity.ok(isValidToken);
    }
}
