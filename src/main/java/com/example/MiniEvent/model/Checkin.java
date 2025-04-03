package com.example.MiniEvent.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checkin {
    private String userId;
    private String eventId;
    private GeoPoint location;
    private Timestamp date;
}
