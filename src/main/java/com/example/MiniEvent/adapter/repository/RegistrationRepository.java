package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Registration;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository {
    Registration save(Registration registration);
    Optional<Registration> findByUserIdAndEventId(String userId, String eventId);
    List<Registration> findByEventId(String eventId);
}
