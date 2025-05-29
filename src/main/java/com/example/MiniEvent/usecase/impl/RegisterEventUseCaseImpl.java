package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.repository.RegistrationRepository;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.QRCodeData;
import com.example.MiniEvent.model.entity.Registration;
import com.example.MiniEvent.model.enums.RegistrationType;
import com.example.MiniEvent.service.inteface.QRCodeGenService;
import com.example.MiniEvent.usecase.inteface.RegisterEventUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterEventUseCaseImpl implements RegisterEventUseCase {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final QRCodeGenService qrCodeGenService;

    @Override
    public BufferedImage registerEvent(String eventId, String userId) {

        Optional<Registration> register = registrationRepository.findByUserIdAndEventId(userId, eventId);
        if (register.isPresent()) {
            qrCodeGenService.generateQRCodeImage(new QRCodeData(userId, eventId));
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND);
        }
        Registration registration = Registration.builder()
                .id(UUID.randomUUID().toString())
                .eventId(eventId)
                .userId(userId)
                .type(RegistrationType.SELF_REGISTERED)
                .registerAt(Instant.now())
                .build();
        registrationRepository.save(registration);
        return qrCodeGenService.generateQRCodeImage(new QRCodeData(userId, eventId));
    }
}
