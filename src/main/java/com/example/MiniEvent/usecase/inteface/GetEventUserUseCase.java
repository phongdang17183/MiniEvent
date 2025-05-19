package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.adapter.web.dto.EventDTO;
import com.example.MiniEvent.model.entity.Event;

import java.time.Instant;
import java.util.List;

public interface GetEventUserUseCase {
    List<EventDTO> getEventsCreated(String userId, Instant cursorDate);
    List<Event> getEventsJoin(String userId, Instant cursorDate);
}
