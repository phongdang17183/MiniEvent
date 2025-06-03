package com.example.MiniEvent.service.inteface;

import com.example.MiniEvent.model.entity.QRCodeData;

import java.awt.image.BufferedImage;

public interface QRCodeGenService {
    BufferedImage generateQRCodeImage(QRCodeData qrCodeData);
    QRCodeData getData(String token);

}
