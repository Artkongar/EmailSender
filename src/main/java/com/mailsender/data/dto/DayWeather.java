package com.mailsender.data.dto;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DayWeather {

    private Map<LocalTime, TimeWeather> timeWeatherData;

    public static class TimeWeather {

        private String weatherDescription;
        private Double temp;
        private Double windSpeed;

        public TimeWeather(String weatherDescription, Double temp, Double windSpeed) {
            this.weatherDescription = weatherDescription;
            this.temp = temp;
            this.windSpeed = windSpeed;
        }

        public String getWeatherDescription() {
            return weatherDescription;
        }

        public Double getTemp() {
            return temp;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }
    }

    public void addTimeData(LocalTime time, TimeWeather timeWeather) {
        if (timeWeatherData == null){
            timeWeatherData = new LinkedHashMap<>();
        }
        timeWeatherData.put(time, timeWeather);
    }

    public Map<LocalTime, TimeWeather> getTimeWeatherData() {
        return timeWeatherData;
    }

}
