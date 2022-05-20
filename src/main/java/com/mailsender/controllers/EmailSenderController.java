package com.mailsender.controllers;

import com.mailsender.service.GmailMessageSenderService;
import com.mailsender.service.JokeGenerator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class EmailSenderController {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private JokeGenerator jokeGenerator;
    private GmailMessageSenderService mailSender;

    @Autowired
    public void setMailSender(GmailMessageSenderService mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setJokeGenerator(JokeGenerator jokeGenerator) {
        this.jokeGenerator = jokeGenerator;
    }

    @RequestMapping(value = "/send_message", method = RequestMethod.POST)
    public String sendMail(@RequestBody JSONObject messageData) {
        String jokeType = (String) messageData.get("jokeType");
        try {
            String translatedJoke = jokeGenerator.getTranslatedJoke();
            String russiianJoke = jokeGenerator.getRussianJoke(jokeType);
            String formatedDateTime = LocalDateTime.now().format(format);

            StringBuffer bf = new StringBuffer();
            bf.append(translatedJoke);
            bf.append("\n___________________________________________________________________\n");
            bf.append(russiianJoke);
            mailSender.setHeader("Love you so much\n" + formatedDateTime);
            mailSender.setMessage(bf.toString());
            mailSender.sendMessage();
        } catch (Exception e) {
            System.out.println("Залогировать нормально ошибку");
        }
        return "index";
    }
}
