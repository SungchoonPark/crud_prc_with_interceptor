package org.choon.crudprc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.choon.crudprc.interceptor.AuthenticationInterceptor;
import org.choon.crudprc.util.JwtProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    public WebConfig(JwtProvider jwtProvider, ObjectMapper objectMapper) {
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtProvider, objectMapper))
                .addPathPatterns("/api/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
