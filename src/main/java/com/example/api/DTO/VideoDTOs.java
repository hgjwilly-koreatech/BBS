package com.example.api.DTO;

import jakarta.validation.constraints.NotBlank;

public class VideoDTOs {

    public record InitRequest(
            @NotBlank String contentType,   // "video/mp4"
            String fileExt                  // "mp4" (옵션)
    ) {}

    public record InitResponse(
            String videoId,
            String objectKey,
            String uploadUrl,
            long expiresInSeconds
    ) {}

    public record CompleteRequest(
            Long sizeBytes
    ) {}

    public record DownloadUrlResponse(
            String videoId,
            String objectKey,
            String downloadUrl,
            long expiresInSeconds
    ) {}
}
