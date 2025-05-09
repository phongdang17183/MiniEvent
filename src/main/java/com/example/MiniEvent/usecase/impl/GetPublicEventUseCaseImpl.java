package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.EventTag;
import com.example.MiniEvent.usecase.inteface.GetPublicEventUseCase;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPublicEventUseCaseImpl implements GetPublicEventUseCase {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getNextPublicEvents(Instant cursorDate, int pageSize) {
        return eventRepository.findNextPublicEvents(cursorDate, pageSize);
    }

    @Override
    public List<Event> getNextPublicEventsFilter(Instant cursorDate, int pageSize, EventTag eventTag) {
        return eventRepository.findNextPublicEventsFilter(cursorDate, pageSize,  eventTag);
    }
}
