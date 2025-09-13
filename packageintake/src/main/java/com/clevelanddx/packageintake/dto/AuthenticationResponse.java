package com.clevelanddx.packageintake.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response containing success status and message")
public class AuthenticationResponse {
    @Schema(description = "Whether authentication was successful", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Authentication successful")
    private String message;

    @Schema(description = "Username of the authenticated user", example = "jeff.ivey")
    private String username;
} 