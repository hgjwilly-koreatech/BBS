package com.example.api.DTO;

import jakarta.validation.constraints.*;
public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {}