package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.AppUser;

import java.util.Optional;

public interface UserRepository {
    AppUser save(AppUser user);
    boolean existsByEmail(String email);
    Optional<AppUser> findByUid(String uid);
}
