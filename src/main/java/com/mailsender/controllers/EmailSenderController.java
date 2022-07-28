package com.mailsender.controllers;

import com.mailsender.data.TranslatedJoke;
import com.mailsender.scheduling.ScheduledEmailSender;
import com.mailsender.service.EmailMessageSenderService;
import com.mailsender.service.JokeGenerator;
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

    private JokeGenerator jokeGenerator;
    private EmailMessageSenderService mailSender;

    @Autowired
    private SchedulingSwitcher schedulingSwitcher;

    @Autowired
    private MessageImageCreator messageImageCreator;

    @Autowired
    public void setMailSender(EmailMessageSenderService mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setJokeGenerator(JokeGenerator jokeGenerator) {
        this.jokeGenerator = jokeGenerator;
    }

    @ResponseBody
    @RequestMapping(value = "/send_message", method = RequestMethod.POST)
    public boolean sendMail(@RequestBody JSONObject messageData) {
        boolean response;
        String jokeType = (String) messageData.get("jokeType");
        try {
            System.out.println("Start sending");
            TranslatedJoke translatedJoke = jokeGenerator.getTranslatedJoke();
            String russianJoke = jokeGenerator.getRussianJoke(jokeType);
            String formatedDateTime = LocalDateTime.now().format(ScheduledEmailSender.format);

            if (translatedJoke.isHasPunchline()) {
                messageImageCreator.createPNG(
                        translatedJoke, russianJoke
                );
                mailSender.setMessage(messageImageCreator.uploadAttachment());
            }

            mailSender.setHeader(formatedDateTime);
            mailSender.sendMessage();
            System.out.println("Message was sent");
            response = true;
        } catch (Exception e) {
            System.out.println("Can not send Email");
            System.out.println(e.getMessage());
            response = false;
        }
        return response;
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
