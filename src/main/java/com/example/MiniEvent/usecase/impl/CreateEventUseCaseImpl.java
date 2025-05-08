package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.usecase.inteface.CreateEventUseCase;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.adapter.web.dto.request.CreateEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateEventUseCaseImpl implements CreateEventUseCase {

    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public Event createEvent(CreateEventRequest eventDTO, MultipartFile image) {

        if (eventDTO.getName() == null || eventDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (eventDTO.getDate() == null) {
            throw new IllegalArgumentException("Event date is required");
        }
        if(eventDTO.getEventTag() == null ){
            throw new IllegalArgumentException("Event tag is required");
        }

        String uid = SecurityContextHolder.getContext().getAuthentication().getName();

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageStorageService.uploadImage(image);
        }

        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .name(eventDTO.getName())
                .location(eventDTO.getLocation())
                .description(eventDTO.getDescription())
                .date(eventDTO.getDate())
                .privateEvent(eventDTO.getPrivateEvent())
                .limit(eventDTO.getLimit())
                .gps(eventDTO.getGps())
                .image(imageUrl)
                .eventTag(eventDTO.getEventTag())
                .createdBy(uid)
                .build();

        return eventRepository.save(event);
    }
}
