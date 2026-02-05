package com.example.api.DTO;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(@NotBlank String refreshToken
) {
}
