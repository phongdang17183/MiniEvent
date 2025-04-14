package com.example.MiniEvent.config.timestamp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.cloud.Timestamp;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimestampDeserializer extends JsonDeserializer<Timestamp> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneId.of("UTC"));

    @Override
    public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String dateStr = jsonParser.getText();
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IOException("Timestamp cannot be null or empty");
        }
        try {
            Instant instant = Instant.from(FORMATTER.parse(dateStr));
            return Timestamp.of(Date.from(instant));
        } catch (Exception e) {
            throw new IOException("Invalid Timestamp format: " + dateStr + ". Expected format: yyyy-MM-dd'T'HH:mm:ss'Z'", e);
        }
    }
}
