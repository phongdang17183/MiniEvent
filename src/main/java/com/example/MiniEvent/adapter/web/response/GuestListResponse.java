package com.example.MiniEvent.adapter.web.response;

import com.example.MiniEvent.model.entity.AppUser;
import lombok.Builder;

@Builder
public class GuestListResponse {
    public AppUser appUser;
    public String state;

    public GuestListResponse(AppUser appUser, String state) {
        this.appUser = appUser;
        this.state = state;
    }
}
