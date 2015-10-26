package hedge.johnny.HedgeObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;

/**
 * Created by EDGE01 on 2015-08-21.
 */
public class AlarmBackgroundService extends Service implements Runnable{
    AlarmManager alarm;
    Thread thread;

    @Override
    public void onCreate() {
        alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        checkChanges();

        thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while (true){
            try {
                Log.e("Thread Running Check :", "ok");

                thread.sleep(10000);

                //확인
                checkChanges();
            }catch (Exception e){
                Log.e("Thread", e.toString());
            }
        }
    }

    private void checkChanges(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject = HedgeHttpClient.HedgeRequest("ensure_member", jsonObject);
            if (HedgeHttpClient.getValues(jsonObject, "result").equals("1") == false) return;

            jsonObject = new JSONObject();
             jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("alarm_update_list", jsonObject);
            // HedgeHttpClient.GetInstance().AlarmUpdateList(id,pw,alarmUpdateList);

            for (int i = 0; i < jsonObject.length() - 1; i++) {
                JSONObject row = HedgeHttpClient.getObject(jsonObject, String.valueOf(i));
                String modifier = HedgeHttpClient.getValues(row, "modifier"); // 수정자 (아직 안쓰임)
                String owner = HedgeHttpClient.getValues(row, "ownerid"); // 알람을 받은 사람 (백그라운드 업데이트가 필요한 유저)
                String alarmid = HedgeHttpClient.getValues(row, "alarmid");  // 알람의 id
                String state = HedgeHttpClient.getValues(row, "state");

                if (state.equals("3")) {
                    cancelAlarm(Integer.parseInt(alarmid));
                } else if (state.equals("2")) {
                    cancelAlarm(Integer.parseInt(alarmid));
                    setAlarm(alarmid);
                } else {
                    setAlarm(alarmid);
                }
                Log.e("Thread Running : " + alarmid + "알람매니저와 동기화시키기", "ok");
            }
        }
        catch (Exception e)
        {
            Log.e("Thread Running : " + e.toString(), "error");
        }
        finally {
            JSONObject jsonObject = new JSONObject();
            HedgeHttpClient.HedgeRequest("clear_alarm_update", jsonObject);
        }

    }

    private void cancelAlarm(int alarmId){
        Intent intent = new Intent(this, HedgeAlarmService.class);
        PendingIntent pender = PendingIntent.getService(this, alarmId, intent, 0);
        alarm.cancel(pender);
    }

    private void setAlarm(String alarmId){
        JSONObject alarminfo = new JSONObject();
        HedgeHttpClient.addValues(alarminfo,"alarmid",alarmId);
        alarminfo = HedgeHttpClient.HedgeRequest("get_alarm_with_alarmid", alarminfo);

        Intent intent = new Intent(this, HedgeAlarmService.class);
        intent.putExtra("repeat", HedgeHttpClient.getValues(alarminfo,"repeating"));    //intent에 요일 on/off정보를 넣음.
        intent.putExtra("weather_alarm",HedgeHttpClient.getValues(alarminfo,"weather"));
        intent.putExtra("alarm_type", HedgeHttpClient.getValues(alarminfo,"alarm_type"));
        intent.putExtra("title", HedgeHttpClient.getValues(alarminfo,"title"));
        intent.putExtra("db_id", alarmId);

        PendingIntent pender = PendingIntent.getService(this, Integer.parseInt(alarmId), intent, 0);

        long OneDay = 24 * 60 * 60 * 1000;  //하루를 MilliSecond로 표현

        Calendar current = Calendar.getInstance();
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);
        long cTime = current.getTimeInMillis(); //currentTime = cTime

        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HedgeHttpClient.getValues(alarminfo,"hour")));
        target.set(Calendar.MINUTE, Integer.parseInt(HedgeHttpClient.getValues(alarminfo,"min")));
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);
        long tTime = target.getTimeInMillis();  //targetTime = tTime

        long trigger = cTime > tTime ? tTime + OneDay - cTime : tTime - cTime;

        long intervalTime = OneDay; //하루마다 반복하고, 알람시 켜지는 클래스에서 요일반복을 처리한다.

        //alarm.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pender);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cTime + trigger, intervalTime, pender);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
