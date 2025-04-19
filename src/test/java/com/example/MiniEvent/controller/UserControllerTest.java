package com.example.MiniEvent.controller;

import com.example.MiniEvent.web.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// REMEMBER SET ENVIRONMENT AND CHECK BEFORE RUNNING TEST

@SpringBootTest // Load full context (tức là dùng luôn service thật)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.version=v1",
        "app.distance.threshold=0.1",
        "firebase.auth.emulator.host=127.0.0.1:9099", // Host của Firebase Auth Emulator
        "firebase.firestore.emulator.host=127.0.0.1:8081" // Host của Firestore Emulator
})
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FirebaseAuth firebaseAuth;

    @BeforeEach
    void cleanUpUser() {
        try {
            UserRecord user = firebaseAuth.getUserByEmail("test@gmail.com");
            firebaseAuth.deleteUser(user.getUid());
            System.out.println("✅ Deleted existing test user.");
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                System.out.println("ℹ️ Test user not found, skip delete.");
            } else {
                throw new RuntimeException("❌ Failed to clean up test user: " + e.getMessage(), e);
            }
        }
    }
    @Test
    void register_success_withoutImage() throws Exception {
        RegisterRequest request = new RegisterRequest("test", "test@gmail.com", "123456", "0123456789");
        String json = objectMapper.writeValueAsString(request);

        MockMultipartFile infoPart = new MockMultipartFile(
                "info",
                "info.json",
                "application/json",
                json.getBytes()
        );

        mockMvc.perform(multipart("/v1/users/register")
                        .file(infoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

}
