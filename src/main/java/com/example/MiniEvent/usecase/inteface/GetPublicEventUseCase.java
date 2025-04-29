package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Event;

import java.util.List;

public interface GetPublicEventUseCase {
    List<Event> getPublicEvent();
}
