package com.example.webadvanced_backend.models;

import com.example.webadvanced_backend.services.EmailSenderService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRunnable implements Runnable{
    String title;
    String body;
    String toEmail;

    @Autowired
    EmailSenderService emailSenderService;

    @Override
    public void run() {
        emailSenderService.sendEmail(this.toEmail, this.title, this.body);
    }
}
