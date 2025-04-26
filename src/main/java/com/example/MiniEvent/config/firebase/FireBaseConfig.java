package com.example.MiniEvent.config.firebase;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Profile("prod")
public class FireBaseConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        InputStream serviceAccount;
        String firebaseConfigJson = System.getenv("FIREBASE_CONFIG_JSON");

        if (firebaseConfigJson != null) {
            serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));
        } else {
            ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
            if (!resource.exists()) {
                throw new IllegalStateException("Firebase service account file not found in resources and no environment variable provided");
            }
            serviceAccount = resource.getInputStream();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    @Bean
    public FirebaseAuth firebaseAuth() { return FirebaseAuth.getInstance(); }
}
