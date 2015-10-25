package hedge.johnny.HedgeObject;

/**
 * Created by 환규 on 2015-05-30.
 */

import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {

    private List<DayForecast> daysForecast = new ArrayList<DayForecast>();

    public void addForecast(DayForecast forecast) {
        daysForecast.add(forecast);
        System.out.println("Add forecast [" + forecast + "]");
    }

    public DayForecast getForecast(int dayNum) {
        return daysForecast.get(dayNum);
    }
}
