package com.camorix.camorixspring.realtime.entity;

import com.camorix.camorixspring.realtime.dto.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Frame {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    // 프레임이 캡처된 시각
    @Column
    private LocalDateTime capturedAt;

    // 프레임 넓이
    @Column
    private int width;

    // 프레임 높이
    @Column
    private int height;

    // 고위험도
    @Column
    @Enumerated(EnumType.STRING)
    private Severity maxSeverity;

    // 병실 번호
    @Column
    private String roomNumber;

    // 프레임에 포함된 사람 정보 리스트
    @OneToMany(mappedBy = "frame", cascade = CascadeType.ALL)
    private List<Person> personList;

    // 프레임에 포함된 객체 정보 리스트
    @OneToMany(mappedBy = "frame", cascade = CascadeType.ALL)
    private List<Item> itemList;
}