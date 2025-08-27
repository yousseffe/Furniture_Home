package com.furniturehome.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furniturehome.dto.AuthResponse;
import com.furniturehome.dto.UserDTO;
import com.furniturehome.model.User;
import com.furniturehome.service.AuthService;
import com.furniturehome.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody User user) {
        User savedUser = userService.createUser(user); 
        String token = authService.signup(savedUser);

        return ResponseEntity.ok(
            new AuthResponse(
                "Welcome " + savedUser.getName() + "!",
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
            )
        );
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam String email, @RequestParam String password) {
        String token = authService.login(email, password);

        User loggedUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
            new AuthResponse(
                "Login successful",
                token,
                loggedUser.getId(),
                loggedUser.getEmail(),
                loggedUser.getRole()
            )
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());
        User updatedUser = userService.createUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/reset-password/{id}")
    public ResponseEntity<String> resetPassword(@PathVariable UUID id, @RequestParam String newPassword) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(newPassword); 
        userService.createUser(user);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful (invalidate token client-side)");
    }
}
