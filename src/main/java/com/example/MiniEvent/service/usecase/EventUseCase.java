package com.example.MiniEvent.service.usecase;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.web.DTO.EventDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EventUseCase {
    Event createEvent(EventDTO eventDTO, MultipartFile image) throws Exception;
}
