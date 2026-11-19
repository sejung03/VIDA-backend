package com.camorix.camorixspring.demo;

import com.camorix.camorixspring.patient.entity.Patient;
import com.camorix.camorixspring.patient.repository.PatientRepository;
import com.camorix.camorixspring.realtime.entity.Event;
import com.camorix.camorixspring.realtime.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("demo")
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final EventRepository eventRepository;

    @Override
    public void run(String... args) {
        LocalDateTime now = LocalDateTime.now();

        patientRepository.saveAll(List.of(
                patient("301", "BED-1", "김영수", "남", LocalDate.of(1941, 3, 12)),
                patient("301", "BED-2", "박정희", "여", LocalDate.of(1938, 11, 4)),
                patient("302", "BED-1", "이순자", "여", LocalDate.of(1945, 7, 21)),
                patient("302", "BED-2", "최철호", "남", LocalDate.of(1936, 1, 9))
        ));

        eventRepository.saveAll(List.of(
                event("301", "CAUTION", now.minusMinutes(42), true),
                event("302", "DANGER", now.minusMinutes(18), true),
                event("301", "FALL", now.minusMinutes(7), true)
        ));
    }

    private Patient patient(String roomNumber, String bedId, String name, String gender, LocalDate birthdate) {
        LocalDateTime now = LocalDateTime.now();
        return Patient.builder()
                .roomNumber(roomNumber)
                .bedId(bedId)
                .name(name)
                .gender(gender)
                .birthdate(birthdate)
                .phoneNumber("010-0000-0000")
                .address("데모용 임시 주소")
                .email("demo-" + roomNumber + "-" + bedId.toLowerCase() + "@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private Event event(String roomNumber, String severity, LocalDateTime occurredAt, boolean success) {
        return Event.builder()
                .roomNumber(roomNumber)
                .severity(severity)
                .occurredAt(occurredAt)
                .success(success)
                .clientIp("127.0.0.1")
                .build();
    }
}
