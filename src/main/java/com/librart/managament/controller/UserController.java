package com.librart.managament.controller;

import com.librart.managament.dto.UserDTO;
import com.librart.managament.exception.ResourceAlreadyExistsException;
import com.librart.managament.model.User;
import com.librart.managament.response.ApiResponse;
import com.librart.managament.response.UpdateUser;
import com.librart.managament.response.UserResponse;
import com.librart.managament.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/findById/{userId}")
    @PreAuthorize("@securityUtils.isAdmin() or @securityUtils.isSelf(#userId)")
    public ResponseEntity<UserResponse> findById(@PathVariable String userId) {
        UserResponse singleUser = this.userService.findById(userId);
        return ResponseEntity.ok(singleUser);
    }


    @PutMapping("/updateUser/{userId}")
    @PreAuthorize("@securityUtils.isAdmin() or @securityUtils.isSelf(#userId)")
    public ResponseEntity<UserResponse> updateUser(@Valid  @RequestBody UpdateUser userDTO, @PathVariable String userId) {
        UserResponse userResponse = this.userService.updateUser(userDTO, userId);
        return ResponseEntity.ok(userResponse);
    }


    @GetMapping("/findAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> allUsers = this.userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @DeleteMapping("/deleteUser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) {
        ApiResponse apiResponse = this.userService.deleteUser(userId);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserResponse user = this.userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/searchUsersByName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String fullName) {
        List<UserResponse> userResponses = this.userService.searchUsers(fullName);
        return ResponseEntity.ok(userResponses);
    }
}
