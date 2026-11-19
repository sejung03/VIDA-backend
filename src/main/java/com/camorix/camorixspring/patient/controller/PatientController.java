package com.camorix.camorixspring.patient.controller;

import com.camorix.camorixspring.patient.dto.PatientDTO;
import com.camorix.camorixspring.patient.entity.Patient;
import com.camorix.camorixspring.patient.service.PatientService;
//import com.camorix.camorixspring.swagger.specification.PatientCreateAPISpecification;
//import com.camorix.camorixspring.swagger.specification.PatientLoadAPISpecification;
//import com.camorix.camorixspring.swagger.specification.PatientUpdateAPISpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("patient") // 공통 URL prefix 설정
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> getPatientList() {
        return patientService.getPatientList();
    }

    /**
     * 공통적으로 사용하는 null 체크 & 404 응답 처리 메서드
     * @param data 응답할 객체
     * @return 객체가 null이면 404 Not Found, 아니면 200 OK로 응답
     */
    private <T> ResponseEntity<T> wrapOrNotFound(T data) {
        return data == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(data);
    }

    /**
     * 환자 정보 조회 API
     * - 병실 번호와 침대 ID로 특정 환자의 정보를 조회함
     * - 존재하지 않으면 404 반환
     */
    @GetMapping("/load/{roomNumber}/{bedId}")
//    @PatientLoadAPISpecification // Swagger 문서용 애노테이션
    public ResponseEntity<Patient> getPatientInfo(
            @PathVariable String roomNumber,
            @PathVariable String bedId
    ) {
        // 서비스로부터 환자 정보 조회 후 반환
        return wrapOrNotFound(patientService.getPatientInfo(roomNumber, bedId));
    }

    /**
     * 환자 정보 초기 등록 API
     * - 특정 병실과 침대에 새로운 환자 정보 등록
     * - 이미 존재하는 경우 409 Conflict 반환
     * - 성공 시 201 Created 반환
     */
    @PostMapping("/create/{roomNumber}/{bedId}")
//    @PatientCreateAPISpecification // Swagger UI Annotation
    public ResponseEntity<Patient> createPatient(
            @PathVariable String roomNumber,
            @PathVariable String bedId,
            @RequestBody PatientDTO createDTO // 클라이언트로부터 받은 JSON Request Body
    ) {
        Patient createdPatient = patientService.createPatientInfo(roomNumber, bedId, createDTO);

        // 생성 실패 시 중복 에러 응답
        if (createdPatient == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }

        // 성공 시 201 Created 응답과 함께 환자 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    /**
     * 환자 정보 수정 API
     * - 병실 번호와 침대 ID로 특정 환자 정보를 수정
     * - 존재하지 않으면 404 Not Found 반환
     */
    @PatchMapping("/update/{roomNumber}/{bedId}")
//    @PatientUpdateAPISpecification // Swagger UI Annotation
    public ResponseEntity<Patient> updatePatientByRoomAndBed(
            @PathVariable String roomNumber,
            @PathVariable String bedId,
            @RequestBody PatientDTO updateDTO // 수정할 정보 포함
    ) {
        // 서비스로부터 환자 정보 수정 후 반환
        return wrapOrNotFound(patientService.updatePatientInfo(roomNumber, bedId, updateDTO));
    }
}
