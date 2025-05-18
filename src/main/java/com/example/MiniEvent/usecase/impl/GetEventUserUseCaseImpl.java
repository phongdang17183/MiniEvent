package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.repository.RegistrationRepository;
import com.example.MiniEvent.adapter.web.dto.EventDTO;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.Registration;
import com.example.MiniEvent.model.enums.EventStatus;
import com.example.MiniEvent.usecase.inteface.GetEventUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetEventUserUseCaseImpl implements GetEventUserUseCase {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public List<EventDTO> getEventsCreated(String userId, Instant cursorDate) {
        List<Event> events = eventRepository.findByCreateBy(userId, cursorDate);
        return events.stream().map(
                event -> {
                    EventStatus eventStatus = event.getDate().isBefore(Instant.now()) ? EventStatus.COMPLETE : EventStatus.UPCOMING;
                    return new EventDTO(event, eventStatus);
                }
        ).toList();
    }

    @Override
    public List<Event> getEventsJoin(String userId, Instant cursorDate) {
        List<Registration> registrations = registrationRepository.findByUserId(userId, cursorDate);
        return registrations.stream()
                .map(registration -> eventRepository.findById(registration.getEventId())
                        .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND)))
                .toList();
    }
}
