package com.example.MiniEvent.config.security;

import com.example.MiniEvent.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AppProperties appProperties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(appProperties.getApiPrefix(), c ->
                c.isAnnotationPresent(RestController.class) ||
                        c.getPackageName().startsWith("com.example.MiniEvent.controller")
        );
    }
}
