package com.votemanager.app.services;

import com.votemanager.app.models.EmailModel;
import com.votemanager.app.models.StatusEmailEnum;
import com.votemanager.app.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public EmailModel sendEmail(EmailModel emailModel) {

        try {

            emailModel.setSendDateEmail(LocalDateTime.now());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());

            javaMailSender.send(message);

            emailModel.setStatusEmail(StatusEmailEnum.SENT);

        } catch (MailException e){
            emailModel.setStatusEmail(StatusEmailEnum.ERROR);

        } finally {
            return emailRepository.save(emailModel);
        }
    }
}
