package com.example.api.DTO;

public record AuthResponse(String userId, String accessToken, String refreshToken) {}
