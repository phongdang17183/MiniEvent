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
public class Event {
    private String id;
    private String name;
    private GeoPoint location;
    private String description;
    private Timestamp date;
    private String image;
    private String createdBy;
    private Boolean privateEvent;
    private Boolean gps;
}
