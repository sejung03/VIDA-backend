package com.camorix.camorixspring.realtime.controller;

import com.camorix.camorixspring.realtime.dto.EventDTO;
import com.camorix.camorixspring.realtime.dto.FrameDTO;
import com.camorix.camorixspring.realtime.service.FrameService;
import com.camorix.camorixspring.patient.service.PatientService;
import com.camorix.camorixspring.realtime.service.SeverityCountService;
import com.camorix.camorixspring.realtime.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("realtime/frame")
@RequiredArgsConstructor
public class FrameController {
    private final SseService sseService;
    private final PatientService patientService;
    private final FrameService frameService;
    private final SeverityCountService severityCountService;

    /**
     * Vision 시스템에서 전송된 Frame 데이터를 처리하는 API
     * - 식별자 → 환자 이름 매핑
     * - 실시간 프레임 전송 (SSE)
     * - 낙상/위험도 증가 시 알림 전송
     * - 프레임 정보 DB 저장 (optional)
     */
    @PostMapping("")
//    @FrameAPISpecification
    public void frameProc(@RequestBody FrameDTO frameDTO) {

        // 1. frameDTO의 item 들의 identifier를 DB 조회하여 환자 이름으로 매핑
        if (frameDTO.getItemList() != null) {
            String roomNumber = frameDTO.getRoomNumber();
            frameDTO.getItemList().forEach(item -> {
                String patientName = patientService.getPatientName(roomNumber, item.getIdentifier());
                item.setIdentifier(patientName);
            });
        }
        // 2. 실시간 시각화 정보 전송 (SSE 방식)
        sseService.broadcastRealTimeFrame(frameDTO);

        // 3. 낙상/위험도 증가 시 알림 전송
        // - 이전 frame 대비 fall/danger count가 증가한 경우에만 전송
        // - 실제 환자 정보를 기반으로 EventDTO 생성
        if (severityCountService.countIncreased(frameDTO.getRoomNumber(), frameDTO.getFallCount(), frameDTO.getDangerCount())){
            sseService.broadcastNotification(EventDTO.builder()
                    .roomNumber(frameDTO.getRoomNumber())
                    .severity(frameDTO.getMaxSeverity())
                    .occurredAt(frameDTO.getCapturedAt())
                    .build()
            );
        }

//        4. DB에 frame 저장
//        frameService.saveProcess(frameDTO);
    }
}
