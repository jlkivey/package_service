package com.clevelanddx.packageintake.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication request containing username and password")
public class AuthenticationRequest {
    @Schema(description = "Username in first.last format", example = "jeff.ivey", required = true)
    private String username;

    @Schema(description = "User's password", example = "password123", required = true)
    private String password;
} 