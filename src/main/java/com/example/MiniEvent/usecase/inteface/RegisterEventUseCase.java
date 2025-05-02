package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.Registration;

import java.awt.image.BufferedImage;

public interface RegisterEventUseCase {
    BufferedImage registerEvent(String eventId, String userId);
}
