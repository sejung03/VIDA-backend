package com.camorix.camorixspring.realtime.service;

import com.camorix.camorixspring.realtime.dto.FrameDTO;
import com.camorix.camorixspring.realtime.dto.PoseType;
import com.camorix.camorixspring.realtime.dto.Severity;
import com.camorix.camorixspring.realtime.entity.Frame;
import com.camorix.camorixspring.realtime.entity.Item;
import com.camorix.camorixspring.realtime.entity.Person;
import com.camorix.camorixspring.realtime.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * FrameDTO 데이터를 엔티티로 변환 후 DB에 저장하는 서비스 클래스
 * - Frame 생성
 * - Person, Item 리스트 생성 및 연결
 * - Frame 저장
 */
@Service
@RequiredArgsConstructor
public class FrameService {
    private final FrameRepository frameRepository;

    public void saveProcess(FrameDTO frameDTO) {
        // 1. Frame 엔티티 생성
        Frame frame = Frame.builder()
                .capturedAt(LocalDateTime.parse(
                        frameDTO.getCapturedAt(),
                        DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                ))
                .width(frameDTO.getWidth())
                .height(frameDTO.getHeight())
                .maxSeverity(Severity.valueOf(frameDTO.getMaxSeverity()))
                .roomNumber(frameDTO.getRoomNumber())
                .build();

        // 2. Person 엔티티 리스트 생성 및 연결
        List<Person> personList = new ArrayList<>();
        frameDTO.getPersonList().forEach(person -> {
            personList.add(Person.builder()
                    .frame(frame)
                    .severity(Severity.valueOf(person.getSeverity()))
                    .poseType(PoseType.valueOf(person.getPoseType()))
                    .centerX(person.getCenterX())
                    .centerY(person.getCenterY())
                    .width(person.getWidth())
                    .height(person.getHeight())
                    .build());
        });

        // 3. Item 엔티티 리스트 생성 및 연결
        List<Item> itemList = new ArrayList<>();
        frameDTO.getItemList().forEach(item -> {
            itemList.add(Item.builder()
                    .frame(frame)
                    .itemType(item.getItemType())
                    .centerX(item.getCenterX())
                    .centerY(item.getCenterY())
                    .width(item.getWidth())
                    .height(item.getHeight())
                    .horz(item.getHorz())
                    .patientName(item.getIdentifier())
                    .build());
        });

        // 4. Frame에 Person, Item 리스트 설정
        frame.setPersonList(personList);
        frame.setItemList(itemList);

        // 5. DB 저장
        frameRepository.save(frame);
    }
}
