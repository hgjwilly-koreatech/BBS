package com.example.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "리프레시 토큰 요청")
public record RefreshTokenRequest(
        @Schema(description = "refresh token", example = "eyJhbGciOiJIUzI1NiJ9.refresh.token")
        @NotBlank
        String refreshToken
) {}