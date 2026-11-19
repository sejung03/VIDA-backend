package com.camorix.camorixspring.patient.repository;

import com.camorix.camorixspring.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

/**
 * Patient 엔티티에 대한 JPA Repository 인터페이스
 * - 기본 CRUD 기능 제공
 * - 식별자는 UUID 타입을 사용함
 */
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    /**
     * 병실 번호와 침대 ID를 기준으로 환자 이름만 조회
     * - @Query 사용: SELECT 대상이 엔티티 전체가 아닌 name 필드
     * - 반환 타입: Optional<String>
     */
    @Query("SELECT p.name FROM Patient p WHERE p.roomNumber = :roomNumber AND p.bedId = :bedId")
    Optional<String> findNameByRoomNumberAndBedId(@Param("roomNumber") String roomNumber, @Param("bedId") String bedId);

    /**
     * 병실 번호와 침대 ID를 기준으로 환자 전체 정보 조회
     * - 메서드 이름 기반 쿼리 자동 생성
     * - 반환 타입: Optional<Patient>
     */
    Optional<Patient> findByRoomNumberAndBedId(String roomNumber, String bedId);
}
