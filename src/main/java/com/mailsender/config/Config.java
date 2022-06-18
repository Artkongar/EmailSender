package com.mailsender.config;

import com.mailsender.scheduling.ScheduledEmailSender;
import com.mailsender.service.MailMessageSenderService;
import com.mailsender.service.JokeGenerator;
import com.mailsender.service.MailSenderREST;
import com.mailsender.utils.SchedulingSwitcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {

    private ScheduledEmailSender scheduledEmailSender;

    @Bean("EmailSender")
    public ScheduledEmailSender getScheduledTasks() {
        if (scheduledEmailSender == null) {
            scheduledEmailSender = new ScheduledEmailSender();
        }
        return scheduledEmailSender;
    }

    @Bean
    public MailMessageSenderService getGmailMessageSenderService() {
        return new MailMessageSenderService();
    }

    @Bean
    public JokeGenerator getJokeGenerator() {
        return new JokeGenerator();
    }

    @Bean
    SchedulingSwitcher getSchedulingSwitcher() {
        return new SchedulingSwitcher();
    }

}
