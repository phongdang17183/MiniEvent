package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.adapter.web.dto.request.CreateEventRequest;
import org.springframework.web.multipart.MultipartFile;

public interface CreateEventUseCase {
    Event createEvent(CreateEventRequest createEventRequest, MultipartFile image);
}
