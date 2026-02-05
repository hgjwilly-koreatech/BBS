package com.example.api.Service;

import com.example.api.exceptionhandle.exceptionhandle;
import com.example.api.DTO.*;
import org.springframework.beans.factory.annotation.Value;

import com.example.api.Entity.RefreshToken;
import com.example.api.Entity.User;
import com.example.api.JWT.JwtProvider;
import com.example.api.Repository.RefreshTokenRepository;
import com.example.api.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

// AuthService.java
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.refresh-days:14}")
    private long refreshDays;

    private final UserRepository userRepo;
    private final RefreshTokenRepository rtRepo;
    private final PasswordEncoder encoder;
    private final JwtProvider jwt;



    @Transactional
    public CreateResponse signUp(SignUpRequest req){
        if (!req.password().equals(req.passwordConfirm())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }
        String email = req.email().trim().toLowerCase();
        userRepo.findByEmail(email).ifPresent(u -> { throw new BadCredentialsException("이미 사용 중인 이메일"); });

        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(req.password()));
        u.setName(req.name());
        userRepo.save(u);

        return new CreateResponse(u.getId());
    }

    @Transactional
    public AuthResponse login(LoginRequest req){
        String email = req.email().trim().toLowerCase();

        User u = userRepo.findByEmail(req.email().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));
        if (!encoder.matches(req.password(), u.getPasswordHash())){
            throw new BadCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
}
        u.setLastLoginAt(Instant.now());

        String access  = jwt.issueAccess(u.getId());
        String refresh = jwt.issueRefresh(u.getId());

        RefreshToken rt = new RefreshToken();
        rt.setId(UUID.randomUUID().toString());
        rt.setUserId(u.getId());
        rt.setToken(refresh);
        rt.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshDays)));
        rtRepo.save(rt);

        return new AuthResponse(u.getId(), access, refresh);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken){
        RefreshToken rt = rtRepo.findByTokenAndRevokedFalse(refreshToken)
                .filter(r -> r.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰 무효"));

        String newAccess  = jwt.issueAccess(rt.getUserId());
        String newRefresh = jwt.issueRefresh(rt.getUserId());

        rt.setRevoked(true);
        RefreshToken next = new RefreshToken();
        next.setId(UUID.randomUUID().toString());
        next.setUserId(rt.getUserId());
        next.setToken(newRefresh);
        next.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshDays)));
        rtRepo.save(next);

        return new AuthResponse(rt.getUserId(), newAccess, newRefresh);
    }

    @Transactional
    public void logout(String refreshToken){
        rtRepo.findByTokenAndRevokedFalse(refreshToken)
                .ifPresent(rt -> rt.setRevoked(true));
    }
}
