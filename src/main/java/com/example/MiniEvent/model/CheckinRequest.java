package com.example.MiniEvent.model;

import lombok.Data;

@Data
public class CheckinRequest {
    private String eventId;
    private double userLatitude;
    private double userLongitude;
}
