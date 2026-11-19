package com.camorix.camorixspring.realtime.service;

import com.camorix.camorixspring.auth.util.SecurityContextHolderUtil;
import com.camorix.camorixspring.realtime.dto.EventDTO;
import com.camorix.camorixspring.realtime.dto.FrameDTO;
import com.camorix.camorixspring.realtime.entity.Event;
import com.camorix.camorixspring.realtime.repository.EventRepository;
import com.camorix.camorixspring.realtime.util.IpUtil;
import com.camorix.camorixspring.realtime.util.SseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 실시간 시각화 스트림에 대한 SSE 서비스 클래스
 * - emitter 생성 및 5분 타임아웃 설정
 * - 클라이언트 식별용 UUID 생성 및 저장
 * - 연결 종료, 에러 발생 시 emitter 자동 제거
 */
@Service
@RequiredArgsConstructor
public class SseService {

    private final Map<String, SseEmitter> realTimeEmitterMap = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> notifyEmitterMap = new ConcurrentHashMap<>();
    private final EventRepository eventLogRepository;
    private final IpUtil ipUtil;
    private final SseUtil sseUtil;
    private final SecurityContextHolderUtil securityContextHolderUtil;

    private String makeClientKey(String tabId) {
        String email = securityContextHolderUtil.getCurrentUserEmail();
        return email + "-" + tabId;
    }

    /**
     * 실시간 시각화 스트림에 대한 SSE 구독 메서드
     * - emitter 생성 및 5분 타임아웃 설정
     * - 클라이언트 식별용 UUID 생성 및 저장
     * - 연결 종료, 에러 발생 시 emitter 자동 제거
     */
    public SseEmitter subscribeRealTimeStream(String tabId) {
        String clientKey = makeClientKey(tabId);
        SseEmitter emitter = new SseEmitter(1000L * 60 * 5); // 5분

        realTimeEmitterMap.put(clientKey, emitter);

        //연결 종료, 오류, 타임아웃 시 emitter 제거
        emitter.onCompletion(() -> realTimeEmitterMap.remove(clientKey));
        emitter.onTimeout(() -> realTimeEmitterMap.remove(clientKey));
        emitter.onError((e) -> realTimeEmitterMap.remove(clientKey));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect to realtime stream"));
//                    .data(clientKey));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * 낙상 알림 스트림에 대한 SSE 구독 메서드
     * - emitter 생성 및 저장
     * - clientKey 기반으로 식별
     * - 연결 종료, 에러 발생 시 emitter 제거
     */
    public SseEmitter subscribeNotifyStream(String tabId) {
        String clientKey = makeClientKey(tabId);
        SseEmitter emitter = new SseEmitter(1000L * 60 * 5); // 5분

        notifyEmitterMap.put(clientKey, emitter);

        // 연결 종료, 타임아웃, 에러 시 emitter 제거
        emitter.onCompletion(() -> notifyEmitterMap.remove(clientKey));
        emitter.onTimeout(() -> notifyEmitterMap.remove(clientKey));
        emitter.onError((e) -> notifyEmitterMap.remove(clientKey));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect to notify stream"));
//                    .data("key : " + clientKey));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * 실시간 시각화 데이터를 구독자들에게 전송
     * - 실패한 emitter는 제거
     */
    public void broadcastRealTimeFrame(FrameDTO frameDTO) {
        List<String> removeList = new ArrayList<>();

        realTimeEmitterMap.forEach((clientKey, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("frame")
                        .data(sseUtil.getObjectMapper().writeValueAsString(frameDTO)));
            } catch (Exception e) {
                removeList.add(clientKey);
                emitter.completeWithError(e);
            }
        });

        removeList.forEach(realTimeEmitterMap::remove);
    }

    /**
     * 낙상 알림 데이터를 구독자들에게 전송하고, DB에 로그 저장
     * - 실패한 emitter는 제거
     * - 전송 성공 여부 및 발생 시각 기록
     */
    public void broadcastNotification(EventDTO eventDTO){
        List<String> removeList = new ArrayList<>();
        boolean[] delivered = {false};

        notifyEmitterMap.forEach((clientKey, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(eventDTO));
                delivered[0] = true;
            } catch (IOException e) {
                removeList.add(clientKey);
                emitter.completeWithError(e);
            }
        });

        removeList.forEach(notifyEmitterMap::remove);

        // 알림 한 건당 로그도 한 건만 저장한다. 구독자가 없어도 감지 이력은 보존한다.
        eventLogRepository.save(Event.builder()
                .roomNumber(eventDTO.getRoomNumber())
                .severity(eventDTO.getSeverity())
                .clientIp(ipUtil.getClientIp())
                .success(delivered[0])
                .occurredAt(LocalDateTime.now())
                .build()
        );
    }

}
