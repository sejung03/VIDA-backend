package com.camorix.camorixspring.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthResult {
//    문자열이 JWT 형식이 아니거나 Base64 decode 실패
    MALFORMED_TOKEN(4001),
//    token의 signature가 유효하지 않음
    INVALID_SIGNATURE(4002),
//    token의 유효 기간 만료
    EXPIRED_TOKEN(4003),
//    token이 지원되지 않은 형식이나 알고리즘 사용
    UNSUPPORTED_TOKEN(4004),
//    token 문자열이 null 이거나 빈 문자열
    TOKEN_NULL(4005);

    private int errorCode;
}
