package com.example.api.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record SignUpRequest(
        @Schema(description = "이메일", example = "test@example.com")
        @Email @NotBlank
        String email,

        @Schema(description = "비밀번호", example = "password1234")
        @NotBlank @Size(min = 8, max = 64)
        String password,

        @Schema(description = "비밀번호 확인", example = "password1234")
        @NotBlank
        String passwordConfirm,

        @Schema(description = "이름", example = "홍길동")
        @NotBlank
        String name
) {}