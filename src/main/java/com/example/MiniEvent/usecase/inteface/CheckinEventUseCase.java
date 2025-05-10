package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.adapter.web.dto.request.CheckinRequest;
import com.example.MiniEvent.model.entity.Checkin;

public interface CheckinEventUseCase {
    Checkin CheckinEventGPS(CheckinRequest checkinRequest, String eventId, String userId);
    Checkin CheckinEventQR(String token, String eventId);
}
