package com.example.MiniEvent.controller;

import com.example.MiniEvent.DTO.request.RegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 👈 Load full context (tức là dùng luôn service thật)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.version=v1",
        "app.distance.threshold=0.1",
        "firebase.auth.emulator.host=127.0.0.1:9099", // Host của Firebase Auth Emulator
        "firebase.firestore.emulator.host=127.0.0.1:8081" // Host của Firestore Emulator// 👈 Nếu bạn cần placeholder như vậy trong các bean
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
            System.out.println("Deleted existing test user.");
        } catch (FirebaseAuthException e) {
            // User doesn't exist -> ignore
            System.out.println("Test user not found, skip delete.");
        }
    }

    @Test
    void testRegister_thenDelete() throws Exception {
        RegisterDTO request = new RegisterDTO("test","test@gmail.com", "123456");

        mockMvc.perform(post("/v1/users/register") // 👈 nhớ đúng prefix
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
