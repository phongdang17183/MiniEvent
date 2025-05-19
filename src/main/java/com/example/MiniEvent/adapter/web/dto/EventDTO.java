package com.example.MiniEvent.adapter.web.dto;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDTO {
    private Event event;
    private EventStatus stateType;
}
