package com.camorix.camorixspring.auth.util;

import com.camorix.camorixspring.auth.dto.AuthResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //    jwt token 생성
    private String createToken(String email, String name, String role, long expireMs) {
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .claim("name", name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(this.secretKey)
                .compact();
    }

    //    access token 생성
    public String createAccessToken(String email, String name, String role) {
        return this.createToken(email, name, role, 1000L * 60 * 60);
    }

    //    refresh token 생성
    public String createRefreshToken(String email, String name, String role) {
        return this.createToken(email, name, role, 1000L * 60 * 60 * 24 * 7);
    }

    //    token 에서 payload 추출
    private Claims getPayload(String token) throws CustomJWTException{
        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomJWTException(AuthResult.TOKEN_NULL, e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new CustomJWTException(AuthResult.EXPIRED_TOKEN, e.getMessage());
        } catch (SignatureException e) {
            throw new CustomJWTException(AuthResult.INVALID_SIGNATURE, e.getMessage());
        } catch (MalformedJwtException e){
            throw new CustomJWTException(AuthResult.MALFORMED_TOKEN, e.getMessage());
        } catch (UnsupportedJwtException e){
            throw new CustomJWTException(AuthResult.UNSUPPORTED_TOKEN, e.getMessage());
        }
        return claims;
    }

//    token 에서 email 추출
    public String getEmail(String token) throws CustomJWTException{
        return this.getPayload(token).get("email").toString();
    }

//    token 에서 name 추출
    public String getName(String token) throws CustomJWTException{
        return this.getPayload(token).get("name").toString();
    }

//    token 에서 role 추출
    public String getRole(String token) throws CustomJWTException{
        return this.getPayload(token).get("role").toString();
    }
}
