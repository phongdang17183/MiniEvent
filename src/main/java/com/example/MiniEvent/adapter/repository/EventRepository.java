package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(String id);
    List<Event> findNextPublicEvents(Instant cursorDate, int pageSize);
}
