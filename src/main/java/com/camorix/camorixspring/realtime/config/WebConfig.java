package com.camorix.camorixspring.realtime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    // CORS 설정을 위한 WebMvcConfigurer 빈 등록
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 모든 경로에 대해 CORS 허용 설정
                registry.addMapping("/**")
                        // React 개발 서버 주소 허용
                        .allowedOrigins("http://localhost:3000")
                        // 허용할 HTTP 메서드 지정
                        .allowedMethods("GET","POST");
            }
        };
    }
}
