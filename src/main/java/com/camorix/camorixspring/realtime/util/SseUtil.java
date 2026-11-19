package com.camorix.camorixspring.realtime.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * SSE 통신에서 사용할 Jackson ObjectMapper 유틸 클래스
 * - 자바 객체를 JSON 문자열로 변환할 때 사용
 * - 공통 ObjectMapper 인스턴스를 제공하여 일관된 직렬화 설정 유지
 */
@Component
public class SseUtil {
    // ObjectMapper 인스턴스를 클래스 내부에 생성 (재사용 가능)
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ObjectMapper 인스턴스 반환
     * - 서비스 클래스에서 JSON 직렬화 시 사용
     *
     * @return 공통 ObjectMapper
     */
    public ObjectMapper getObjectMapper(){
        return this.objectMapper;
    }
}

