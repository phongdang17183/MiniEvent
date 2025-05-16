package com.example.MiniEvent.model.entity;

import lombok.Getter;

@Getter
public enum StateType {
    confirm("already checkin"),
    pending("not checkin yet");

    private final String description;

    StateType(String description) {
        this.description = description;
    }
}
