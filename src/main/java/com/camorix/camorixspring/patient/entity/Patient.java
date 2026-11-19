package com.camorix.camorixspring.patient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    // 침대 ID (identifier)
    @Column
    private String bedId;

    // 병실 번호
    @Column
    private String roomNumber;

    // 환자 이름
    @Column
    private String name;

    // 성별
    @Column
    private String gender;

    // 생년월일
    @Column
    private LocalDate birthdate;

    // 휴대전화 번호
    @Column
    private String phoneNumber;

    // 주소
    @Column(columnDefinition = "TEXT")
    private String address;

    // 이메일
    @Column
    private String email;

    // 생성 시간
    @Column
    private LocalDateTime createdAt;

    // 수정 시간
    @Column
    private LocalDateTime updatedAt;
}
