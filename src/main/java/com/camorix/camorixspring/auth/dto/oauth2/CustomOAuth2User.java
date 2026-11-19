package com.camorix.camorixspring.auth.dto.oauth2;

import com.camorix.camorixspring.auth.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

//    사용안함
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    public String getEmail(){
        return userDTO.getEmail();
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(userDTO.getRole());
        return authorities;
    }
}
