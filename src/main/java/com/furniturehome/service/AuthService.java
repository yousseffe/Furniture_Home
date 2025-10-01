package com.furniturehome.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.furniturehome.model.User;
import com.furniturehome.repository.UserRepository;
import com.furniturehome.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String signup(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) 
            throw new RuntimeException("Email already exists");
        
        if (userRepository.findByPhone(user.getPhone()).isPresent()) 
            throw new RuntimeException("Phone already exists");
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() == null ? "CUSTOMER" : user.getRole());
        userRepository.save(user);
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
}

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
            return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
