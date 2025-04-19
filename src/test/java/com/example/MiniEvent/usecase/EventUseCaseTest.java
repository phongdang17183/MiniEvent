package com.example.MiniEvent.usecase;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.usecase.impl.CreateEventUseCaseImpl;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.adapter.web.dto.request.CreateEventRequest;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventUseCaseTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private CreateEventUseCaseImpl eventUseCaseImpl;

    private CreateEventRequest createEventRequest;
    private MultipartFile imageFile;
    private Event event;

    @BeforeEach
    void setUp() {
        GeoPoint location = new GeoPoint(10.7769,106.7009);
        Timestamp date = Timestamp.ofTimeMicroseconds(1634567890000000L);
        createEventRequest = new CreateEventRequest("test event", location, "A test event", date, false, false, 50);

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
    void createEvent_success_withoutImage() throws Exception {

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = eventUseCaseImpl.createEvent(createEventRequest, null);

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
        verify(imageStorageService, never()).uploadImage(any());
    }

    @Test
    void createEvent_withImage_shouldUploadAndCreateEvent() throws Exception {

        String uploadedImageUrl = "https://cloudinary.com/my-upload.jpg";
        when(imageStorageService.uploadImage(any())).thenReturn(uploadedImageUrl);
        when(eventRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Event result = eventUseCaseImpl.createEvent(createEventRequest, imageFile);

        assertNotNull(result);
        assertEquals(uploadedImageUrl, result.getImage());

        verify(imageStorageService).uploadImage(imageFile);
        verify(eventRepository).save(any());
    }

}
