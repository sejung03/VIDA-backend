package com.camorix.camorixspring.realtime.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder
@Jacksonized
public class FrameDTO {
    private String capturedAt;
    private int width;
    private int height;
    private String maxSeverity;
    private String roomNumber;
    private int fallCount; // 낙상 횟수
    private int dangerCount; // 위험 횟수
    private List<Person> personList;
    private List<Item> itemList;

    @Getter
    @Builder
    @Jacksonized
    public static class Person {
        private String severity;
        private String poseType;
        private int centerX;
        private int centerY;
        private int width;
        private int height;
    }

    @Getter
    @Setter
    @Builder
    @Jacksonized
    public static class Item {
        private String itemType;
        private int centerX;
        private int centerY;
        private int width;
        private int height;
        private String horz;
        private String identifier; // 식별자 (침대 ID)
    }
}
