package com.example.api.controller;

import com.example.api.DTO.VideoDTOs;
import com.example.api.JWT.JwtAuthFilter;
import com.example.api.JWT.JwtProvider;
import com.example.api.Security.SecurityConfig;
import com.example.api.Service.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VideoController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class})
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("POST /api/videos/init succeeds with valid bearer token")
    void init_withValidToken_returnsPresignedInfo() throws Exception {
        when(jwtProvider.validateAndGetSubject("good-token")).thenReturn("user-123");

        var response = new VideoDTOs.InitResponse(
                "video-1",
                "videos/user-123/video-1.mp4",
                "https://example.com/upload",
                600L
        );
        when(videoService.initUpload(eq("user-123"), any(VideoDTOs.InitRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/videos/init")
                                .header("Authorization", "Bearer good-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"contentType":"video/mp4","fileExt":"mp4"}
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoId").value("video-1"))
                .andExpect(jsonPath("$.objectKey").value("videos/user-123/video-1.mp4"))
                .andExpect(jsonPath("$.uploadUrl").value("https://example.com/upload"))
                .andExpect(jsonPath("$.expiresInSeconds").value(600));

        Mockito.verify(videoService).initUpload(eq("user-123"), any(VideoDTOs.InitRequest.class));
    }

    @Test
    @DisplayName("POST /api/videos/init requires authentication")
    void init_withoutToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(
                        post("/api/videos/init")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"contentType":"video/mp4"}
                                        """)
                )
                .andExpect(status().isUnauthorized());

        Mockito.verifyNoInteractions(videoService);
    }
}
