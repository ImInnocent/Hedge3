package hedge.johnny.HedgeObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hedge.johnny.Activity.TimeoutActivity;
import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;

/**
 * Created by EDGE01 on 2015-08-14.
 */
public class HedgeAlarmService extends Service {
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getSharedPreferences("HedgeMembers", 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStart", "on");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 현재 Hour
        SimpleDateFormat sdfHour = new SimpleDateFormat("H");
        String strHour = sdfHour.format(date);
        int intHour = Integer.parseInt(strHour);
        // 현재 Min
        SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
        String strMinute = sdfMinute.format(date);

        // 방해 금지 시간
        String prefOnoff = pref.getString("permission_onoff", "null");
        if(prefOnoff.equals("on"))
        {
            String prefStart = pref.getString("permission_start", "null");      // AM 09:00
            if(!prefStart.equals("null")) {
                String[] prefStarr = prefStart.split(" ");
                String[] prefStartTime = prefStarr[1].split(":");
                float prefStartHour = Float.parseFloat(prefStartTime[0]);
                float prefStartMin = Float.parseFloat(prefStartTime[1]);
                prefStartHour += prefStartMin / 60;
                if (prefStarr[0].equals("PM"))
                    prefStartHour += 12;
                String prefEnd = pref.getString("permission_end", "null");      // AM 09:00
                String[] prefEndd = prefEnd.split(" ");
                String[] prefEndTime = prefStarr[1].split(":");
                float prefEndHour = Float.parseFloat(prefEndTime[0]);
                float prefEndMin = Float.parseFloat(prefEndTime[1]);
                prefEndHour += prefEndMin / 60;
                if (prefEndd[0].equals("PM"))
                    prefEndHour += 12;

                // Check
                if (prefStartHour > prefEndHour) {
                    if ((prefStartHour < intHour) || (prefEndHour > intHour))
                        return START_NOT_STICKY;
                } else {
                    if ((prefStartHour < intHour) && (prefEndHour > intHour))
                        return START_NOT_STICKY;
                }
            }
        }

        //db확인
        String alarmid = intent.getExtras().getString("db_id");
        if(alarmid == null) return START_NOT_STICKY;

       // HedgeHttpClient.GetInstance().GetAlarmWithAlarmID(id, pw, alarmid, src);
        JSONObject jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject,"alarmid",alarmid);
        jsonObject = HedgeHttpClient.HedgeRequest("get_alarm_with_alarmid",jsonObject);

        //if(src.get(0)[0].equals("Deleted") == true)
            //return START_NOT_STICKY;

        //on/off 확인
        if(HedgeHttpClient.getValues(jsonObject,"on_off").equals("false"))
            return START_NOT_STICKY;

        //반복 확인
        boolean repeat = HedgeHttpClient.getValues(jsonObject,"repeating").equals("true");

        //요일 확인
        boolean day[] = new boolean[7];
        String temp = HedgeHttpClient.getValues(jsonObject,"day");
        if(temp == "Fail") {
            int z = 5;
            return START_NOT_STICKY;
        }
        for(int i=0; i < 7; i++){
            String a = Character.toString(temp.charAt(i));
            day[i] = Integer.parseInt(a) == 1;
        }

        int today = getToday();

        boolean flag = false;

        for(int i=0; i < 7; i++){
            if(day[i]){
                flag = true;
                break;
            }
        }


        boolean weather = Boolean.parseBoolean(HedgeHttpClient.getValues(jsonObject, "weather"));

        if(!repeat && day[today]){
            //반복하지 않으므로 해당 요일을 지운다.
            //db.execSQL("update HedgeAlarm set d" + today + " = true where _id = "+id + ";");
        }

        if(!flag && !repeat){
            //이 알람을 off상태로 만듬
            //db.execSQL("update HedgeAlarm set on_off = 0 where _id = "+id + ";");

            //알람 시작
            startTimeout(weather, intent);
            return START_NOT_STICKY;
        }
        else if(day[today] == false){
            return START_NOT_STICKY;
        }

        //알람 시작
        startTimeout(weather, intent);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void startTimeout(boolean weather, Intent intent){
        pref = getSharedPreferences("isAlarming", 0);
        //boolean flag = pref.getBoolean("isAlarming", false);
        //if(flag){
        //    return;
        //}

        Intent send = new Intent(this, TimeoutActivity.class);
        send.putExtra("weather_alarm", weather);
        send.putExtra("alarm_type", intent.getExtras().getString("alarm_type"));
        send.putExtra("title", intent.getExtras().getString("title"));
        send.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(send);
    }

    private int getToday(){
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);

        if(today - 2 < 0 ){
            today = 6;
        }
        else {
            today -= 2;
        }

        return today;
    }
}
