package com.example.api.DTO;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 응답")
public record AuthResponse(
        @Schema(description = "유저 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId,

        @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiJ9.access.token")
        String accessToken,

        @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9.refresh.token")
        String refreshToken
) {}