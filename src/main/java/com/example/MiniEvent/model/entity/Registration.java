package com.example.MiniEvent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    Registration {
    private String id;
    private String userId;
    private String eventId;
    private Date registerAt;
}
