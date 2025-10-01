package com.furniturehome.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDTO {
    private UUID user_id;
    private String name;
    private String email;
    private String phone;
    private String role;
}