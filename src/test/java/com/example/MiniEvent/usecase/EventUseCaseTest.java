package com.example.MiniEvent.usecase;

import com.example.MiniEvent.adapter.web.dto.mapper.EventMapper;
import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequest;
import com.example.MiniEvent.adapter.web.dto.request.UpdateEventRequestDTO;
import com.example.MiniEvent.model.entity.DecodedTokenInfo;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.model.entity.EventTag;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.usecase.impl.CreateEventUseCaseImpl;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.adapter.web.dto.request.CreateEventRequest;
import com.example.MiniEvent.usecase.impl.UpdateEventUseCaseImpl;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EventUseCaseTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CreateEventUseCaseImpl createEventUseCase;

    @InjectMocks
    private UpdateEventUseCaseImpl updateEventUseCase;

    private CreateEventRequest createEventRequest;
    private MultipartFile imageFile;
    private Event event;

    @BeforeEach
    void setUp() {
        GeoPoint location = new GeoPoint(10.7769,106.7009);
        Timestamp date = Timestamp.ofTimeMicroseconds(1634567890000000L);
        createEventRequest = new CreateEventRequest("test event", location, "A test event", date, false, false, 50, EventTag.SPORTS, "111abc");

        event = Event.builder()
                .id("event123")
                .name("Test Event")
                .date(Timestamp.ofTimeMicroseconds(1634567890000000L))
                .description("A test event")
                .location(new GeoPoint(10.7769, 106.7009))
                .privateEvent(false)
                .gps(false)
                .limit(50)
                .createdBy("uid123")
                .build();

        imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("uid123");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createEvent_success_withoutImage() {

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = createEventUseCase.createEvent(createEventRequest, null);

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
        assertEquals(Timestamp.ofTimeMicroseconds(1634567890000000L), result.getDate());
        assertEquals("A test event", result.getDescription());
        assertFalse(result.getPrivateEvent());
        assertFalse(result.getGps());
        assertEquals(50, result.getLimit());
        assertEquals(10.7769, result.getLocation().getLatitude(), 0.0001);
        assertEquals(106.7009, result.getLocation().getLongitude(), 0.0001);
        assertEquals("uid123", result.getCreatedBy());
        assertNull(result.getImage());

        verify(eventRepository).save(any(Event.class));
        verify(imageStorageService, never()).uploadImage((MultipartFile) any());
    }

    @Test
    void createEvent_withImage_shouldUploadAndCreateEvent() {

        String uploadedImageUrl = "https://cloudinary.com/my-upload.jpg";
        when(imageStorageService.uploadImage((MultipartFile) any())).thenReturn(uploadedImageUrl);
        when(eventRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Event result = createEventUseCase.createEvent(createEventRequest, imageFile);

        assertNotNull(result);
        assertEquals(uploadedImageUrl, result.getImage());

        verify(imageStorageService).uploadImage(imageFile);
        verify(eventRepository).save(any());
    }

    @Test
    void updateEvent_success_withoutImage() {

        // Set up
        String idToken = "abc";
        String uid = "uid123";
        String eventId = "event123";
        GeoPoint updateLocation = new GeoPoint(11.7769,13.7009);
        Timestamp updateDate = Timestamp.ofTimeMicroseconds(1745147570000000L);
        UpdateEventRequest updateEventRequest = new UpdateEventRequest("update event", updateLocation, "A update test event", updateDate, true, true, 30,null,null);
        UpdateEventRequestDTO updateEventRequestDTO = new UpdateEventRequestDTO(idToken, eventId, updateEventRequest, null);
        DecodedTokenInfo decodedTokenInfo = mock(DecodedTokenInfo.class);


        when(decodedTokenInfo.getUid()).thenReturn(uid);
        when(authService.verifyToken(idToken)).thenReturn(decodedTokenInfo);
        when(eventRepository.findById("event123")).thenReturn(Optional.ofNullable(event));
        doAnswer(invocation -> {
            event.setName(updateEventRequest.getName());
            event.setDescription(updateEventRequest.getDescription());
            event.setDate(updateEventRequest.getDate());
            return null;
        }).when(eventMapper).updateEventFromRequest(updateEventRequest, event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        Event updatedEvent  = updateEventUseCase.updateEvent(updateEventRequestDTO);

        // Assert
        assertEquals("update event", updatedEvent.getName());
        assertEquals("A update test event", updatedEvent.getDescription());

        verify(authService).verifyToken(idToken);
        verify(eventRepository).findById(eventId);
        verify(eventRepository).save(event);
        verify(imageStorageService, never()).uploadImage((MultipartFile) any());
        verify(eventMapper).updateEventFromRequest(updateEventRequest, event);
    }

}
