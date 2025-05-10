package com.example.MiniEvent.model.entity;

import lombok.Getter;

@Getter
public enum CheckinMethod {
    GPS("check-in by GPS"),
    QR("check-in by QR");

    private final String description;

    CheckinMethod(String description) {
        this.description = description;
    }

}
