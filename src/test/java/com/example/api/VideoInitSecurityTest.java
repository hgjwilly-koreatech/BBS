package com.example.api;

import com.example.api.Service.VideoService;
import com.example.api.JWT.JwtProvider;
import com.example.api.controller.VideoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoController.class)
class VideoInitSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideoService videoService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void initEndpointWithAuthShouldNotReturn403() throws Exception {
        mockMvc.perform(post("/api/videos/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contentType\":\"video/mp4\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void initEndpointWithMockUserShouldNotReturn403() throws Exception {
        mockMvc.perform(post("/api/videos/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contentType\":\"video/mp4\"}"))
                .andExpect(status().isOk());
    }
}
