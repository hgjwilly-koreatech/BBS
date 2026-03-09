package com.example.api.controller;

import com.example.api.DTO.VideoDTOs;
import com.example.api.Service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    private String userId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BadCredentialsException("Authentication required");
        }
        return String.valueOf(auth.getPrincipal());
    }

    // Jetson: presigned PUT 발급
    @PostMapping("/init")
    public ResponseEntity<VideoDTOs.InitResponse> init(@Valid @RequestBody VideoDTOs.InitRequest req) {
        return ResponseEntity.ok(videoService.initUpload(userId(), req));
    }

    // Jetson: 업로드 완료 처리
    @PostMapping("/{videoId}/complete")
    public ResponseEntity<Void> complete(@PathVariable String videoId,
                                         @RequestBody(required = false) VideoDTOs.CompleteRequest req) {
        videoService.completeUpload(userId(), videoId, req == null ? new VideoDTOs.CompleteRequest(null) : req);
        return ResponseEntity.ok().build();
    }

    // App: presigned GET 발급
    @GetMapping("/{videoId}/download-url")
    public ResponseEntity<VideoDTOs.DownloadUrlResponse> downloadUrl(@PathVariable String videoId) {
        return ResponseEntity.ok(videoService.issueDownloadUrl(userId(), videoId));
    }
}
