package com.example.MiniEvent.config.webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${google.api.url}")
    private String googleApiUrl;

    @Value("${dicebear.api.url}")
    private String dicebearApiUrl;

    @Bean
    @Qualifier("firebaseWebClient")
    public WebClient firebaseWebClient() {
        return WebClient.builder()
                .baseUrl(googleApiUrl)
                .build();
    }

    @Bean
    @Qualifier("dicebearApiUrl")
    public WebClient dicebearWebClient() {
        return WebClient.builder()
                .baseUrl(dicebearApiUrl)
                .build();
    }
}
