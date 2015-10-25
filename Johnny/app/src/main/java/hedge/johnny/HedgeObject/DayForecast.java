package hedge.johnny.HedgeObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayForecast {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static SimpleDateFormat sdfd = new SimpleDateFormat("d");
    private static SimpleDateFormat sdfm = new SimpleDateFormat("M");
    private static SimpleDateFormat sdft = new SimpleDateFormat("H");
    public Weather weather = new Weather();
    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;

    public class ForecastTemp {
        public float day;
        public float min;
        public float max;
        public float night;
        public float eve;
        public float morning;
    }

    public String getStringDate() {
        String m = sdfm.format(new Date(timestamp*1000));
        String d = sdfd.format(new Date(timestamp*1000));

        String t = m + "월" + d + "일";
        return t;
    }

    public int getintDate() {
        String m = sdfm.format(new Date(timestamp*1000));
        String d = sdfd.format(new Date(timestamp*1000));

        return Integer.parseInt(m)*100 + Integer.parseInt(d);
    }

    public int getTimeData()
    {
        String t = sdft.format(new Date(timestamp*1000));
        return Integer.parseInt(t);
    }

    public Date getDate() {
        return (new Date(timestamp*1000));
    }
}
