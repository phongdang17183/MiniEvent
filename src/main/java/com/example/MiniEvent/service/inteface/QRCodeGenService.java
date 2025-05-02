package com.example.MiniEvent.service.inteface;

import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.QRCodeData;

import java.awt.image.BufferedImage;

public interface QRCodeGenService {
    public BufferedImage generateQRCodeImage(QRCodeData qrCodeData);
}
