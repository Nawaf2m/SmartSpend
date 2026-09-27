package com.example.smartspend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendBudgetStatus(String email, String name, String messageText){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("SmartSpend Budget Status");
        message.setText("Hello " + name + ",\n\n" + messageText + "\n\nSmartSpend");

        javaMailSender.send(message);
    }
}