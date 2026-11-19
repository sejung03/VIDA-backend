package com.camorix.camorixspring.auth.config;

import com.camorix.camorixspring.auth.filter.JWTFilter;
import com.camorix.camorixspring.auth.handler.CustomOAuth2SuccessHandler;
import com.camorix.camorixspring.auth.service.CustomOAuth2UserService;
import com.camorix.camorixspring.auth.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("!demo")
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JWTUtil jwtUtil;

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        disable csrf
        http
                .csrf(AbstractHttpConfigurer::disable);

//        disable form login
        http
                .formLogin(AbstractHttpConfigurer::disable);

//        disable http basic
        http
                .httpBasic(AbstractHttpConfigurer::disable);

//        stateless session
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

//        JWT filter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil, objectMapper()), UsernamePasswordAuthenticationFilter.class);

//        OAuth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2UserService)))
                        .successHandler(customOAuth2SuccessHandler)
                );

//        경로별 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/swagger-ui/**", "/loginpage.html", "/frame","/login").permitAll()
                        .anyRequest().authenticated()
                );

        http
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(Collections.singletonList("Set-Cooklie"));
                                configuration.setExposedHeaders(Collections.singletonList("access"));
                                configuration.setExposedHeaders(Collections.singletonList("refresh"));

                                return configuration;
                            }
                        }));

        return http.build();
    }
}
