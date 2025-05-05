package com.example.MiniEvent.service.impl;

import com.example.MiniEvent.adapter.web.exception.EmailException;
import com.example.MiniEvent.config.rabbitmq.RabbitMQConfig;
import com.example.MiniEvent.model.entity.EmailDetail;
import com.example.MiniEvent.service.inteface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MailPublisherService implements EmailService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Void sendEmail(EmailDetail emailDetail) {
        try {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                emailDetail);
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
