package com.mailsender.utils.service_handler.weather;

import com.mailsender.data.Months;
import com.mailsender.data.dto.DayWeather;
import com.mailsender.service.ServiceContentGenerator;
import com.mailsender.utils.service_handler.ServiceContent;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;


public class FiveDayWeatherImpl implements ServiceContent {

    private Map<LocalDate, DayWeather> dayWeatherData;

    @Value("${max_days_count}")
    private int maxDaysCount = 2;

    @Override
    public String getHTMLRows() throws Exception {
        StringBuffer bf = new StringBuffer("" +
                "<div>" +
                "   <b>" +
                "       Прогноз Погоды" +
                "   </b>" +
                "</div>" +
                "<table>" +
                " <tr>\n" +
                "        <td/>\n" +
                "        <td>\n" +
                "            Время\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            Описание\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            <div>" +
                "               Температура," +
                "            </div>" +
                "            <div>" +
                "               °C" +
                "            </div>" +
                "        </td>\n" +
                "        <td>\n" +
                "            <div>" +
                "               Скорость ветра," +
                "            </div>" +
                "            <div>" +
                "               м/с" +
                "            </div>" +
                "        </td>\n" +
                "    </tr>"
        );
        int dayCounter = 0;
        for (LocalDate date : this.dayWeatherData.keySet()) {
            DayWeather dayWeatherData = this.dayWeatherData.get(date);
            Map<LocalTime, DayWeather.TimeWeather> timeWeatherMap = dayWeatherData.getTimeWeatherData();

            boolean isFirst = true;
            for (LocalTime time : timeWeatherMap.keySet()) {
                DayWeather.TimeWeather timeWeather = timeWeatherMap.get(time);
                String weatherDescription = timeWeather.getWeatherDescription();
                Double windSpeed = timeWeather.getWindSpeed();
                Double temperature = timeWeather.getTemp();

                bf.append("<tr>\n");
                int rowspanValue = 1;
                if (isFirst) {
                    rowspanValue = timeWeatherMap.size();
                    bf.append("" +
                            "    <td rowspan=\"" + rowspanValue + "\">\n" +
                            "        " + convertDateToHtml(date) + "\n" +
                            "    </td>\n");
                    isFirst = false;
                }
                bf.append("" +
                        "    <td>\n" +
                        "        " + time + "\n" +
                        "    </td>\n" +
                        "    <td>\n" +
                        "        " + weatherDescription + "\n" +
                        "    </td>\n" +
                        "    <td>\n" +
                        "        " + temperature + "\n" +
                        "    </td>\n" +
                        "    <td>\n" +
                        "        " + windSpeed + "\n" +
                        "    </td>\n");
                bf.append("</tr>");
            }
            dayCounter ++;
            if (dayCounter >= maxDaysCount){
                break;
            }
        }
        bf.append("</table>");
        return bf.toString();
    }

    private String convertDateToHtml(LocalDate date) throws Exception {
        Months months = Months.getByMonthNumber(date.getMonth().getValue());
        String ruName = months.getRuName();
        int dayNumber = date.getDayOfMonth();
        return "<div style=\"text-align: center\">" +
                    dayNumber +
                    "<br></br>" +
                    ruName +
                "</div>";
    }

    public void setDayWeatherData(Map<LocalDate, DayWeather> dayWeatherData) {
        this.dayWeatherData = dayWeatherData;
    }

}
