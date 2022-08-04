package com.mailsender.controllers;

import com.mailsender.scheduling.ScheduledEmailSender;
import com.mailsender.service.EmailMessageSenderService;
import com.mailsender.service.ServiceContentGenerator;
import com.mailsender.utils.MessageImageCreator;
import com.mailsender.utils.SchedulingSwitcher;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class EmailSenderController {

    private EmailMessageSenderService mailSender;

    @Autowired
    private SchedulingSwitcher schedulingSwitcher;

    @Autowired
    private MessageImageCreator messageImageCreator;

    @Autowired
    public void setMailSender(EmailMessageSenderService mailSender) {
        this.mailSender = mailSender;
    }


    @ResponseBody
    @RequestMapping(value = "/send_message", method = RequestMethod.POST)
    public void sendMail() throws Exception {
        try {
            String formatedDateTime = LocalDateTime.now().format(ScheduledEmailSender.format);
            System.out.println("Start sending");

            messageImageCreator.createPNG();
            mailSender.setMessage(messageImageCreator.uploadAttachment());

            mailSender.setHeader("Love you so Much: " + formatedDateTime);
            mailSender.sendMessage();
            System.out.println("Message was sent");
        } catch (Exception e) {
            System.out.println("Can not send Email");
            throw e;
        }
    }

    @ResponseBody
    @RequestMapping(value = "is_working", method = RequestMethod.POST)
    public Map<String, Boolean> checkIsWorking(@RequestBody JSONObject messageData) {
        String action = (String) messageData.get("action");
        String key = "result";
        Map<String, Boolean> response = schedulingSwitcher.switchEmailSender(action, key);
        return response;
    }
}
