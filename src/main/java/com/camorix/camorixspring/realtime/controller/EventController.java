package com.camorix.camorixspring.realtime.controller;

import com.camorix.camorixspring.realtime.entity.Event;
import com.camorix.camorixspring.realtime.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 이벤트 관련 요청 처리 컨트롤러 클래스
 */
@RestController
@RequestMapping("realtime/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    /**
     * 전체 이벤트 목록 조회 처리
     * @return 이벤트 객체 리스트 반환
     */
    @GetMapping()
    public List<Event> loadEventProc(){
        return eventService.loadEventList();  // 서비스에서 이벤트 리스트 조회
    }
}