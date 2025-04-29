package com.example.MiniEvent.adapter.web.controller.event;

import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequest;
import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequestDTO;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.usecase.inteface.CreateEventUseCase;
import com.example.MiniEvent.adapter.web.dto.request.CreateEventRequest;
import com.example.MiniEvent.adapter.web.response.ResponseObject;
import com.example.MiniEvent.usecase.inteface.GetPublicEventUseCase;
import com.example.MiniEvent.usecase.inteface.UpdateEventUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final CreateEventUseCase eventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final GetPublicEventUseCase getPublicEventUseCase;

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

    @PutMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateEvent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String eventId,
            @Valid @RequestPart(value = "updateEvent") UpdateEventRequest updateEventRequest,
            @RequestPart(value = "image", required = false) MultipartFile image ) {
        String idToken = authHeader.replace("Bearer ", "");

        UpdateEventRequestDTO requestDTO = UpdateEventRequestDTO.builder()
                .eventId(eventId)
                .request(updateEventRequest)
                .idToken(idToken)
                .image(image)
                .build();

        Event event = updateEventUseCase.updateEvent(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Update event successfully")
                        .data(event)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<?> getPublicEvent() {
        List<Event> eventList = getPublicEventUseCase.getPublicEvent();
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all public event successfully")
                        .data(eventList)
                        .build()
        );
    }
}
