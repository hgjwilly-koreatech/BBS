package com.example.api.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name="users", indexes=@Index(name="ux_users_email", columnList="email", unique=true))
@Getter @Setter
public class User {
    @Id @Column(length=36) private String id;                 // UUID
    @Column(nullable=false, unique=true, length=255) private String email;
    @Column(nullable=false, length=60) private String name;
    @Column(name="password_hash", nullable=false, length=60) private String passwordHash; // BCrypt
    @Column(name="created_at", nullable=false) private Instant createdAt = Instant.now();
    @Column(name="updated_at", nullable=false) private Instant updatedAt = Instant.now();
    @Column(name="last_login_at") private Instant lastLoginAt;

    @PreUpdate void onUpdate(){ updatedAt = Instant.now(); }
}