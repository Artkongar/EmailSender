package com.mailsender.config;

import com.mailsender.scheduling.ScheduledEmailSender;
import com.mailsender.service.EmailMessageSenderService;
import com.mailsender.service.ServiceContentGenerator;
import com.mailsender.utils.MessageImageCreator;
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
    public EmailMessageSenderService getGmailMessageSenderService() {
        return new EmailMessageSenderService();
    }

    @Bean
    public ServiceContentGenerator getJokeGenerator() {
        return new ServiceContentGenerator();
    }

    @Bean
    public SchedulingSwitcher getSchedulingSwitcher() {
        return new SchedulingSwitcher();
    }

    @Bean
    public MessageImageCreator getMessageImageCreator(){
        return new MessageImageCreator();
    }

}
