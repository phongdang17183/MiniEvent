package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.web.DTO.EventDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CreateEventUseCase {
    Event createEvent(EventDTO eventDTO, MultipartFile image) throws Exception;
}
