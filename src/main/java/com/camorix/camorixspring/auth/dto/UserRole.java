package com.camorix.camorixspring.auth.dto;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    USER,
    NURSE,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
