package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Registration;

public interface UpdateUserInEvent {
    Registration addUserInEvent(String ownerId, String eventId, String phoneNumber);
    Registration removeUserInEvent(String ownerId, String eventId, String phoneNumber);
}
