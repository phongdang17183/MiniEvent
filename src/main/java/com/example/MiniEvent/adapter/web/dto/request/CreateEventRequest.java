package com.example.MiniEvent.adapter.web.dto.request;

import com.example.MiniEvent.config.geopoint.GeoPointDeserializer;
import com.example.MiniEvent.config.geopoint.GeoPointSerializer;
import com.example.MiniEvent.model.entity.EventTag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.firestore.GeoPoint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class CreateEventRequest {

    @NotEmpty
    private String name;

    @NotNull
    @JsonDeserialize(using = GeoPointDeserializer.class)
    @JsonSerialize(using = GeoPointSerializer.class)
    private GeoPoint location;

    @NotEmpty
    private String description;

    @NotNull
    private Instant date;

    @NotNull
    private Boolean privateEvent;

    @NotNull
    private Boolean gps;

    @NotNull
    @Positive
    private Integer limit;

    @NotNull
    private EventTag eventTag;

    @NotEmpty
    private String address;

}