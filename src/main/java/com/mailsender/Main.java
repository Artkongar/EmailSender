package com.mailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Main {

    public static void main(String[] args){
        SpringApplication springMvcApplication = new SpringApplication(Main.class);
        springMvcApplication.run(args);
    }
}