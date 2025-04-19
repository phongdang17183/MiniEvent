package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.AppUser;

public interface UserRepository {
    AppUser save(AppUser user);
    boolean existsByEmail(String email);
}
