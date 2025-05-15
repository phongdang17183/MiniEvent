package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.CheckinRepository;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.web.dto.request.CheckinRequest;
import com.example.MiniEvent.adapter.web.exception.BadRequestException;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.Checkin;
import com.example.MiniEvent.model.entity.CheckinMethod;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.QRCodeData;
import com.example.MiniEvent.service.inteface.QRCodeGenService;
import com.example.MiniEvent.usecase.inteface.CheckinEventUseCase;
import com.google.cloud.firestore.GeoPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinEventUseCaseImpl implements CheckinEventUseCase {

    private final EventRepository eventRepository;
    private final CheckinRepository checkinRepository;
    private final QRCodeGenService qrCodeGenService;

    @Value("${app.distance.threshold}")
    private double DISTANCE_THRESHOLD;

    @Override
    public Checkin CheckinEventGPS(CheckinRequest checkinRequest, String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getGps()) {
            throw new BadRequestException("This event does not support GPS check-in", HttpStatus.BAD_REQUEST);
        }

        double distance = calculateDistance(
                event.getLocation().getLatitude(), event.getLocation().getLongitude(),
                checkinRequest.getUserLatitude(), checkinRequest.getUserLongitude()
        );

        Checkin checkin = Checkin.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .eventId(eventId)
                .date(Instant.now())
                .checkinMethod(CheckinMethod.GPS)
                .location(new GeoPoint(checkinRequest.getUserLatitude(), checkinRequest.getUserLongitude()))
                .build();

        return checkinRepository.save(checkin);
    }

    @Override
    public Checkin CheckinEventQR(String token, String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND));

        QRCodeData qrCodeData = qrCodeGenService.getData(token);

        Checkin checkin = Checkin.builder()
                .id(UUID.randomUUID().toString())
                .userId(qrCodeData.getUserId())
                .eventId(qrCodeData.getEventId())
                .date(Instant.now())
                .checkinMethod(CheckinMethod.QR)
                .location(event.getLocation())
                .build();

        checkinRepository.save(checkin);
        return checkin;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        log.info(String.valueOf(R * c));
        log.info(String.valueOf(DISTANCE_THRESHOLD));
        return R * c;
    }
}
