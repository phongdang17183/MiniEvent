package com.example.MiniEvent.adapter.web.controller.event;

import com.example.MiniEvent.adapter.web.dto.request.CheckinRequest;
import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequest;
import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequestDTO;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.EventTag;
import com.example.MiniEvent.usecase.impl.GetGuestListUseCaseImpl;
import com.example.MiniEvent.usecase.inteface.*;
import com.example.MiniEvent.adapter.web.dto.request.CreateEventRequest;
import com.example.MiniEvent.adapter.web.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final CreateEventUseCase createEventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final GetPublicEventUseCase getPublicEventUseCase;
    private final RegisterEventUseCase registerEventUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final CheckinEventUseCase checkinEventUseCase;
    private final GetGuestListUseCase getGuestListUseCase;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(
            @Valid @RequestPart(value = "event") CreateEventRequest createEventRequest,
            @RequestPart(value = "image", required = false) MultipartFile image ) {
        Event event = createEventUseCase.createEvent(createEventRequest, image);
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
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );

        UpdateEventRequestDTO requestDTO = UpdateEventRequestDTO.builder()
                .eventId(eventId)
                .request(updateEventRequest)
                .userId(user.getId())
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
    public ResponseEntity<?> getPublicEvent(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String lastDate
    ) {
        Instant cursor = lastDate != null
                ? Instant.parse(lastDate)
                : Instant.now();
        List<Event> eventList = getPublicEventUseCase.getNextPublicEvents(cursor, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all public event successfully")
                        .data(eventList)
                        .build()
        );
    }
    @GetMapping(value ="/{tag}")
    public ResponseEntity<?> getPublicEventFilter(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String lastDate,
            @PathVariable EventTag tag
    ) {
        Instant cursor = lastDate != null
                ? Instant.parse(lastDate)
                : Instant.now();
        List<Event> eventList = getPublicEventUseCase.getNextPublicEventsFilter(cursor, pageSize,tag);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all public event successfully")
                        .data(eventList)
                        .build()
        );
    }


    @GetMapping(value = "/register/{eventId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> registerEvent(
            @PathVariable String eventId,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage qrImage = registerEventUseCase.registerEvent(eventId, user.getId());
        ImageIO.write(qrImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
    }

    @PostMapping(value = "/checkin/gps")
    public ResponseEntity<?> checkinEventGPS(
            @Valid @RequestBody CheckinRequest checkinRequest,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String eventId) {

        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Checkin event by GPS successfully")
                        .data(checkinEventUseCase.CheckinEventGPS(checkinRequest, eventId, user.getId()))
                        .build()
        );
    }

    @PostMapping(value = "/checkin/qr")
    public ResponseEntity<?> checkinEventQR(
            @Valid @RequestBody String token,
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String eventId) {
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Checkin event by QR successfully")
                        .data(checkinEventUseCase.CheckinEventQR(token, eventId))
                        .build()
        );
    }

    @GetMapping("/{eventID}/userList")
    public ResponseEntity<?> getGuestList(
            @PathVariable String eventID,
            @RequestHeader("Authorization") String authHeader){
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get guest list successfully")
                        .data(getGuestListUseCase.GetGuestListWithState(eventID, user))
                        .build()
        );
    }
}
