package com.mailsender.service;

import com.mailsender.data.joke.JokeImpl;
import com.mailsender.data.joke.RussianJokeImpl;
import com.mailsender.data.joke.TranslatedJokeImpl;
import com.mailsender.utils.StringEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class JokeGenerator {

    private JSONParser parser = new JSONParser();
    Random rnd = new Random();

    private InputStream createGetRequest(String endpoint) throws IOException, ParseException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(endpoint);
        HttpResponse httpresponse = httpclient.execute(httpget);
        return httpresponse.getEntity().getContent();
    }

    private String translateAPI(String req) throws IOException, ParseException {
        String url = "https://api.mymemory.translated.net/get?";
        String params = "q=" + URLEncoder.encode(req, "UTF-8") + "&langpair=" + URLEncoder.encode("en|ru");
        JSONObject answer = (JSONObject) parser.parse(new InputStreamReader(createGetRequest(url + params), StandardCharsets.UTF_8));

        JSONObject responseData = (JSONObject) answer.get("responseData");
        String translatedText = (String) responseData.get("translatedText");

        return translatedText;
    }

    public JokeImpl getRussianJoke() throws IOException, ParseException {
        int attempts = 10;
        int attempt = 0;

        int[] jokeTypeNumbers = {1, 2, 3, 4, 5, 8, 11, 12, 13, 14, 15, 18};
        int jokeType = jokeTypeNumbers[rnd.nextInt(jokeTypeNumbers.length)];
        RussianJokeImpl russianJoke = new RussianJokeImpl();

        while (true) {
            attempt++;
            try {
                String url = "http://rzhunemogu.ru/RandJSON.aspx?CType=" + jokeType;
                InputStream in = createGetRequest(url);

                JSONObject root = (JSONObject) parser.parse(new InputStreamReader(in, Charset.forName("windows-1251")));
                String result = (String) root.get("content");
                russianJoke.setJokeText(StringEncoder.encodeUTF8(result));
                return russianJoke;
            } catch (Exception e) {
                if (attempt >= attempts) {
                    throw e;
                }
            }
        }
    }

    public JokeImpl getTranslatedJoke() throws Exception {
        String url = "https://v2.jokeapi.dev/joke/Any";
        JSONObject response = (JSONObject) parser.parse(new InputStreamReader(createGetRequest(url), StandardCharsets.UTF_8));
        String category = (String) response.get("category");
        String joke = (String) response.get("joke");

        TranslatedJokeImpl translatedJoke = new TranslatedJokeImpl();
        translatedJoke.setSubject(category);
        translatedJoke.setSubjectRu(translateAPI(category));
        if (joke != null) {
            translatedJoke.setHasPunchline(false);
            translatedJoke.setJokeRu(translateAPI(joke));
            translatedJoke.setJoke(joke);
        } else {
            String setup = (String) response.get("setup");
            String delivery = (String) response.get("delivery");
            translatedJoke.setSetup(setup);
            translatedJoke.setPunchline(delivery);
            translatedJoke.setSetupRu(translateAPI(setup));
            translatedJoke.setPunchlineRu(translateAPI(delivery));
        }

        return translatedJoke;
    }
}
