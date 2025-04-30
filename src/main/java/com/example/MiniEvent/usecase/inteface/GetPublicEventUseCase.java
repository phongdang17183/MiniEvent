package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Event;

import java.time.Instant;
import java.util.List;

public interface GetPublicEventUseCase {
    List<Event> getNextPublicEvents(Instant cursorDate, int pageSize);
}
