package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Checkin;

public interface CheckinRepository {
    Checkin save(Checkin checkin);
    boolean existsByEventIdAndUserId(String eventId, String userId);
}
