package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.usecase.inteface.GetUserByPhoneUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserByPhoneUseCaseImpl implements GetUserByPhoneUseCase {

    private final UserRepository userRepository;

    @Override
    public List<AppUser> findAllByPhoneAfter(String phone, Instant cursor) {
        return userRepository.findAllByPhoneAfter(phone, cursor);
    }
}
