package com.mailsender.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
public class GmailMessageSenderService {

    @Value("${from_login}")
    private String fromLogin;

    @Value("${to_login}")
    private String toLogin;

    @Value("${password}")
    private String password;

    private String header;
    private String message;

    public void sendMessage() {
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromLogin, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromLogin));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toLogin));

            message.setSubject(header);
            message.setText(this.message);

            Transport.send(message);
        } catch (MessagingException e){
            System.out.println(e.getMessage());
        }
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
