package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.AppUser;

import java.time.Instant;
import java.util.List;

public interface GetUserByPhoneUseCase {
    List<AppUser> findAllByPhoneAfter(String phone, Instant cursor);
}
