package com.example.MiniEvent.model.enums;

import lombok.Getter;

@Getter
public enum EventStatus {
    COMPLETE("event already completed"),
    UPCOMING("event is upcoming");

    private final String description;

    EventStatus(String description) {
        this.description = description;
    }
}
