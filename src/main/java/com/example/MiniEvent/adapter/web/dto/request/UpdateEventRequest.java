package com.example.MiniEvent.adapter.web.dto.request;

import com.example.MiniEvent.config.geopoint.GeoPointDeserializer;
import com.example.MiniEvent.config.geopoint.GeoPointSerializer;
import com.example.MiniEvent.model.entity.EventTag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.firestore.GeoPoint;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class UpdateEventRequest {

    private String name;

    @JsonDeserialize(using = GeoPointDeserializer.class)
    @JsonSerialize(using = GeoPointSerializer.class)
    private GeoPoint location;

    private String description;

    private Instant date;

    private Boolean privateEvent;

    private Boolean gps;

    @Positive
    private Integer limit;

    private EventTag eventTag;
    private String address;
}
