package com.camorix.camorixspring.realtime.service;

import com.camorix.camorixspring.realtime.entity.Event;
import com.camorix.camorixspring.realtime.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 실시간 이벤트 로그 데이터를 처리하는 서비스 클래스
 * - 전체 이벤트 목록 조회
 */
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventLogRepository;

    /**
     * 모든 Event 로그를 조회하여 반환
     * - Event 테이블의 전체 데이터를 가져옴
     *
     * @return List<Event> 전체 이벤트 목록
     */
    public List<Event> loadEventList(){
        return eventLogRepository.findAllByOrderByOccurredAtDesc();
    }
}
