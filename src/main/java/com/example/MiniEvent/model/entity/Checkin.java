package com.example.MiniEvent.model.entity;

import com.google.cloud.firestore.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checkin {
    private String id;
    private String userId;
    private String eventId;
    private GeoPoint location;
    private Instant date;
    private CheckinMethod checkinMethod;
}
