package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.AppUser;

import java.util.List;

public interface GetGuestListUseCase {
    List<?> GetGuestListWithState(String eventID, AppUser user);
}
