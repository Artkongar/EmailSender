package com.mailsender.config;

import com.mailsender.scheduling.ScheduledEmailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {

    @Bean("EmailSender")
    @Scope("singleton")
    public ScheduledEmailSender getScheduledTasks(){
        return new ScheduledEmailSender();
    }
}
