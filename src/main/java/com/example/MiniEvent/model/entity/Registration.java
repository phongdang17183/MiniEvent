package com.example.MiniEvent.model.entity;

import com.example.MiniEvent.model.enums.RegistrationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    Registration {
    private String id;
    private String userId;
    private String eventId;
    private Instant registerAt;
    private RegistrationType type;
}
