package com.camorix.camorixspring.auth.handler;

import com.camorix.camorixspring.auth.dto.oauth2.CustomOAuth2User;
import com.camorix.camorixspring.auth.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = customOAuth2User.getEmail();
        String name = customOAuth2User.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        response.addCookie(createCookie("access", jwtUtil.createAccessToken(email, name, role)));
        response.addCookie(createCookie("refresh", jwtUtil.createRefreshToken(email, name, role)));
        response.sendRedirect("http://localhost:8080/main/nurse");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
