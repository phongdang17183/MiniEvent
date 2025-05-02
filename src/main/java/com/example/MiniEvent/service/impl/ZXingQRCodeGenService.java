package com.example.MiniEvent.service.impl;

import com.example.MiniEvent.model.entity.QRCodeData;
import com.example.MiniEvent.service.inteface.QRCodeGenService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ZXingQRCodeGenService implements QRCodeGenService {

    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    @Value("${qr.secret.key}")
    private String secretKey;

    @Override
    public BufferedImage generateQRCodeImage(QRCodeData qrCodeData) {

        String data = Jwts.builder()
                .claim("userId", qrCodeData.getUserId())
                .claim("eventId", qrCodeData.getEventId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 300000))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
                .compact();
        try {
            BitMatrix bitMatrix =
                qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        }
        catch (Exception e) {
            throw new RuntimeException("Error in create QR Code");
        }
    }
}
