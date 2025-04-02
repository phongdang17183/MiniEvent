package com.example.MiniEvent.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Event {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
}
