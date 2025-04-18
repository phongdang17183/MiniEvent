package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.Event;

import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(String id);
}
