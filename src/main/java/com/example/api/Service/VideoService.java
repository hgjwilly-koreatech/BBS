package com.example.api.Service;

import com.example.api.DTO.VideoDTOs;
import com.example.api.Entity.VideoObject;
import com.example.api.Repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final S3Presigner presigner;

    @Value("${app.s3.bucket}") private String bucket;
    @Value("${app.s3.prefix}") private String prefix;
    @Value("${app.s3.presignMinutes:10}") private long presignMinutes;

    public VideoDTOs.InitResponse initUpload(String userId, VideoDTOs.InitRequest req) {
        String id = UUID.randomUUID().toString();
        String ext = (req.fileExt() == null || req.fileExt().isBlank()) ? "mp4" : req.fileExt().trim();

        // ✅ 익명 업로드면 anon 폴더로
        String owner = (userId == null || userId.isBlank()) ? "anon" : userId;
        String key = "%s/%s/%s.%s".formatted(prefix, owner, id, ext);

        VideoObject v = new VideoObject();
        v.setId(id);
        v.setUserId((userId == null || userId.isBlank()) ? null : userId); // ✅ null 저장(익명)
        v.setS3Key(key);
        v.setContentType(req.contentType());
        v.setStatus("INIT");
        v.setCreatedAt(Instant.now());
        v.setUpdatedAt(Instant.now());
        videoRepository.save(v);

        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(req.contentType()) // ⚠️ PUT할 때 Content-Type 동일해야 함
                .build();

        Duration sig = Duration.ofMinutes(presignMinutes);

        PutObjectPresignRequest presignReq = PutObjectPresignRequest.builder()
                .signatureDuration(sig)
                .putObjectRequest(por)
                .build();

        String uploadUrl = presigner.presignPutObject(presignReq).url().toString();
        return new VideoDTOs.InitResponse(id, key, uploadUrl, sig.toSeconds());
    }

    public void completeUpload(String userIdOrNull, String videoId, VideoDTOs.CompleteRequest req) {
        VideoObject v = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("video not found"));

        // ✅ 소유자 규칙
        // - DB에 userId가 null(익명 업로드)이면: 익명 호출일 때만 complete 허용
        // - DB에 userId가 있으면: 그 userId로 로그인한 사용자만 complete 허용
        if (v.getUserId() == null) {
            if (userIdOrNull != null) {
                throw new IllegalArgumentException("anonymous upload: complete requires anonymous call");
            }
        } else {
            if (userIdOrNull == null || !v.getUserId().equals(userIdOrNull)) {
                throw new IllegalArgumentException("not owner");
            }
        }

        v.setStatus("UPLOADED");
        v.setSizeBytes(req.sizeBytes());
        v.setUpdatedAt(Instant.now());
        videoRepository.save(v);
    }

    public VideoDTOs.DownloadUrlResponse issueDownloadUrl(String userId, String videoId) {
        // ✅ 다운로드는 계속 “내 것만” 유지
        VideoObject v = videoRepository.findByIdAndUserId(videoId, userId)
                .orElseThrow(() -> new IllegalArgumentException("video not found"));

        if (!"UPLOADED".equals(v.getStatus())) {
            throw new IllegalStateException("video not uploaded yet");
        }

        GetObjectRequest gor = GetObjectRequest.builder()
                .bucket(bucket)
                .key(v.getS3Key())
                .responseContentType(v.getContentType())
                .build();

        Duration sig = Duration.ofMinutes(presignMinutes);

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(sig)
                .getObjectRequest(gor)
                .build();

        String downloadUrl = presigner.presignGetObject(presignReq).url().toString();
        return new VideoDTOs.DownloadUrlResponse(v.getId(), v.getS3Key(), downloadUrl, sig.toSeconds());
    }
}