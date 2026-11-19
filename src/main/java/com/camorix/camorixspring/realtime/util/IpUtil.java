package com.camorix.camorixspring.realtime.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 클라이언트의 실제 IP 주소를 추출하기 위한 유틸 클래스
 * - 프록시, 로드밸런서 등을 거친 환경에서도 정확한 IP 확인 가능
 * - 컨트롤러 외의 서비스/기타 영역에서도 사용 가능
 */
@Component
public class IpUtil {

    /**
     * HttpServletRequest 기반으로 클라이언트 IP 추출
     * - 여러 프록시 헤더를 우선적으로 확인한 뒤,
     * - 유효한 값이 없으면 request.getRemoteAddr() 반환
     *
     * @param request 현재 요청 객체
     * @return 클라이언트의 IP 주소
     */
    public String getClientIp(HttpServletRequest request) {
        // 일반적인 프록시 환경에서 사용되는 헤더들
        String[] headerNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            // 유효한 IP가 있고, unknown이 아니면 사용
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 여러 IP가 있을 경우 첫 번째 IP 반환
                return ip.split(",")[0].trim();
            }
        }
        // 위의 헤더에서 유효한 값을 찾지 못했을 경우 기본값 사용
        return request.getRemoteAddr();
    }

    /**
     * 컨트롤러 외부(Service 등)에서도 클라이언트 IP를 가져올 수 있는 메서드
     * - RequestContextHolder를 통해 현재 HttpServletRequest에 접근
     * - 내부적으로 getClientIp(request) 재사용
     *
     * @return 클라이언트의 IP 주소
     */
    public String getClientIp() {
        // 현재 정보를 불러오는 기능
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // 현재 요청 객체 추출
        HttpServletRequest request = attributes.getRequest();
        return getClientIp(request); // 위의 메서드 재사용
    }
}
