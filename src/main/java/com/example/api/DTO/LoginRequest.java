package com.example.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "이메일", example = "test@example.com")
        @Email @NotBlank
        String email,

        @Schema(description = "비밀번호", example = "password1234")
        @NotBlank
        String password
) {}