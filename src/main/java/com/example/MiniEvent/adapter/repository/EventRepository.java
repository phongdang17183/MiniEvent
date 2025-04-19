package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Event;

import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(String id);
}
