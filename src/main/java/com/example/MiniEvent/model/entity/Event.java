package com.example.MiniEvent.model.entity;

import com.example.MiniEvent.model.enums.EventTag;
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
public class Event {
    private String id;
    private String name;
    private GeoPoint location;
    private String description;
    private Instant date;
    private String image;
    private String createdBy;
    private Boolean privateEvent;
    private Integer limit;
    private Boolean gps;
    private EventTag eventTag;
    private String address;
}
