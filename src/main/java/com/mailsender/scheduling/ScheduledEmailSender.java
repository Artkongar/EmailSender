package com.mailsender.scheduling;

import com.mailsender.controllers.EmailSenderController;
import com.mailsender.data.ServiceStatus;
import com.mailsender.service.EmailMessageSenderService;
import com.mailsender.utils.MessageImageCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduledEmailSender {

    private static final Logger LOGGER = LogManager.getLogger(ScheduledEmailSender.class);

    private EmailMessageSenderService mailSender;
    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Autowired
    private MessageImageCreator messageImageCreator;

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    public void setMailSender(EmailMessageSenderService mailSender) {
        this.mailSender = mailSender;
    }

    private int messageNumber = 0;

    @Scheduled(cron = "0 0 */2 * * ?")
    public void reportCurrentTime() {
        if (ServiceStatus.getInstance().isWorking()) {
            messageNumber++;
            try {
                String formatedDateTime = LocalDateTime.now().format(format);

                String uploadedImageId = messageImageCreator.uploadImageContent();
                mailSender.setMessage(uploadedImageId);

                mailSender.setHeader(formatedDateTime);
                LOGGER.info("Sending message №" + messageNumber + " ...");
                mailSender.sendMessage();
                LOGGER.info("Message №" + messageNumber + " was sent");
            } catch (Exception e) {
                LOGGER.error("Didn't sent message №" + messageNumber);
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
