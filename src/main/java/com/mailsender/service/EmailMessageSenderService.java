package com.mailsender.service;

import com.mailsender.utils.MessageImageCreator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public class EmailMessageSenderService {

    @Value("${from_login}")
    private String fromLogin;

    @Value("${to_login}")
    private String toLogin;

    @Value("${password}")
    private String password;

    private String header;
    private String message;

    public int sendMessage() throws Exception {
        int attempts = 10;
        int attemptsCount = 0;
        while (true) {
            try {
                return send();
            } catch (Exception e){
                attemptsCount ++;
                System.out.println("Can not send message. Trying again...(" + attemptsCount + ")");
                if (attemptsCount > attempts){
                    throw e;
                }
            }
        }
    }

    private int send() throws Exception {
        String url = "https://api.nylas.com/send";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Authorization", "Bearer " + password);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("cache-control", "no-cache");
        httpPost.setHeader("charset", "utf-8");

        JSONObject bodyRoot = new JSONObject();
        bodyRoot.put("subject", header);

        JSONArray toList = new JSONArray();
        JSONObject toObject = new JSONObject();
        toObject.put("email", toLogin);
        toObject.put("name", "My Darling");
        toList.add(toObject);
        bodyRoot.put("to", toList);

        JSONArray fromList = new JSONArray();
        JSONObject fromObject = new JSONObject();
        fromObject.put("email", fromLogin);
        fromObject.put("name", "Your husband");
        fromList.add(fromObject);
        bodyRoot.put("from", toList);

        JSONArray attachIDs = new JSONArray();
        attachIDs.add(message);
        bodyRoot.put("file_ids", attachIDs);

        StringEntity entity = new StringEntity(bodyRoot.toJSONString());
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        client.close();
        return statusCode;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
