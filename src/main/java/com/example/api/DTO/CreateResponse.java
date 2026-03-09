package com.example.api.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "생성 응답")
public record CreateResponse(
        @Schema(description = "생성된 유저 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String userId
) {}