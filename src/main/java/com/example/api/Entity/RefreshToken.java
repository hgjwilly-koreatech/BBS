package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name="refresh_tokens",
        indexes=@Index(name="ix_refresh_user", columnList="user_id"))
@Getter @Setter
public class RefreshToken {
    @Id @Column(length=36) private String id;                   // UUID
    @Column(name="user_id", nullable=false, length=36) private String userId;
    @Column(nullable=false, unique=true, length=512) private String token;
    @Column(name="expires_at", nullable=false) private Instant expiresAt;
    @Column(nullable=false) private boolean revoked = false;
    @Column(name="created_at", nullable=false) private Instant createdAt = Instant.now();
}