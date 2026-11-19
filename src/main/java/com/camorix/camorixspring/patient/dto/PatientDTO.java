package com.camorix.camorixspring.patient.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Jacksonized
public class PatientDTO {
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
