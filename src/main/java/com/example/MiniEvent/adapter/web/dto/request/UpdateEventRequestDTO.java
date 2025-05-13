package com.example.MiniEvent.adapter.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class UpdateEventRequestDTO {
    private String userId;
    private String eventId;
    private UpdateEventRequest request;
    private MultipartFile image;
}
