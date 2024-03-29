package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.R;

public class TimeoutActivity extends Activity implements OnInitListener {
    TextToSpeech myTTS = null;
    TextView txtMsg = null;
    MediaPlayer music = null;
    Vibrator vibe = null;
    int user_volume = 0;
    AudioManager am;
    private boolean mWeatherAlarm;
    private String mtitle;

    Timer timer = new Timer(true);
    float volume = 1.f;
    float time = 0.0f;
    int high;
    int low;
    int curr;
    String locName;
    String weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
        String engWeather[] = {"Kangnung",
                "Kwangju",
                "Kunsan",
                "Kimch'on",
                "Taegwallyong",
                "Taegu",
                "Taejon",
                "Tonghae",
                "Masan",
                "Mokp'o",
                "Miryang",
                "Polgyo",
                "Pusan",
                "Sogwipo",
                "Sosan",
                "Seoul",
                "Songnam",
                "Sokcho",
                "Suwon",
                "Andong",
                "Anyang",
                "Yangyang",
                "Yosu",
                "Yongwol",
                "Osan",
                "Wando",
                "Ullungdo",
                "Ulsan",
                "Ulchin",
                "Wonju",
                "Uisong",
                "Iri",
                "Inch'on",
                "Chonju",
                "Cheju",
                "ChejuUpper",
                "Chinju",
                "Chinhae",
                "Ch'angwon",
                "Ch'onan",
                "Cholwon",
                "Chupungnyong",
                "Chunchon",
                "Chungmu",
                "Ch'ungju",
                "Pohang",
                "Haenam"};
        JSONObject jsonObject2 = new JSONObject();
        //SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
        int loc = pref.getInt("weather_area", 15);
        String engLoc = engWeather[loc];
        HedgeHttpClient.addValues(jsonObject2,"loc",engLoc);
        jsonObject2 = HedgeHttpClient.HedgeRequest("get_forecast", jsonObject2);

        locName = HedgeHttpClient.getValues(jsonObject2, "loc");
        JSONObject forecast = HedgeHttpClient.getObject(jsonObject2, "forecast");
        forecast = HedgeHttpClient.getObject(forecast, "0");
        String eWeather = HedgeHttpClient.getValues(forecast, "condition");
        if(eWeather.equals("Partly Cloud") || eWeather.equals("Cloud")){
            weather = "구름";
        }
        else if(eWeather.equals("Rainy")){
            weather = "비옴";
        }
        else if(eWeather.equals("Sunny")){
            weather = "맑음";
        }
        else if(eWeather.equals("Snowy")){
            weather = "눈옴";
        }
        high = (int)((float)(Integer.parseInt(HedgeHttpClient.getValues(forecast, "high")) - 32) / 1.8f);
        low = (int)((float)(Integer.parseInt(HedgeHttpClient.getValues(forecast, "low")) - 32) / 1.8f);
        curr = (int)((float)(Integer.parseInt(HedgeHttpClient.getValues(jsonObject2, "curr").substring(0, 2)) - 32) / 1.8f);


        SharedPreferences prefs = getSharedPreferences("isAlarming", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("isAlarming", true);
        edit.commit();

        PowerManager pm = (PowerManager) getSystemService( Context.POWER_SERVICE );
        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_BRIGHT_WAKE_LOCK|
                                                        PowerManager.ACQUIRE_CAUSES_WAKEUP|
                                                        PowerManager.ON_AFTER_RELEASE, "WakeUp" );
        wakeLock.acquire();

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        am.setStreamVolume(AudioManager.STREAM_ALARM, maxVol, AudioManager.FLAG_PLAY_SOUND);

        Intent intent = getIntent(); // 값을 받아온다.
        mWeatherAlarm = intent.getBooleanExtra("weather_alarm", true);
        mtitle = intent.getExtras().getString("title", "null");

        if(mWeatherAlarm)
            setContentView(R.layout.activity_timeout_weather);
        else
            setContentView(R.layout.activity_timeout);

        //musicInit();

        soundInit();

        textInit();
    }

    private void soundInit(){
        Intent intent = getIntent(); // 값을 받아온다.
        String alarmType = intent.getStringExtra("alarm_type");

        music = new MediaPlayer();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(alarmType.equals("1")){
            setMusic();
        }else if(alarmType.equals("2")){
            setMusic();
            setVibe();
        }else if(alarmType.equals("3")){
            setVibe();
        }

        if(mWeatherAlarm){
            timer.scheduleAtFixedRate(new UpdateTimeTask(), 3500, 100);

            myTTS = new TextToSpeech(this, this);
            final String city = "Seoul,KR";
            final String lang = "en";

            Log.e("TTS On : ", "ok");
        }
    }

    private void setMusic(){
//            music = MediaPlayer.create(this, R.raw.love_passion);
//            music.setLooping(true);
//            music.start();
//            music.setVolume(1.0f, 1.0f);
        //music = MediaPlayer.create(this, R.raw.love_passion);
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

//        Uri path = Uri.parse("android.resource://hedge.johnny/" + R.raw.love_passion);
        try{
            music.setDataSource(alert.toString());
            //music.setDataSource(alert.toString());
        }catch (IOException o){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        music.setAudioStreamType(AudioManager.STREAM_ALARM);
        //music.setVolume(1.0f, 1.0f);

        music.setLooping(true);
        try{
            music.prepare();
        }catch (IOException o){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        music.start();

        Log.e("Sound On : ", "ok");
    }

    private void setVibe(){
        long[] pattern = {800, 1600};

        vibe.vibrate(pattern, 0);

        Log.e("Vibe On : ", "ok");
    }

    public void btnClickTimeOut(View v){
        switch (v.getId()){
            case R.id.btn_goto_weatherquiz:
                Intent i = new Intent(TimeoutActivity.this, WeatherQuizActivity.class);
                i.putExtra("user_volume", user_volume);
                i.putExtra("weather", weather);
                startActivity(i);
                finish();
                break;
            case R.id.btn_end_alarm:
                stopAlarm();
                finish();
                break;
        }
    }

    private void textInit(){
        if(mWeatherAlarm){
            long now = System.currentTimeMillis();
            Date date = new Date(now);

            TextView nowTimeHour = (TextView)findViewById(R.id.timeout_weather_hour);

            SimpleDateFormat sdfHour = new SimpleDateFormat("hh");
            String strHour= sdfHour.format(date);
            nowTimeHour.setText(strHour);

            TextView nowTimeMinute = (TextView)findViewById(R.id.timeout_weather_minute);
            SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
            String strMinute= sdfMinute.format(date);
            nowTimeMinute.setText(strMinute);

            TextView curTemp = (TextView)findViewById(R.id.timeout_weather_curr);
            curTemp.setText(String.valueOf(curr) + "º");

            TextView highTemp = (TextView)findViewById(R.id.timeout_weather_high);
            highTemp.setText(String.valueOf(high) + "º");

            TextView lowTemp = (TextView)findViewById(R.id.timeout_weather_low);
            lowTemp.setText(String.valueOf(low) + "º");

            TextView alarmTitle = (TextView)findViewById(R.id.timeout_weather_msg);
            alarmTitle.setText(mtitle);

        }else{
            long now = System.currentTimeMillis();
            Date date = new Date(now);

            TextView nowTimeHour = (TextView)findViewById(R.id.tNowTimeHour);
            SimpleDateFormat sdfHour = new SimpleDateFormat("hh");
            String strHour= sdfHour.format(date);
            nowTimeHour.setText(strHour);

            TextView nowTimeMinute = (TextView)findViewById(R.id.tNowTimeMinute);
            SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
            String strMinute= sdfMinute.format(date);
            nowTimeMinute.setText(strMinute);

            // 알람 문구
            TextView alarmTitle = (TextView)findViewById(R.id.tNowMsg);
            alarmTitle.setText(mtitle);
        }
    }

    private void stopAlarm() {
        if(music.isPlaying())
            music.stop();

        if(vibe.hasVibrator())
            vibe.cancel();

        SharedPreferences pref = getSharedPreferences("isAlarming", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("isAlarming", false);
        edit.commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        SharedPreferences pref = getSharedPreferences("isAlarming", 0);
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putBoolean("isAlarming", false);
//        edit.commit();
    }

    public void SpeeachWeather()
    {
        JSONObject jsonObject = new JSONObject();

        String sCurr = curr < 0 ? "영하 " + String.valueOf(-curr) : String.valueOf(curr);
        String sLow = low < 0 ? "영하 " + String.valueOf(-low) : String.valueOf(low);
        String sHigh = high < 0 ? "영하 " + String.valueOf(-high) : String.valueOf(high);

        user_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, AudioManager.FLAG_PLAY_SOUND);

        String temp = Calendar.getInstance().get(Calendar.MONTH) + "월 " +
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일, " +
                (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1) + "시의 "+ locName +"에서의 날씨는, 눈옴입니다. ";
        myTTS.speak(temp, TextToSpeech.QUEUE_FLUSH, null);
        temp = ", 기온은 섭씨 " +
                sCurr + "도, 최저기온은 섭씨 " +
                sLow + "도, 최고기온은 섭씨" +
                sHigh + "도, 입니다.";
        myTTS.speak(temp, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int status) {
        myTTS.setLanguage(Locale.KOREA);
    }

    private class UpdateTimeTask extends TimerTask {
        private boolean briefTag = true;

        private void briefStart()
        {
            try {
                SpeeachWeather();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            final float BRTIME = 12.0f;

            time += 0.1f;
            if(time > 15.0f + BRTIME){
                time = 0.f;
                briefTag = true;
            }

            if( time > 5.0f + BRTIME )
            {
                volume += 0.01f; if(volume > 1.0f) volume = 1.0f;
            }

            else if( time >= 0.0f )
            {
                volume -= 0.02f;
                if(volume <= 0.00f)
                {
                    volume = 0.00f;
                    if(briefTag == true)
                    {
                        briefTag = false;
                        //breif start
                        briefStart();
                    }
                }
            }

            music.setVolume( volume, volume );
        }
    }
}
