package com.example.MiniEvent.usecase;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.repository.EventRepository;
import com.example.MiniEvent.service.impl.EventUseCaseImpl;
import com.example.MiniEvent.service.usecase.ImageStorageService;
import com.example.MiniEvent.web.DTO.EventDTO;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventUseCaseImplTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private MultipartFile image;

    @InjectMocks
    private EventUseCaseImpl eventUseCaseImpl;

    private EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        // Khởi tạo EventDTO với dữ liệu mặc định
        GeoPoint location = new GeoPoint(10.7769,106.7009);
        Timestamp date = Timestamp.ofTimeMicroseconds(1634567890000000L);
        eventDTO = new EventDTO("test event", location, "A test event", date, false, false);

        // Mock SecurityContextHolder để trả về uid
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("uid123");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createEvent_success_withoutImage() throws Exception {
        // Chuẩn bị dữ liệu
        Event event = Event.builder()
                .id("event123")
                .name("Test Event")
                .date(Timestamp.ofTimeMicroseconds(1634567890000000L))
                .description("A test event")
                .location(new GeoPoint(10.7769, 106.7009))
                .privateEvent(false)
                .gps(false)
                .createdBy("uid123")
                .build();

        // Mock EventRepository
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Gọi phương thức
        Event result = eventUseCaseImpl.createEvent(eventDTO, null);

        // Kiểm tra kết quả
        assertNotNull(result);
        assertEquals("Test Event", result.getName());
        assertEquals(Timestamp.ofTimeMicroseconds(1634567890000000L), result.getDate());
        assertEquals("A test event", result.getDescription());
        assertFalse(result.getPrivateEvent());
        assertFalse(result.getGps());
        assertEquals(10.7769, result.getLocation().getLatitude(), 0.0001);
        assertEquals(106.7009, result.getLocation().getLongitude(), 0.0001);
        assertEquals("uid123", result.getCreatedBy());
        assertNull(result.getImage());

        verify(eventRepository).save(any(Event.class));
        verify(imageStorageService, never()).uploadImage(any());
    }
}
