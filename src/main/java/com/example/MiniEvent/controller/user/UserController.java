package com.example.MiniEvent.controller.user;

import com.example.MiniEvent.DTO.EventDTO;
import com.example.MiniEvent.model.Event;
import com.example.MiniEvent.response.ResponseObject;
import com.example.MiniEvent.service.event.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.version}/users")
@Slf4j
public class UserController {

    private final EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDTO eventDTO) throws Exception {
        Event event = eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Event create successfully")
                        .data(event)
                        .build()
        );
    }
}
