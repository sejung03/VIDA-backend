package com.camorix.camorixspring.patient.service;

import com.camorix.camorixspring.patient.dto.PatientDTO;
import com.camorix.camorixspring.patient.entity.Patient;
import com.camorix.camorixspring.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 환자 정보 관리를 위한 서비스 클래스
 * - 병실 번호와 침대 ID를 기준으로 환자 조회, 등록, 수정
 * - 중복 등록 방지 및 입력값 조건에 따른 수정 처리
 * - DTO를 엔티티로 변환 후 저장
 */
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public List<Patient> getPatientList() {
        return patientRepository.findAll();
    }

    /**
     * 병실 번호(roomNumber)와 침대 번호(bedId)를 기준으로
     * 환자의 이름을 조회. 없으면 "UNIDENTIFIED" 반환.
     */
    public String getPatientName (String roomNumber, String bedId) {
        return patientRepository.findNameByRoomNumberAndBedId(roomNumber, bedId)
                .orElse("UNIDENTIFIED");
    }

    /**
     * 병실 번호와 침대 번호를 기준으로 환자 전체 정보를 조회.
     * 없으면 null 반환.
     */
    public Patient getPatientInfo(String roomNumber, String bedId) {
        return patientRepository.findByRoomNumberAndBedId(roomNumber, bedId)
                .orElse(null);
    }

    /**
     * 병실 번호와 침대 번호를 기준으로 환자 등록.
     * 이미 등록된 침대일 경우 null 반환.
     */
    public Patient createPatientInfo(String roomNumber, String bedId, PatientDTO createDTO) {
        if (patientRepository.findByRoomNumberAndBedId(roomNumber, bedId).isPresent()) {
            return null; // 중복 발생 시 null 반환
        }

        Patient patient = Patient.builder()
                .roomNumber(roomNumber)
                .bedId(bedId)
                .name(createDTO.getName())
                .gender(createDTO.getGender())
                .birthdate(createDTO.getBirthDate())
                .phoneNumber(createDTO.getPhoneNumber())
                .address(createDTO.getAddress())
                .email(createDTO.getEmail())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return patientRepository.save(patient);
    }

    /**
     * 환자 정보 수정
     * - 입력된 필드만 조건적으로 업데이트
     * - 병실 번호 + 침대 번호로 조회 후 수정
     * - 없으면 null 반환
     */
    public Patient updatePatientInfo(String roomNumber, String bedId, PatientDTO updateDTO) {
        return patientRepository.findByRoomNumberAndBedId(roomNumber, bedId)
                .map(patient -> {
                    if (updateDTO.getName() != null) patient.setName(updateDTO.getName());
                    if (updateDTO.getPhoneNumber() != null) patient.setPhoneNumber(updateDTO.getPhoneNumber());
                    if (updateDTO.getGender() != null) patient.setGender(updateDTO.getGender());
                    if (updateDTO.getEmail() != null) patient.setEmail(updateDTO.getEmail());
                    if (updateDTO.getAddress() != null) patient.setAddress(updateDTO.getAddress());
                    if (updateDTO.getBirthDate() != null) patient.setBirthdate(updateDTO.getBirthDate());

                    patient.setUpdatedAt(LocalDateTime.now());
                    return patientRepository.save(patient);
                })
                .orElse(null); // controller에서 404 처리
    }
}
