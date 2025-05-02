package com.example.MiniEvent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRCodeData {
    private String userId;
    private String eventId;
}
