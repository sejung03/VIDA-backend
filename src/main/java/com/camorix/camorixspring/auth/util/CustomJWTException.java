package com.camorix.camorixspring.auth.util;

import com.camorix.camorixspring.auth.dto.AuthResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomJWTException extends Exception{
     private AuthResult authResult;
     private String message;
}
