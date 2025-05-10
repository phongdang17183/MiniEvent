package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.CheckinRepository;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.web.dto.request.CheckinRequest;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.Checkin;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.usecase.inteface.CheckinEventUseCase;
import com.google.cloud.firestore.GeoPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinEventUseCaseImpl implements CheckinEventUseCase {

    private final EventRepository eventRepository;
    private final CheckinRepository checkinRepository;

    @Value("${app.distance.threshold}")
    private double DISTANCE_THRESHOLD;

    @Override
    public Boolean CheckinEventGPS(CheckinRequest checkinRequest, String eventId, String userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND));

        double distance = calculateDistance(
                event.getLocation().getLatitude(), event.getLocation().getLongitude(),
                checkinRequest.getUserLatitude(), checkinRequest.getUserLongitude()
        );

        Checkin checkin = Checkin.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .eventId(eventId)
                .date(new Date())
                .location(new GeoPoint(checkinRequest.getUserLatitude(), checkinRequest.getUserLongitude()))
                .build();

        checkinRepository.save(checkin);
        return distance <= DISTANCE_THRESHOLD;
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
