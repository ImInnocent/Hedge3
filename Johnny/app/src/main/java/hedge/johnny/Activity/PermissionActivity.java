package hedge.johnny.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import hedge.johnny.R;

/**
 * Created by Administrator on 2015-07-21.
 */
public class PermissionActivity extends Activity implements View.OnClickListener, MyDialog.TimeDlgListener{
    Boolean onoff = false;
    ToggleButton tgBtn;
    Button startBtn, endBtn;
    TextView tvStart, tvEnd;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        init();
    }

    public void init(){
        tgBtn = (ToggleButton) findViewById(R.id.pms_onoff_btn);
        startBtn = (Button) findViewById(R.id.pms_Start1);
        endBtn = (Button) findViewById(R.id.pms_End1);
        tvStart = (TextView) findViewById(R.id.pms_tv_start);
        tvEnd = (TextView) findViewById(R.id.pms_tv_end);

        // 알람 금지를 켰는지 껐는지 서버와 통신해서 셋팅
        pref = getSharedPreferences("HedgeMembers", 0);
        startBtn.setText(pref.getString("permission_start", "PM 12:00"));       // 시작시간 불러옴
        endBtn.setText(pref.getString("permission_end", "PM 6:00"));           // 종료시간 불러옴
        if(pref.getString("permission_onoff", "") == "on")              // onoff 유무 불러옴
            tgBtn.setChecked(true);
        else
            tgBtn.setChecked(false);


        if(tgBtn.isChecked())
            turnOn();
        else
            turnOff();
    }

    @Override
    public void onClick(View v) {
        MyDialog dlg;

        switch (v.getId()){
            case R.id.pms_back_btn:         // 뒤로가기
                this.finish();
                break;

            case R.id.pms_onoff_btn:        // 켜기 끄기 토글버튼
                if (onoff == true)  turnOff();
                else                 turnOn();
                break;

            case R.id.pms_Start1:case R.id.pms_Start2:      // 시작 시간
                if(onoff == false)
                    return;
                dlg = new MyDialog();                  // time dialog 호출
                dlg.show(getFragmentManager(), "time_start");
                break;

            case R.id.pms_End1:case R.id.pms_End2:          // 종료 시간
                if(onoff == false)
                    return;
                dlg = new MyDialog();
                dlg.show(getFragmentManager(), "time_end");
                break;

            default:
                break;
        }
        return;
    }

    public void turnOff(){
        onoff = false;

        startBtn.setTextColor(Color.LTGRAY);
        endBtn.setTextColor(Color.LTGRAY);
        tvStart.setTextColor(Color.LTGRAY);
        tvEnd.setTextColor(Color.LTGRAY);

        SharedPreferences.Editor edit = pref.edit();
        edit.putString("permission_onoff", "off");
        edit.commit();
    }

    public void turnOn(){
        onoff = true;

        startBtn.setTextColor(Color.parseColor("#65c7ff"));
        endBtn.setTextColor(Color.parseColor("#65c7ff"));
        tvStart.setTextColor(Color.BLACK);
        tvEnd.setTextColor(Color.BLACK);

        SharedPreferences.Editor edit = pref.edit();
        edit.putString("permission_onoff", "on");
        edit.commit();
    }

    @Override
    public void time(String str, String tag) {
        Button btn;
        SharedPreferences.Editor edit = pref.edit();

        switch (tag)
        {
            case "time_start":
                btn = (Button)findViewById(R.id.pms_Start1);    // time 다이얼로그에 따라 텍스트 변경
                btn.setText(str);
                // Preferences 변수에 저장
                edit.putString("permission_start", str);
                break;
            case "time_end":
                btn = (Button)findViewById(R.id.pms_End1);
                btn.setText(str);
                edit.putString("permission_end", str);
                break;
            default:
                break;
        }
        edit.commit();
    }
}