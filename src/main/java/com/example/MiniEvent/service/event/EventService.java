package com.example.MiniEvent.service.event;

import com.example.MiniEvent.DTO.EventDTO;
import com.example.MiniEvent.model.Event;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final Firestore firestore;

    public Event createEvent(EventDTO eventDTO) throws Exception {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();

        if (eventDTO.getName() == null || eventDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (eventDTO.getDate() == null) {
            throw new IllegalArgumentException("Event date is required");
        }

        String eventId = UUID.randomUUID().toString();
        Event event = Event.builder()
                .id(eventId)
                .name(eventDTO.getName())
                .location(eventDTO.getLocation())
                .description(eventDTO.getDescription())
                .date(eventDTO.getDate())
                .image(eventDTO.getImage())
                .createdBy(uid)
                .build();

        firestore.collection("events").document(eventId).set(event).get();
        return event;
    }


}
