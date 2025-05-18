package com.example.MiniEvent.model.enums;

import lombok.Getter;

@Getter
public enum RegistrationType {
    SELF_REGISTERED("tu tham gia su kien (public)"),
    ADDED_BY_HOST("duoc them boi chu su kien (private)");

    private final String description;

    RegistrationType(String description) {
        this.description = description;
    }
}
