package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.Event;

public interface EventRepository {
    Event save(Event event);
    Event findById(String id);
}
