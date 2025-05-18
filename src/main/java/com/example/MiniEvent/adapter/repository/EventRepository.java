package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.enums.EventTag;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(String id);
    List<Event> findNextPublicEvents(Instant cursorDate, int pageSize);
    List<Event> findNextPublicEventsFilter(Instant cursorDate, int pageSize, EventTag eventTag);
    boolean isNotCreatedByUserId(String eventId, String userId);
    void deleteByUserId(String userId);
}
