package com.example.MiniEvent.config.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${google.api.url}")
    private String url;

    @Bean
    public WebClient firebaseWebClient() {
        return WebClient.builder()
                .baseUrl(url)
                .build();
    }
}
