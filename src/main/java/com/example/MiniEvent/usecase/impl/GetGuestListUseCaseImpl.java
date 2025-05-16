package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.repository.CheckinRepository;
import com.example.MiniEvent.adapter.repository.EventRepository;
import com.example.MiniEvent.adapter.repository.RegistrationRepository;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.adapter.web.dto.mapper.AppUserMapper;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.adapter.web.exception.NotAllowAccessException;
import com.example.MiniEvent.adapter.web.response.GuestListResponse;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.Registration;
import com.example.MiniEvent.model.entity.StateType;
import com.example.MiniEvent.usecase.inteface.GetGuestListUseCase;
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
    private final AppUserMapper appUserMapper;

    public GetGuestListUseCaseImpl(EventRepository eventRepository, RegistrationRepository registrationRepository, CheckinRepository checkinRepository, UserRepository userRepository, AppUserMapper appUserMapper) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.checkinRepository = checkinRepository;
        this.userRepository = userRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    public List<GuestListResponse> GetGuestListWithState(String eventId, AppUser user) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event not found", HttpStatus.NOT_FOUND));

        if (!event.getCreatedBy().equals(user.getId())) {
            throw new NotAllowAccessException("You are not the owner of event", HttpStatus.FORBIDDEN);
        }

        List<Registration> registrations = registrationRepository.findByEventId(eventId);

        return registrations.stream()
                .map(registration -> {
                    String guestUserId = registration.getUserId();
                    boolean hasCheckedIn = checkinRepository.existsByEventIdAndUserId(eventId, guestUserId);
                    AppUser guestUser = userRepository.findByUid(guestUserId).orElseThrow(() -> new DataNotFoundException("User of registration not found", HttpStatus.NOT_FOUND));
                    return GuestListResponse.builder()
                            .appUserDTO(appUserMapper.fromAppUser(guestUser))
                            .stateType(hasCheckedIn ? StateType.confirm : StateType.pending)
                            .build();
                })
                .collect(Collectors.toList());
    }

}
