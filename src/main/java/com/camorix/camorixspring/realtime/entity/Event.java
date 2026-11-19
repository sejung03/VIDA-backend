package com.camorix.camorixspring.realtime.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event { //event별로 기록을 한번만 처리함
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    // 병실 번호
    @Column(nullable = false)
    private String roomNumber;

    // 위험도
    @Column(nullable = false)
    private String severity;

    // 이벤트 발생 시간
    @Column(nullable = false)
    private LocalDateTime occurredAt;

    // 전송 성공 여부
    @Column(nullable = false)
    private boolean success;

    // 수신자 IP
    @Column(nullable = false)
    private String clientIp;
}