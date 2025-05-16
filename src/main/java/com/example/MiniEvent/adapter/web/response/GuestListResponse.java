package com.example.MiniEvent.adapter.web.response;

import com.example.MiniEvent.adapter.web.dto.AppUserDTO;
import com.example.MiniEvent.model.entity.StateType;
import lombok.Builder;

@Builder
public class GuestListResponse {
    public AppUserDTO appUserDTO;
    public StateType stateType;

    public GuestListResponse(AppUserDTO appUserDTO, StateType stateType) {
        this.appUserDTO = appUserDTO;
        this.stateType = stateType;
    }
}
