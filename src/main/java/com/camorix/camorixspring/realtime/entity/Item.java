package com.camorix.camorixspring.realtime.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    // 해당 객체가 포함된 프레임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_id")
    private Frame frame;

    // 객체 종류
    @Column
    private String itemType;

    // 객체 중심 좌표 X
    @Column
    private int centerX;

    // 객체 중심 좌표 Y
    @Column
    private int centerY;

    // 객체 너비
    @Column
    private int width;

    // 객체 높이
    @Column
    private int height;

    // 가로 방향 상태
    @Column
    private String horz;

    // 환자 이름
    @Column
    private String patientName;
}
