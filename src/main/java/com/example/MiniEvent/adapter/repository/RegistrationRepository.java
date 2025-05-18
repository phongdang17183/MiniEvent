package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Registration;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository {
    Registration save(Registration registration);
    Optional<Registration> findByUserIdAndEventId(String userId, String eventId);
    Registration delete(Registration registration);
    List<Registration> findByEventId(String eventId);
    void deleteByUserId(String userId);
    List<Registration> findByUserId(String userId, Instant cursor);
}
