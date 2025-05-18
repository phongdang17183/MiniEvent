package com.example.MiniEvent.model.enums;

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
