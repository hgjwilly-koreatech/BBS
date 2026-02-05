package com.example.api.Repository;

import com.example.api.Entity.VideoObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<VideoObject, String> {
    List<VideoObject> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<VideoObject> findByIdAndUserId(String id, String userId);
}
