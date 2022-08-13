package com.mailsender.controllers;

import com.mailsender.scheduling.ScheduledEmailSender;
import com.mailsender.service.EmailMessageSenderService;
import com.mailsender.utils.MessageImageCreator;
import com.mailsender.utils.SchedulingSwitcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(EmailSenderController.class);

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
    public void sendMail() {
        try {
            String formatedDateTime = LocalDateTime.now().format(ScheduledEmailSender.format);
            LOGGER.info("Start sending");
            String uploadedImageId = messageImageCreator.uploadImageContent();
            mailSender.setMessage(uploadedImageId);

            mailSender.setHeader("Love you so Much: " + formatedDateTime);
            mailSender.sendMessage();
            LOGGER.info("Message was sent");
        } catch (Exception e) {
            LOGGER.error("Can not send Email");
            LOGGER.error(e.getMessage(), e);
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
