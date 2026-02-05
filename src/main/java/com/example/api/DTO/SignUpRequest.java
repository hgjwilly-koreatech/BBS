package com.example.api.DTO;

import jakarta.validation.constraints.*;
public record SignUpRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min=8,max=60) String password,
        @NotBlank String passwordConfirm,
        @NotBlank String name
) {}