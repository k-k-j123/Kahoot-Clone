package com.kaushik.auth_service.dto;

import com.kaushik.auth_service.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String email;
    private Role role;
}
