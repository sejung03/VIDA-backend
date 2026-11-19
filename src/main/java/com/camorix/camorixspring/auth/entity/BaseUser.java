package com.camorix.camorixspring.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public abstract class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //    이메일(아이디)
    @Column(unique = true)
    private String email;

    //    이름
    private String name;

    //    성별
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    //    생년월일
    private LocalDate birthDate;

    //    주민등록번호
    private String residentId;

    //    전화번호
    private String phoneNumber;

    //    주소
    private String address;

    //    배정 병실
    private String roomNumber;

    //    회원가입 시각
    @CreatedDate
    private LocalDateTime createdAt;

    //    최근 수정 시각
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
