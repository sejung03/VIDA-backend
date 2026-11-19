package com.camorix.camorixspring.auth.util;

import com.camorix.camorixspring.auth.dto.oauth2.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHolderUtil {
    private CustomOAuth2User getCurrentOAuth2User(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User user)) {
            return null;
        }
        return user;
    }

    public String getCurrentUserEmail(){
        CustomOAuth2User user = getCurrentOAuth2User();
        return user == null ? "demo@camorix.local" : user.getEmail();
    }

    public String getCurrentUserName(){
        CustomOAuth2User user = getCurrentOAuth2User();
        return user == null ? "데모 사용자" : user.getName();
    }

    public String getCurrentUserRole(){
        CustomOAuth2User user = getCurrentOAuth2User();
        return user == null ? "ROLE_DEMO" : user.getAuthorities().iterator().next().toString();
    }
}
