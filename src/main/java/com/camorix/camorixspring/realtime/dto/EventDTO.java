package com.camorix.camorixspring.realtime.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventDTO {
    private String roomNumber;
    private String severity;
    private String occurredAt;
}

