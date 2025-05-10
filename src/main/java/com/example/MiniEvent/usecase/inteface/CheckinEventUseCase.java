package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.adapter.web.dto.request.CheckinRequest;

public interface CheckinEventUseCase {
    Boolean CheckinEventGPS(CheckinRequest checkinRequest, String eventId, String userId);
}
