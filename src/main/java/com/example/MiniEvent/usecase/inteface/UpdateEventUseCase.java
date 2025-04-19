package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequestDTO;
import com.example.MiniEvent.model.entity.Event;

public interface UpdateEventUseCase {
    Event updateEvent(UpdateEventRequestDTO updateEventRequestDTO);
}
