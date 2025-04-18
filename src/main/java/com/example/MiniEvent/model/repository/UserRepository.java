package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.web.dto.request.RegisterRequest;

import java.util.Optional;

public interface UserRepository {
    AppUser save(RegisterRequest request);
    Optional<AppUser> findById(String id);
    boolean existsByEmail(String email);
}
