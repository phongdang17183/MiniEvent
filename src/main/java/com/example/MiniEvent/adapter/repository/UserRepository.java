package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.AppUser;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    AppUser save(AppUser user);
    boolean existsByEmail(String email);
    Optional<AppUser> findByUid(String uid);
    List<AppUser> findAllByPhoneAfter(String phone, Instant cursor);
    Optional<AppUser> findByPhone(String phone);
    void deleteByUserId(String userId);
}
