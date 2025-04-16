package com.example.MiniEvent.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.util.Date;

public class FirebaseCredentialsEmulator extends GoogleCredentials {
    @Override
    public AccessToken refreshAccessToken() {
        return new AccessToken("fake-token", new Date(System.currentTimeMillis() + 3600_000));
    }
}
