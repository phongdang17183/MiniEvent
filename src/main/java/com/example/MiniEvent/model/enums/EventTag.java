package com.example.MiniEvent.model.enums;

public enum EventTag {
    SPORTS("Thể thao"),
    EDUCATION("Giáo dục"),
    TECHNOLOGY("Công nghệ"),
    ENTERTAINMENT("Văn hóa & Giải trí"),
    BUSINESS("Kinh doanh / Khởi nghiệp"),
    HEALTH("Y tế / Sức khỏe"),
    MUSIC("Âm nhạc"),
    ART("Nghệ thuật"),
    FOOD("Ẩm thực"),
    TRAVEL("Du lịch"),
    CHARITY("Từ thiện / Xã hội");

    private final String description;

    EventTag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

