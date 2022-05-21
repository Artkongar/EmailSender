package com.mailsender.scheduling;

import com.mailsender.data.ServiceStatus;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledEmailSender {

    private int v = 0;

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        if (ServiceStatus.getInstance().isWorking()){
            System.out.println();
            v += 1;
            System.out.println(v);
        }
    }
}
