package com.example.MiniEvent.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
@Slf4j
public class TestFirebaseConfig {

    @Value("${firebase.auth.emulator.host}")
    private String authEmulatorHost;

    @Value("${firebase.firestore.emulator.host}")
    private String firestoreEmulatorHost;

    @Bean
    public FirebaseApp firebaseApp() {

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(new FirebaseCredentialsEmulator())
                .setProjectId("minievent") // projectId giả định
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp app = FirebaseApp.initializeApp(options);
            System.out.println("Initialized FirebaseApp with emulator config");
            return app;
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }


}