package com.example.MiniEvent.web.controller.event;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.usecase.inteface.CreateEventUseCase;
import com.example.MiniEvent.web.dto.request.CreateEventRequest;
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

    private final CreateEventUseCase eventUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(
            @Valid @RequestPart(value = "event") CreateEventRequest createEventRequest,
            @RequestPart(value = "image", required = false) MultipartFile image ) {
        Event event = eventUseCase.createEvent(createEventRequest, image);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Event create successfully")
                        .data(event)
                        .build()
        );
    }
}
