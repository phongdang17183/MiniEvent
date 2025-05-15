package com.example.MiniEvent.service.impl;

import com.example.MiniEvent.adapter.web.exception.EmailException;
import com.example.MiniEvent.model.entity.EmailDetail;
import com.example.MiniEvent.service.inteface.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("appPasswordMailService")
@RequiredArgsConstructor
@Slf4j
public class AppPasswordMailService implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Override
    public Void sendEmail(EmailDetail emailDetail) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDetail.getTo());
            message.setSubject(emailDetail.getSubject());
            message.setText(emailDetail.getMsgBody());
            message.setFrom(emailUsername);
            javaMailSender.send(message);
            return null;
        }
        catch (Exception e) {
            throw new EmailException("Error while sending mail", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Void sendEmailWithFile(EmailService emailService, MultipartFile file) {
        return null;
    }
}
