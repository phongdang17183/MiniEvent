package com.example.MiniEvent.config.timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.Timestamp;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimestampSerializer extends JsonSerializer<Timestamp> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneId.of("UTC"));

    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (timestamp == null) {
            jsonGenerator.writeNull();
            return;
        }
        Instant instant = timestamp.toDate().toInstant();
        jsonGenerator.writeString(FORMATTER.format(instant));
    }
}
