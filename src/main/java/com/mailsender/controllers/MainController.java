package com.mailsender.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewIndex(){
        return "index.html";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String viewTest(){
        return "login.html";
    }
}
