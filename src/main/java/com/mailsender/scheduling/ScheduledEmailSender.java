package com.mailsender.scheduling;

import com.mailsender.data.ServiceStatus;
import com.mailsender.service.MailMessageSenderService;
import com.mailsender.service.JokeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ScheduledEmailSender {

    private JokeGenerator jokeGenerator;
    private MailMessageSenderService mailSender;
    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private Random rnd = new Random();

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    public void setJokeGenerator(JokeGenerator jokeGenerator) {
        this.jokeGenerator = jokeGenerator;
    }

    @Autowired
    public void setMailSender(MailMessageSenderService mailSender) {
        this.mailSender = mailSender;
    }

    private int messageNumber = 0;

    @Scheduled(cron = "0 0 */2 * * ?")
    public void reportCurrentTime() {
        if (ServiceStatus.getInstance().isWorking()) {
            messageNumber ++;
            try {
                String jokeType = String.valueOf(rnd.nextInt(17));
                String translatedJoke = jokeGenerator.getTranslatedJoke();
                String russianJoke = jokeGenerator.getRussianJoke(jokeType);
                String formatedDateTime = LocalDateTime.now().format(format);

                StringBuffer bf = new StringBuffer();
                bf.append(translatedJoke);
                bf.append("\n___________________________________________________________________\n");
                bf.append(russianJoke);
                mailSender.setHeader("Love you so much\n" + formatedDateTime);
                mailSender.setMessage(bf.toString());
                System.out.println("Sending message №" + messageNumber + " ...");
                mailSender.sendMessage();
                System.out.println("Message №" + messageNumber + " was sent");
            } catch (Exception e) {
                System.out.println("Didn't sent message №" + messageNumber);
            }
        }
    }
}
