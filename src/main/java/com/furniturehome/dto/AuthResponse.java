package com.furniturehome.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private UUID userId;
    private String email;
    private String role;
}