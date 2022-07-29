package com.mailsender.scheduling;

import com.mailsender.data.ServiceStatus;
import com.mailsender.data.joke.Joke;
import com.mailsender.data.joke.TranslatedJoke;
import com.mailsender.service.EmailMessageSenderService;
import com.mailsender.service.JokeGenerator;
import com.mailsender.utils.MessageImageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ScheduledEmailSender {

    private JokeGenerator jokeGenerator;
    private EmailMessageSenderService mailSender;
    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Autowired
    private MessageImageCreator messageImageCreator;

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    public void setJokeGenerator(JokeGenerator jokeGenerator) {
        this.jokeGenerator = jokeGenerator;
    }

    @Autowired
    public void setMailSender(EmailMessageSenderService mailSender) {
        this.mailSender = mailSender;
    }

    private int messageNumber = 0;

    @Scheduled(fixedDelay = 10000) // cron = "0 0 */2 * * ?"
    public void reportCurrentTime() {
        if (ServiceStatus.getInstance().isWorking()) {
            messageNumber ++;
            try {
                Joke translatedJoke = jokeGenerator.getTranslatedJoke();
                Joke russianJoke = jokeGenerator.getRussianJoke();
                String formatedDateTime = LocalDateTime.now().format(format);

                messageImageCreator.createPNG(
                        translatedJoke, russianJoke
                );
                mailSender.setMessage(messageImageCreator.uploadAttachment());

                mailSender.setHeader(formatedDateTime);
                System.out.println("Sending message №" + messageNumber + " ...");
                mailSender.sendMessage();
                System.out.println("Message №" + messageNumber + " was sent");
            } catch (Exception e) {
                System.out.println("Didn't sent message №" + messageNumber);
            }
        }
    }
}
