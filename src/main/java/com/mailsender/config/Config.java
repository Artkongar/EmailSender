package com.mailsender.config;

import com.mailsender.controllers.EmailSenderController;
import com.mailsender.controllers.MainController;
import com.mailsender.scheduling.ScheduledEmailSender;
import com.mailsender.service.GmailMessageSenderService;
import com.mailsender.service.JokeGenerator;
import com.mailsender.utils.SchedulingSwitcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {

    private ScheduledEmailSender scheduledEmailSender;

    @Bean("EmailSender")
    public ScheduledEmailSender getScheduledTasks(){
        if (scheduledEmailSender == null){
            scheduledEmailSender = new ScheduledEmailSender();
        }
        return scheduledEmailSender;
    }

    @Bean
    public GmailMessageSenderService getGmailMessageSenderService(){
        return new GmailMessageSenderService();
    }

    @Bean
    public JokeGenerator getJokeGenerator(){
        return new JokeGenerator();
    }

    @Bean
    SchedulingSwitcher getSchedulingSwitcher(){
        return new SchedulingSwitcher();
    }

}
