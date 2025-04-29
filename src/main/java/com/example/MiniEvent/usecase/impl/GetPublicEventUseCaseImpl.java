package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.usecase.inteface.GetPublicEventUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPublicEventUseCaseImpl implements GetPublicEventUseCase {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getPublicEvent() {
        return eventRepository.findPublicEvent();
    }
}
