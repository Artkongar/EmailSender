package com.mailsender.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MailSenderREST {

    private String fromAddress;
    private String fromName;
    private String toAddress;
    private String body;
    private String subject;

    private JSONParser parser = new JSONParser();

    public void send() throws IOException, ParseException {
        FileOutputStream out = new FileOutputStream("joke.txt");
        out.write(body.getBytes(StandardCharsets.UTF_8));
        out.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("test.txt")));
        String newBody = "";
        String line;
        while ((line = br.readLine()) != null){
            newBody += URLEncoder.encode(line,"UTF-8") + "%0A";
        }

        String url = "https://api.unisender.com/ru/api/sendEmail?format=json&lang=ru" +
                "&" +
                "api_key=6zpp6e9hwu1ggp6eco1fj8q6yxyuxbgi66smp5ue" +
                "&" +
                "email=" + toAddress +
                "&" +
                "sender_name=" + fromName +
                "&" +
                "sender_email=" + fromAddress +
                "&" +
                "subject=" + subject +
                "&" +
                "body=" + newBody +
                "&" +
                "list_id=1";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        System.out.println(httpget.getURI());
        HttpResponse httpresponse = httpclient.execute(httpget);

        JSONObject root = (JSONObject) parser.parse(new InputStreamReader(httpresponse.getEntity().getContent()));
        System.out.println(root);
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
