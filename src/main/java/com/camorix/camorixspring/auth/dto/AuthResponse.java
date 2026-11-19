package com.camorix.camorixspring.auth.dto;

import lombok.*;

@Getter
@Setter
public class AuthResponse {
    private int result;
    private String email;
    private String message;

    public AuthResponse(AuthResult jwtResult, String email){
        this.result = jwtResult.getErrorCode();
        this.email = email;
        this.message = jwtResult.name();
    }
}
