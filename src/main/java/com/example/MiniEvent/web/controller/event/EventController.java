package com.example.MiniEvent.web.controller.event;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.service.usecase.EventUseCase;
import com.example.MiniEvent.web.DTO.EventDTO;
import com.example.MiniEvent.web.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventUseCase eventUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(
            @Valid @RequestPart("event") EventDTO eventDTO,
            @RequestPart("image") MultipartFile image ) throws Exception {
        Event event = eventUseCase.createEvent(eventDTO, image);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Event create successfully")
                        .data(event)
                        .build()
        );
    }
}
