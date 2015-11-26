package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import hedge.johnny.R;


/**
 * Created by EDGE01 on 2015-07-24.
 */
public class WeatherQuizActivity  extends Activity {
    EditText eWInput;
    ImageButton IB_Sunny, IB_Cloud, IB_Rain, IB_Snow;
    TextView tvNowTime;
    int selectNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_quiz);

        // init
        eWInput = (EditText)findViewById(R.id.eWeatherInput);
        IB_Sunny = (ImageButton)findViewById(R.id.bSunny);
        IB_Cloud = (ImageButton)findViewById(R.id.bCloud);
        IB_Rain = (ImageButton)findViewById(R.id.bRain);
        IB_Snow = (ImageButton)findViewById(R.id.bSnow);
        tvNowTime = (TextView)findViewById(R.id.weaher_now_time);

        // 현재 시간
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH:mm");   // 포맷
        String strCurTime = CurTimeFormat.format(date);     // String 변환
        tvNowTime.setText(strCurTime);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 화면의 레이아웃이 설정된 후에, 아이콘들의 사이즈 일체화
        int sizeW = IB_Sunny.getWidth();
        int sizeH = IB_Sunny.getHeight();

        IB_Cloud.setMinimumWidth(sizeW);
        IB_Rain.setMinimumWidth(sizeW);
        IB_Rain.setMinimumHeight(sizeH);
        IB_Snow.setMinimumWidth(sizeW);
    }

    public void btn_quiz(View v)
    {
        switch (v.getId())
        {
            case R.id.bSunny:
                refreshImage(0);
                IB_Sunny.setImageResource(R.drawable.sunny_ch);
                eWInput.setHint("맑음");
                break;
            case R.id.bCloud:
                refreshImage(1);
                IB_Cloud.setImageResource(R.drawable.cloud_ch);
                eWInput.setHint("구름");
                break;
            case R.id.bRain:
                refreshImage(2);
                IB_Rain.setImageResource(R.drawable.rain_ch);
                eWInput.setHint("비옴");
                break;
            case R.id.bSnow:
                refreshImage(3);
                IB_Snow.setImageResource(R.drawable.snow_ch);
                eWInput.setHint("눈옴");
                break;
            case R.id.weather_ok:
                String input = "", hint = "null";   // 아무것도 입력하지 않았을 때도 통과되는것을 막기위해 임의의 문자열(null)로 설정

                input = eWInput.getText().toString();
                hint = eWInput.getHint().toString();

                // 입력한 문자열과 정답이 일치한다면~
                if(hint.equals(input)){
                    if(!input.equals("눈옴")){
                        Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //Toast.makeText(getApplicationContext(), "equals", Toast.LENGTH_SHORT).show();
                    AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, getIntent().getExtras().getInt("user_volume"), AudioManager.FLAG_PLAY_SOUND);
                    System.exit(0);
                }
                else                        Toast.makeText(getApplicationContext(), "틀렸습니다", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    // 날씨 아이콘이 선택되었을 때, 다른 아이콘들은 체크를 품.
    public void refreshImage(int num)
    {
        if(selectNum == 0)
            IB_Sunny.setImageResource(R.drawable.sunny);
        else if(selectNum == 1)
            IB_Cloud.setImageResource(R.drawable.cloud);
        else if(selectNum == 2)
            IB_Rain.setImageResource(R.drawable.rain);
        else if(selectNum == 3)
            IB_Snow.setImageResource(R.drawable.snow);

        selectNum = num;
    }
}
