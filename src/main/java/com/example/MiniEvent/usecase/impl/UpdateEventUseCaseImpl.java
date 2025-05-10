package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.web.dto.mapper.EventMapper;
import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequestDTO;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.adapter.web.exception.ForbiddenException;
import com.example.MiniEvent.model.entity.DecodedTokenInfo;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.usecase.inteface.UpdateEventUseCase;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ImageStorageService imageStorageService;

    @Override
    public Event updateEvent(UpdateEventRequestDTO updateEventRequestDTO) {

        Event event = eventRepository.findById(updateEventRequestDTO.getEventId())
                .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getCreatedBy().equals(updateEventRequestDTO.getUserId())) {
            throw new ForbiddenException("You are not allowed to update this event", HttpStatus.FORBIDDEN);
        }

        eventMapper.updateEventFromRequest(updateEventRequestDTO.getRequest(), event);
        if (updateEventRequestDTO.getImage() != null && !updateEventRequestDTO.getImage().isEmpty()) {
            event.setImage(imageStorageService.uploadImage(updateEventRequestDTO.getImage()));
        }
        return eventRepository.save(event);
    }
}
