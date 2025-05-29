package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Checkin;

import java.util.Optional;

public interface CheckinRepository {
    Checkin save(Checkin checkin);
    boolean existsByEventIdAndUserId(String eventId, String userId);
    void deleteByUserId(String userId);
    Optional<Checkin> findByEventIdAndUserId(String eventId, String userId);
}
