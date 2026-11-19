package com.camorix.camorixspring.realtime.entity;

import com.camorix.camorixspring.realtime.dto.PoseType;
import com.camorix.camorixspring.realtime.dto.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 해당 사람이 포함된 프레임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_id")
    private Frame frame;

    // 위험도
    @Column
    @Enumerated(EnumType.STRING)
    private Severity severity;

    // 자세 유형
    @Column
    @Enumerated(EnumType.STRING)
    private PoseType poseType;

    // 중심 좌표 X
    @Column
    private int centerX;

    // 중심 좌표 Y
    @Column
    private int centerY;

    // 폭
    @Column
    private int width;

    // 높이
    @Column
    private int height;
}