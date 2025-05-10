package com.example.MiniEvent.service.impl;

import com.example.MiniEvent.adapter.web.exception.QRCodeException;
import com.example.MiniEvent.model.entity.QRCodeData;
import com.example.MiniEvent.service.inteface.QRCodeGenService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ZXingQRCodeGenService implements QRCodeGenService {

    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    @Value("${qr.secret.key}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    public void getSignInKey() {
        byte[] bytes_access = Base64.getDecoder().decode(secretKey);
        this.key= Keys.hmacShaKeyFor(bytes_access);
    }


    @Override
    public BufferedImage generateQRCodeImage(QRCodeData qrCodeData) {

        String data = Jwts.builder()
                .claim("userId", qrCodeData.getUserId())
                .claim("eventId", qrCodeData.getEventId())
                .issuedAt(new Date())
                .signWith(key)
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

    @Override
    public QRCodeData getData(String token) {
        try {
            Claims claims = parseJwt(token);
            String userId = claims.get("userId", String.class);
            String eventId = claims.get("eventId", String.class);
            return new QRCodeData(userId, eventId);
        }
        catch (Exception e) {
            throw new RuntimeException("Error in get data QR Code");
        }
    }

    private Claims parseJwt(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new QRCodeException("QRCode is not valid", HttpStatus.BAD_REQUEST, e);
        }
    }

}
