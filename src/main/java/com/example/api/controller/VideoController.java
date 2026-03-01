package com.example.api.controller;

import com.example.api.DTO.VideoDTOs;
import com.example.api.Service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    // ✅ 익명이면 null
    private String userIdOrNull() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();
        if (principal == null) return null;

        String s = principal.toString();
        if ("anonymousUser".equals(s)) return null;

        return s;
    }

    // Jetson: presigned PUT 발급 (✅ 익명 가능)
    @PostMapping("/init")
    public ResponseEntity<VideoDTOs.InitResponse> init(@Valid @RequestBody VideoDTOs.InitRequest req) {
        return ResponseEntity.ok(videoService.initUpload(userIdOrNull(), req));
    }

    // Jetson: 업로드 완료 처리 (✅ 익명 가능)
    @PostMapping("/{videoId}/complete")
    public ResponseEntity<Void> complete(@PathVariable String videoId,
                                         @RequestBody(required = false) VideoDTOs.CompleteRequest req) {
        videoService.completeUpload(userIdOrNull(), videoId, req == null ? new VideoDTOs.CompleteRequest(null) : req);
        return ResponseEntity.ok().build();
    }

    // App: presigned GET 발급 (✅ 이건 로그인 필요로 유지)
    @GetMapping("/{videoId}/download-url")
    public ResponseEntity<VideoDTOs.DownloadUrlResponse> downloadUrl(@PathVariable String videoId) {
        String userId = userIdOrNull();
        if (userId == null) {
            // 로그인 안 된 다운로드는 막는 게 안전
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(videoService.issueDownloadUrl(userId, videoId));
    }
}