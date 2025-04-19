package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.AppUser;

import java.util.Optional;

public interface GetUserInfoUseCase {
    Optional<AppUser> getInfo(String idToken);
}
