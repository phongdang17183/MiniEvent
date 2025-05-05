package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.usecase.inteface.GetUserByPhoneUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserByPhoneUseCaseImpl implements GetUserByPhoneUseCase {

    private final UserRepository userRepository;

    @Override
    public List<AppUser> findByPhone(String phone, Instant cursor) {
        return userRepository.findByPhone(phone, cursor);
    }
}
