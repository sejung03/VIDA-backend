package com.camorix.camorixspring.realtime.controller;

import com.camorix.camorixspring.realtime.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("realtime/stream")
@RequiredArgsConstructor
public class StreamController {
    private final SseService sseService;

    /**
     * 병실 상태 실시간 스트림 구독 처리
     * @return SseEmitter 객체 반환
     */
    @GetMapping(
            value = "/realtime",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
//    @RealtimeStreamAPISpecification
    public SseEmitter subscribeRealtimeStream(@RequestParam String tabId){
        return sseService.subscribeRealTimeStream(tabId);
    }

    /**
     * 실시간 알림 스트림 구독 처리
     * @return SseEmitter 객체 반환
     */
    @GetMapping(
            value = "/notify",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
//    @NotifyStreamAPISpecification
    public SseEmitter subscribeNotifyStream(@RequestParam String tabId){
//         실시간 알림을 주기 위한 Stream
        return sseService.subscribeNotifyStream(tabId);
    }
}