package com.camorix.camorixspring.realtime.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 실시간 낙상(fall) 및 위험(danger) 카운트를 관리하는 서비스 클래스
 * - 병실별 이전 카운트를 저장하고, 현재 카운트와 비교하여 증가 여부 판단
 */
@Service
public class SeverityCountService {
    // 병실별 낙상 카운트 저장용 맵
    private final ConcurrentHashMap<String, Integer> fallCountMap = new ConcurrentHashMap<>();
    // 병실별 위험 카운트 저장용 맵
    private final ConcurrentHashMap<String, Integer> dangerCountMap = new ConcurrentHashMap<>();

    /**
     * 현재 병실의 낙상/위험 카운트를 저장 (이전 값으로 기록)
     *
     * @param roomNumber 병실 번호
     * @param fallCount 현재 낙상 카운트
     * @param dangerCount 현재 위험 카운트
     */
    public void storePrevCounts(String roomNumber, int fallCount, int dangerCount){
        fallCountMap.put(roomNumber, fallCount);
        dangerCountMap.put(roomNumber, dangerCount);
    }

    /**
     * 현재 낙상/위험 카운트가 이전보다 증가했는지 여부 확인
     * - 확인 후, 현재 값을 저장하여 다음 비교에 사용
     *
     * @param roomNumber 병실 번호
     * @param fallCount 현재 낙상 카운트
     * @param dangerCount 현재 위험 카운트
     * @return true: 증가했을 경우, false: 변화 없을 경우
     */
    public boolean countIncreased(String roomNumber, int fallCount, int dangerCount){
        int prevFallCount = fallCountMap.getOrDefault(roomNumber, 0);
        int prevDangerCount = dangerCountMap.getOrDefault(roomNumber, 0);

        storePrevCounts(roomNumber, fallCount, dangerCount);

        if (prevFallCount < fallCount || prevDangerCount < dangerCount){
            return true;
        }
        return false;
    }
}
