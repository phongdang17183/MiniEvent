package com.example.MiniEvent.service.inteface;

import com.example.MiniEvent.model.entity.EmailDetail;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    Void sendEmail(EmailDetail emailDetail);
    Void sendEmailWithFile(EmailService emailService, MultipartFile file);
}
