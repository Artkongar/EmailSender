package com.mailsender.utils;

import com.mailsender.data.ServiceStatus;
import com.mailsender.scheduling.ScheduledEmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SchedulingSwitcher {

    private final String scheduledTaskBeanName = "EmailSender";
    private ScheduledAnnotationBeanPostProcessor postProcessor;
    @Autowired
    private ScheduledEmailSender scheduledTask;

    @Autowired
    private void setPostProcessor(ScheduledAnnotationBeanPostProcessor postProcessor){
        this.postProcessor = postProcessor;
    }

    public Map<String, Boolean> switchEmailSender(String action, String key){
        Map<String, Boolean> response = new HashMap<>();
        if ("start".equals(action)) {
            if (!ServiceStatus.getInstance().isWorking()) {
                ServiceStatus.getInstance().setWorking(true);
                postProcessor.postProcessAfterInitialization(scheduledTask, scheduledTaskBeanName);
            }
            response.put(key, true);
        } else if ("stop".equals(action)) {
            if (ServiceStatus.getInstance().isWorking()) {
                ServiceStatus.getInstance().setWorking(false);
                postProcessor.postProcessBeforeDestruction(scheduledTask, scheduledTaskBeanName);
            }
            response.put(key, false);
        } else if ("status".equals(action)){
            if (!ServiceStatus.getInstance().isWorking()){
                postProcessor.postProcessBeforeDestruction(scheduledTask, scheduledTaskBeanName);
            }
            response.put(key, ServiceStatus.getInstance().isWorking());
        }
        return response;
    }
}
