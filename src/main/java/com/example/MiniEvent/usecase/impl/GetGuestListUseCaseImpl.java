package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.CheckinRepository;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.repository.RegistrationRepository;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.adapter.web.response.GuestListResponse;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.Registration;
import com.example.MiniEvent.usecase.inteface.GetGuestListUseCase;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetGuestListUseCaseImpl implements GetGuestListUseCase {
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final CheckinRepository checkinRepository;
    private final UserRepository userRepository;

    public GetGuestListUseCaseImpl(EventRepository eventRepository, RegistrationRepository registrationRepository, CheckinRepository checkinRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.checkinRepository = checkinRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GuestListResponse> GetGuestListWithState(String eventId, AppUser user) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getCreatedBy().equals(user.getId())) {
            throw new DataAccessException("You are not the owner of event: " + eventId) {};
        }

        List<Registration> registrations = registrationRepository.findByEventId(eventId);

        return registrations.stream()
                .map(registration -> {
                    String guestUserId = registration.getUserId();
                    boolean hasCheckedIn = checkinRepository.existsByEventIdAndUserId(eventId, guestUserId);
                    AppUser guestUser = userRepository.findByUid(guestUserId).orElseThrow(() -> new DataNotFoundException("User of registration not found", HttpStatus.NOT_FOUND));
                    String state = hasCheckedIn ? "confirm" : "pending";
                    return GuestListResponse.builder()
                            .appUser(guestUser)
                            .state(state)
                            .build();
                })
                .collect(Collectors.toList());
    }

}
