package com.mailsender.service;

import com.mailsender.utils.service_handler.ServiceContent;
import com.mailsender.data.dto.DayWeather;
import com.mailsender.utils.service_handler.joke.RussianJokeImpl;
import com.mailsender.utils.service_handler.joke.TranslatedJokeImpl;
import com.mailsender.utils.service_handler.weather.FiveDayWeatherImpl;
import com.mailsender.utils.MessageImageCreator;
import com.mailsender.utils.StringEncoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;


public class ServiceContentGenerator {

    @Value("${weather_apikey}")
    private String weatherApiKey;

    @Value("${city_lat}")
    private String weatherLat;

    @Value("${city_lon}")
    private String weatherLon;

    private JSONParser parser = new JSONParser();
    private Random rnd = new Random();
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String translateAPI(String req) throws IOException, ParseException {
        String url = "https://api.mymemory.translated.net/get?";
        String params = "q=" + URLEncoder.encode(req, "UTF-8") + "&langpair=" + URLEncoder.encode("en|ru");

        JSONObject answer = (JSONObject) parser.parse(new InputStreamReader(MessageImageCreator.createGetRequest(url + params), StandardCharsets.UTF_8));
        String translatedText;
        Long responseStatus = (Long) answer.get("responseStatus");
        if (responseStatus == 200){
            JSONObject responseData = (JSONObject) answer.get("responseData");
            translatedText = (String) responseData.get("translatedText");
        } else {
            translatedText = "Перевод в данное время недоступен. Обратитесь к администрации";
        }

        return translatedText;
    }

    public ServiceContent getRussianJoke() throws IOException, ParseException {
        int attempts = 10;
        int attempt = 0;

        int[] jokeTypeNumbers = {1, 2, 3, 4, 5, 8};
        int jokeType = jokeTypeNumbers[rnd.nextInt(jokeTypeNumbers.length)];
        RussianJokeImpl russianJoke = new RussianJokeImpl();

        while (true) {
            attempt++;
            try {
                String url = "http://rzhunemogu.ru/RandJSON.aspx?CType=" + jokeType;
                InputStream in = MessageImageCreator.createGetRequest(url);

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

    public ServiceContent getTranslatedJoke() throws Exception {
        String url = "https://v2.jokeapi.dev/joke/Any";
        JSONObject response = (JSONObject) parser.parse(new InputStreamReader(MessageImageCreator.createGetRequest(url), StandardCharsets.UTF_8));
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

    private static Double convertNumberToDouble(Object number) {
        Double doubleValue;
        try {
            doubleValue = (Double) number;
        } catch (ClassCastException e) {
            Long longTemp = (Long) number;
            doubleValue = longTemp.doubleValue();
        }
        return doubleValue;
    }

    public ServiceContent getFiveDayWeatherData() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + weatherLat + "&lon=" + weatherLon + "&lang=ru&appid=" + weatherApiKey;
        JSONObject rootObject = (JSONObject) parser.parse(new InputStreamReader(MessageImageCreator.createGetRequest(url)));
        JSONArray rootList = (JSONArray) rootObject.get("list");

        Map<LocalDate, DayWeather> dayWeatherMap = new LinkedHashMap<>();
        for (Object i : rootList) {
            JSONObject hourData = (JSONObject) i;
            String date = (String) hourData.get("dt_txt");
            JSONObject main = (JSONObject) hourData.get("main");
            Double temp = Double.parseDouble(String.format("%.3f", convertNumberToDouble(main.get("temp")) - 273.15).replace(",", "."));
            JSONObject wind = (JSONObject) hourData.get("wind");
            Double speed = convertNumberToDouble(wind.get("speed"));
            JSONArray weatherData = (JSONArray) hourData.get("weather");
            JSONObject weatherDataObject = (JSONObject) weatherData.get(0);
            String weatherDescription = (String) weatherDataObject.get("description");

            LocalDateTime formatDateTime = LocalDateTime.parse(date, formatter);
            LocalTime localTime = formatDateTime.toLocalTime();
            LocalDate localDate = formatDateTime.toLocalDate();

            DayWeather dayWeather;
            if (dayWeatherMap.get(localDate) == null) {
                dayWeather = new DayWeather();
            } else {
                dayWeather = dayWeatherMap.get(localDate);
            }
            dayWeather.addTimeData(localTime, new DayWeather.TimeWeather(weatherDescription, temp, speed));
            dayWeatherMap.put(localDate, dayWeather);
        }

        FiveDayWeatherImpl fiveDayWeather = new FiveDayWeatherImpl();
        fiveDayWeather.setDayWeatherData(dayWeatherMap);
        return fiveDayWeather;
    }
}
