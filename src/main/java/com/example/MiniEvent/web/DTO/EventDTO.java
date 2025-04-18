package com.example.MiniEvent.web.DTO;

import com.example.MiniEvent.config.geopoint.GeoPointDeserializer;
import com.example.MiniEvent.config.geopoint.GeoPointSerializer;
import com.example.MiniEvent.config.timestamp.TimestampDeserializer;
import com.example.MiniEvent.config.timestamp.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EventDTO {

    @NotEmpty
    private String name;

    @NotNull
    @JsonDeserialize(using = GeoPointDeserializer.class)
    @JsonSerialize(using = GeoPointSerializer.class)
    private GeoPoint location;

    @NotEmpty
    private String description;

    @NotNull
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp date;

    @NotNull
    private Boolean privateEvent;

    @NotNull
    private Boolean gps;

}