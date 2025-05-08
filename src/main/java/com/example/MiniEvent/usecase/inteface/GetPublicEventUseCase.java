package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.EventTag;

import java.time.Instant;
import java.util.List;

public interface GetPublicEventUseCase {
    List<Event> getNextPublicEvents(Instant cursorDate, int pageSize);
    List<Event> getNextPublicEventsFilter(Instant cursorDate, int pageSize, EventTag eventTag);
}
