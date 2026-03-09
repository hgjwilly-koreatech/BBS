package com.example.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public record ErrorResponse(
        @Schema(description = "에러 메시지", example = "이미 사용 중인 이메일")
        String message
) {}