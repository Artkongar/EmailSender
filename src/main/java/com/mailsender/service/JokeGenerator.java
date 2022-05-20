package com.mailsender.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class JokeGenerator {

    private JSONParser parser = new JSONParser();;

    private InputStream createGetRequest(String endpoint) throws IOException, ParseException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(endpoint);
        HttpResponse httpresponse = httpclient.execute(httpget);

        return httpresponse.getEntity().getContent();//, StandardCharsets.UTF_8);
    }

    private String translateAPI(String req) throws IOException, ParseException {
        String url = "https://api.mymemory.translated.net/get?";
        String params = "q=" +  URLEncoder.encode(req,"UTF-8") + "&langpair="+ URLEncoder.encode("en|ru");
        JSONObject answer = (JSONObject) parser.parse(new InputStreamReader(createGetRequest(url + params), StandardCharsets.UTF_8));

        JSONObject responseData = (JSONObject) answer.get("responseData");
        String translatedText = (String) responseData.get("translatedText");

        return translatedText;
    }

    public String getRussianJoke(String jokeType) throws IOException, ParseException, ParserConfigurationException, SAXException, XPathExpressionException {
        String url = "http://rzhunemogu.ru/Rand.aspx?CType=" + jokeType;
        InputStream in = createGetRequest(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("windows-1251")));
        StringBuffer bf = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null){
            bf.append(line);
            bf.append("\n");
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(bf.toString().getBytes()));
        NodeList list = doc.getElementsByTagName("content");
        return list.item(0).getTextContent();
    }

    public String getTranslatedJoke() throws IOException, ParseException {
        String url = "https://v2.jokeapi.dev/joke/Any";
        JSONObject response = (JSONObject) parser.parse(new InputStreamReader(createGetRequest(url), StandardCharsets.UTF_8));
        String category = (String) response.get("category");
        String joke = (String) response.get("joke");
        StringBuffer bf = new StringBuffer();
        bf.append(category + " | " + translateAPI(category));
        bf.append("\n");
        if (joke != null){
            bf.append(joke);
            bf.append("\n");
            bf.append(translateAPI(joke));
        } else {
            String setup = (String) response.get("setup");
            String delivery = (String) response.get("delivery");
            bf.append("Сатравка: " + setup);
            bf.append("\n");
            bf.append("Панчлайн: " + delivery);
            bf.append("\n");
            bf.append("Сатравка: " + translateAPI(setup));
            bf.append("\n");
            bf.append("Панчлайн: " + translateAPI(delivery));
        }
        return bf.toString();
    }
}
