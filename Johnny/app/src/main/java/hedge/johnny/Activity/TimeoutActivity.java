package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.HedgeObject.HttpClient.WeatherHttpClient;
import hedge.johnny.HedgeObject.Weather;
import hedge.johnny.HedgeObject.WeatherForecast;
import hedge.johnny.R;

public class TimeoutActivity extends Activity implements OnInitListener {
    TextToSpeech myTTS = null;
    TextView txtMsg = null;
    JSONWeatherTask task = new JSONWeatherTask();
    JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
    MediaPlayer music = null;
    Vibrator vibe = null;
    int user_volume = 0;
    AudioManager am;
    private boolean mWeatherAlarm;
    private String mtitle;

    Timer timer = new Timer(true);
    float volume = 1.f;
    float time = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("isAlarming", 0);
        SharedPreferences.Editor edit = pref.edit();
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

            task.execute(new String[]{city, lang});
            task1.execute(new String[]{city, lang, "3"});

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
                startActivity(i);
                finish();
                break;
            case R.id.btn_end_alarm:
                stopAlarm();
                finish();
                break;
        }
    }

//    private void musicInit(){
//        if(mWeatherAlarm){
//            timer.scheduleAtFixedRate(new UpdateTimeTask(), 3500, 100);
//
//            music = MediaPlayer.create(this, R.raw.love_passion);
//            music.setLooping(true);
//            music.start();
//            music.setVolume(1.0f, 1.0f);
//
//            myTTS = new TextToSpeech(this, this);
//            final String city = "Seoul,KR";
//            final String lang = "en";
//
//            task.execute(new String[]{city, lang});
//            task1.execute(new String[]{city, lang, "3"});
//        }
//        else{
//            music = MediaPlayer.create(this, R.raw.love_passion);
//            music.setLooping(true);
//            music.start();
//            music.setVolume(1.0f, 1.0f);
//        }
//    }

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
    }

    public void SpeeachWeather(WeatherForecast speechData)
    {
//        DayForecast today = speechData.getForecast(0);
//        String weather = today.weather.currentCondition.getDescr();
//        int temp_day = (int)((float) (today.forecastTemp.day - 273.15));
//        int temp_min = (int)((float) (today.forecastTemp.min - 273.15));
//        int temp_max = (int)((float) (today.forecastTemp.max - 273.15));
//
//        int date = today.getintDate();
//        int time = today.getTimeData();
//
//        if( weather.equals("sky is clear") )
//        {
//            weather = "맑음";
//        }

//        String temp = date/100 + "월 " + date%100 + "일, " + time + "시의 서울 날씨는, " + weather + ", 입니다. ";
//        myTTS.speak(temp, TextToSpeech.QUEUE_FLUSH, null);
//        temp = ", 기온은 섭씨 " + temp_day + "도, " + "최저기온은 " + temp_min + "도, " + "최고기온은 " + temp_max + "도, 입니다.";
//        myTTS.speak(temp, TextToSpeech.QUEUE_ADD, null);

        user_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, maxVol, AudioManager.FLAG_PLAY_SOUND);

        String temp = "8월 26일, " + getResources().getString(R.string.now_time) + "시의 서울 날씨는, 구름입니다. ";
        myTTS.speak(temp, TextToSpeech.QUEUE_FLUSH, null);
        temp = ", 기온은 섭씨 25도, 최저기온은 20도, 최고기온은 25도, 입니다.";
        myTTS.speak(temp, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int status) {
        myTTS.setLanguage(Locale.KOREA);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0],params[1]));
           /* try {
               // weather = JSONWeatherParser.getWeather(data);
                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

        }
    }

    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
            WeatherForecast forecast = new WeatherForecast();
            /*try {
                forecast = JSONWeatherParser.getForecastWeather(data);
                System.out.println("Weather [" + forecast + "]");
                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            return forecast;
        }
    }

    private class UpdateTimeTask extends TimerTask {
        private boolean briefTag = true;

        private void briefStart()
        {
            WeatherForecast speechData;
            try {
                speechData = task1.get();
                SpeeachWeather(speechData);
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
