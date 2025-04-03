package com.example.MiniEvent.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckinRequest {
    private String userId;
    private String eventId;
    private double userLatitude;
    private double userLongitude;
}
