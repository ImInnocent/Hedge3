package hedge.johnny.HedgeObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hedge.johnny.Activity.AlarmActivity;
import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.HedgeObject.Navigators.NavigationActivity;
import hedge.johnny.HedgeObject.Navigators.NavigationGroup;
import hedge.johnny.R;

/**
 * Created by Administrator on 2015-08-22.
 */
public class AlarmSetting extends NavigationActivity {
    protected boolean mDayOfWeek[] = new boolean[7];
    protected int mHour;
    protected int mMinute;
    protected int mAlarmType;
    protected boolean mRepeat;
    protected boolean mWeatherAlarm;
    protected String mTitle;

    protected int sound = 1;
    protected int sound_vibe = 2;
    protected int vibe = 3;

    protected String activity_name = null;
    protected boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void setActivityName(String activity_name){
        this.activity_name = activity_name;
    }

    protected void setTextWithData(){
        String alarmid = AlarmActivity.tag;

        //HedgeHttpClient.GetInstance().GetAlarmWithAlarmID(id,pw,alarmid,src);
        JSONObject jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject,"alarmid",alarmid);
        jsonObject = HedgeHttpClient.HedgeRequest("get_alarm_with_alarmid",jsonObject);

        if(HedgeHttpClient.getValues(jsonObject,"result").equals("1") == false)
        {
            exit = true;
            Toast.makeText(getApplicationContext(), "존재하지 않는 알람입니다.", Toast.LENGTH_LONG).show();
            return;
        }
        //시간
        EditText hour, minute;
        hour = (EditText)findViewById(R.id.hour_add_alarm);
        minute = (EditText)findViewById(R.id.minute_add_alarm);

        int ind = Integer.parseInt(HedgeHttpClient.getValues(jsonObject,"hour"));

        if(ind >= 12){
            hour.setText(String.valueOf(ind - 12));
            CheckBox day_night = (CheckBox)findViewById(R.id.daynight_add_alarm);
            day_night.setChecked(true);
        }
        else
            hour.setText(String.valueOf(ind));
        minute.setText(HedgeHttpClient.getValues(jsonObject,"min"));

        //요일
        CheckBox day;
        int d = Integer.parseInt(HedgeHttpClient.getValues(jsonObject,"day"));
        for(int i = 6; i >= 0; i--){
            int sid = getResources().getIdentifier("@id/day" + (i + 1) + "_add_alarm", "id", this.getPackageName());
            day = (CheckBox) findViewById(sid);
            boolean mark = d % 10 == 1;
            day.setChecked(mark);
            d = d/10;
        }

        //날씨 알람
        CheckBox weather_quiz = (CheckBox)findViewById(R.id.weather_add_alarm);
        weather_quiz.setChecked(HedgeHttpClient.getValues(jsonObject,"weather").equals("true"));

        //알람 타입
        String type = HedgeHttpClient.getValues(jsonObject,"alarm_type");
        RadioButton alarm_type;
        if(type.equals("1"))
            alarm_type = (RadioButton) findViewById(R.id.sound_add_alarm);
        else if(type.equals("2"))
            alarm_type = (RadioButton) findViewById(R.id.sound_vibe_add_alarm);
        else
            alarm_type = (RadioButton) findViewById(R.id.vibe_add_alarm);
        alarm_type.setChecked(true);

        //반복 알람
        CheckBox alarm_repeat = (CheckBox)findViewById(R.id.repeat_add_alarm);
        alarm_repeat.setChecked(HedgeHttpClient.getValues(jsonObject,"repeating").equals("true"));
    }

    protected void setTextWithNow(){
        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        // 시간 포맷으로 만든다.
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
        String strHour = sdfHour.format(date);
        String strMinute = sdfMinute.format(date);

        EditText hour, minute;
        hour = (EditText)findViewById(getHedgeId("@id/hour_"+ activity_name + "_alarm"));
        minute = (EditText)findViewById(getHedgeId("@id/minute_"+ activity_name + "_alarm"));

        int ind = Integer.parseInt(strHour);

        if(ind > 12){
            hour.setText(String.valueOf(ind - 12));
            CheckBox day_night = (CheckBox)findViewById(getHedgeId("@id/daynight_"+ activity_name + "_alarm"));
            day_night.setChecked(true);
        }
        else
            hour.setText(strHour);

        minute.setText(strMinute);

        //요일
        CheckBox day;
        for(int i = 0; i < 7; i++){
            day = (CheckBox) findViewById(getHedgeId("@id/day" + (i + 1) + "_" + activity_name + "_alarm"));
            day.setChecked(false);
        }

        //반복 알람
        CheckBox alarm_repeat = (CheckBox)findViewById(getHedgeId("@id/repeat_" + activity_name + "_alarm"));
        alarm_repeat.setChecked(false);

        //알람 타입
        RadioButton alarm_type = (RadioButton) findViewById(getHedgeId("@id/sound_" + activity_name + "_alarm"));
        alarm_type.setChecked(true);

        //날씨 알람
        CheckBox weather_quiz = (CheckBox)findViewById(getHedgeId("@id/weather_" + activity_name + "_alarm"));
        weather_quiz.setChecked(false);
    }

    protected  void initMembers() {
        //시간 저장
        EditText hour = (EditText) findViewById(getHedgeId("@id/hour_"+ activity_name + "_alarm"));
        CheckBox daynight = (CheckBox) findViewById(getHedgeId("@id/daynight_"+ activity_name + "_alarm"));

        mHour = Integer.parseInt(hour.getText().toString());

        if(daynight.isChecked())
            mHour += 12;

        EditText minute = (EditText)findViewById(getHedgeId("@id/minute_"+ activity_name + "_alarm"));

        mMinute = Integer.parseInt(minute.getText().toString());

        for(int i = 0; i < 7; i++){
            mDayOfWeek[i] = ((CheckBox) findViewById(getHedgeId("@id/day" + (i + 1) + "_" + activity_name + "_alarm"))).isChecked();
        }

        mWeatherAlarm = ((CheckBox)findViewById(getHedgeId("@id/weather_"+ activity_name + "_alarm"))).isChecked();

        if(((RadioButton)findViewById(getHedgeId("@id/sound_" + activity_name + "_alarm"))).isChecked())
            mAlarmType = sound;
        if(((RadioButton)findViewById(getHedgeId("@id/sound_vibe_"+ activity_name + "_alarm"))).isChecked())
            mAlarmType = sound_vibe;
        if(((RadioButton)findViewById(getHedgeId("@id/vibe_"+ activity_name + "_alarm"))).isChecked())
            mAlarmType = vibe;

        mRepeat = ((CheckBox)findViewById(getHedgeId("@id/repeat_"+ activity_name + "_alarm"))).isChecked();

        mTitle = ((EditText)findViewById(getHedgeId("@id/msg_"+ activity_name + "_alarm"))).getText().toString();
    }

    protected int InsertAlarm(String toid, boolean modify){
        //데이터 베이스에 저장
        SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
        String id = pref.getString("userid", "None");
        String pw = pref.getString("password", "None");
        String hour = Integer.toString(mHour);
        String min = Integer.toString(mMinute);
        String day = "";
        for(int i=0; i<7; i++)
        {
            if( mDayOfWeek[i] == true)
                day += "1";
            else
                day += "0";
        }
        String weather;
        if(mWeatherAlarm == true)
            weather = "1";
        else weather = "0";
        String alarm_type = Integer.toString(mAlarmType);
        String on_off = "1";
        String repeat;
        if(mRepeat == true)
            repeat = "1";
        else repeat = "0";
        String fromid = id;

        String alarmid = "";
        int alarmid_int;

        JSONObject jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject,"hour",hour);
        HedgeHttpClient.addValues(jsonObject,"min",min);
        HedgeHttpClient.addValues(jsonObject,"day",day);
        HedgeHttpClient.addValues(jsonObject,"weather",weather);
        HedgeHttpClient.addValues(jsonObject,"alarm_type",alarm_type);
        HedgeHttpClient.addValues(jsonObject,"on_off",on_off);
        HedgeHttpClient.addValues(jsonObject,"repeating",repeat);
        HedgeHttpClient.addValues(jsonObject,"title",mTitle);

        if(modify){
            alarmid = getIntent().getExtras().getString("alarmid");
           // HedgeHttpClient.GetInstance().ModifyAlarm(id, pw, hour, min, day, weather, alarm_type, on_off, repeat, mTitle, alarmid);

            HedgeHttpClient.addValues(jsonObject,"alarmid",alarmid);
            jsonObject = HedgeHttpClient.HedgeRequest("modify_alarm", jsonObject);

            alarmid_int = Integer.parseInt(alarmid);
           // HedgeHttpClient.GetInstance().InsertAlarmUpdate(id, pw, id, Integer.toString(alarmid_int), "2"); //변경 로그 저장\
            JSONObject jsonAU = new JSONObject();
            HedgeHttpClient.addValues(jsonAU, "alarmid",alarmid);
            HedgeHttpClient.addValues(jsonAU, "state","2");
            jsonAU = HedgeHttpClient.HedgeRequest("insert_alarm_update", jsonAU);
        }
        else{
         //   alarmid = HedgeHttpClient.GetInstance().InsertAlarm(id, pw, hour, min, day, weather, alarm_type, on_off,
         //           repeat, fromid, toid, mTitle);
            HedgeHttpClient.addValues(jsonObject,"fromid",fromid);
            HedgeHttpClient.addValues(jsonObject,"toid",toid);
            jsonObject = HedgeHttpClient.HedgeRequest("insert_alarm",jsonObject);

            alarmid = HedgeHttpClient.getValues(jsonObject,"alarmid");
            alarmid_int = Integer.parseInt(alarmid);
         //   HedgeHttpClient.GetInstance().InsertAlarmUpdate(id, pw, id, Integer.toString(alarmid_int), "1"); //변경 로그 저장\
            JSONObject jsonAU = new JSONObject();
            HedgeHttpClient.addValues(jsonAU, "alarmid",alarmid);
            HedgeHttpClient.addValues(jsonAU, "state","1");
            jsonAU = HedgeHttpClient.HedgeRequest("insert_alarm_update", jsonAU);
        }
        return alarmid_int;
    }
}
