package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.CheckinRepository;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.repository.RegistrationRepository;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.service.impl.FireBaseAuthService;
import com.example.MiniEvent.usecase.inteface.DeleteAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAccountUseCaseImpl implements DeleteAccountUseCase {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CheckinRepository checkinRepository;
    private final FireBaseAuthService fireBaseAuthService;
    private final RegistrationRepository registrationRepository;

    @Override
    public void deleteAccount(String userId) {
        fireBaseAuthService.deleteAccount(userId);
        userRepository.deleteByUserId(userId);
        eventRepository.deleteByUserId(userId);
        checkinRepository.deleteByUserId(userId);
        registrationRepository.deleteByUserId(userId);
    }
}
