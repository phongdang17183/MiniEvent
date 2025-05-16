package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.repository.RegistrationRepository;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.adapter.web.exception.BadRequestException;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.Registration;
import com.example.MiniEvent.model.enums.RegistrationType;
import com.example.MiniEvent.usecase.inteface.UpdateUserInEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserInEventImpl implements UpdateUserInEvent {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public Registration addUserInEvent(String ownerId, String eventId, String phoneNumber) {
        if (eventRepository.isNotCreatedByUserId(eventId, ownerId))
            throw new BadRequestException("Event is not created by this user ", HttpStatus.BAD_REQUEST);

        AppUser user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND));
        if (registrationRepository.findByUserIdAndEventId(user.getId(), eventId).isPresent())
            throw new BadRequestException("User are already register", HttpStatus.BAD_REQUEST);

        Registration registration = Registration.builder()
                .id(UUID.randomUUID().toString())
                .eventId(eventId)
                .userId(user.getId())
                .registerAt(Instant.now())
                .type(RegistrationType.ADDED_BY_HOST)
                .build();
        return registrationRepository.save(registration);
    }

    @Override
    public Registration removeUserInEvent(String ownerId, String eventId, String phoneNumber) {
        if (eventRepository.isNotCreatedByUserId(eventId, ownerId))
            throw new BadRequestException("Event is not created by this user ", HttpStatus.BAD_REQUEST);

        AppUser user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND));
        if (registrationRepository.findByUserIdAndEventId(user.getId(), eventId).isEmpty())
            throw new BadRequestException("User are not register yet", HttpStatus.BAD_REQUEST);

        Registration registration = registrationRepository.findByUserIdAndEventId(user.getId(), eventId)
                .orElseThrow(() -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND));

        return registrationRepository.delete(registration);
    }
}
