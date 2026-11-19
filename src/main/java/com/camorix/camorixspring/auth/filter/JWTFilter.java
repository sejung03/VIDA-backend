package com.camorix.camorixspring.auth.filter;

import com.camorix.camorixspring.auth.dto.AuthResponse;
import com.camorix.camorixspring.auth.dto.UserDTO;
import com.camorix.camorixspring.auth.dto.UserRole;
import com.camorix.camorixspring.auth.dto.oauth2.CustomOAuth2User;
import com.camorix.camorixspring.auth.util.CustomJWTException;
import com.camorix.camorixspring.auth.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/login", "/oauth2"
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String accessToken = null;
//        String refreshToken = null;
//
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies == null || cookies.length == 0) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        for (Cookie c : cookies) {
//            if (c.getName().equals("access")) {
//                accessToken = c.getValue();
//            } else if (c.getName().equals("refresh")){
//                refreshToken = c.getValue();
//            }
//        }
//
//        if (accessToken == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            System.out.println("access token null");
//            return;
//        }
//
//        String email = null;
//        String name = null;
//        String role = null;
//        try {
//            email = jwtUtil.getEmail(accessToken);
//            name = jwtUtil.getName(accessToken);
//            role = jwtUtil.getRole(accessToken);
//        } catch (CustomJWTException e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            System.out.println("token error");
//            return;
//        }
//
//        UserDTO userDTO = UserDTO.builder()
//                .email(email)
//                .name(name)
//                .role(UserRole.valueOf(role))
//                .build();
//
//        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        filterChain.doFilter(request, response);

        String accessToken = request.getHeader("access");

        String email = null;
        String name = null;
        String role = null;

        try {
            email = jwtUtil.getEmail(accessToken);
            name = jwtUtil.getName(accessToken);
            role = jwtUtil.getRole(accessToken);
        } catch (CustomJWTException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(
                    new AuthResponse(e.getAuthResult(), email)
            ));
            return;
        }

        System.out.println("email : " + email);
        System.out.println("name : " + name);
        System.out.println("role : " + role);

        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .name(name)
                .role(UserRole.valueOf(role))
                .build();

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
