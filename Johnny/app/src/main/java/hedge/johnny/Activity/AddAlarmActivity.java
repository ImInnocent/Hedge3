package hedge.johnny.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import hedge.johnny.HedgeObject.AlarmSetting;
import hedge.johnny.HedgeObject.Navigators.NavigationGroup;
import hedge.johnny.R;

/**
 * Created by Administrator on 2015-07-21.
 */
public class AddAlarmActivity extends AlarmSetting{
    EditText text_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        super.setActivityName("add");
        refresh();

        // 알람 글귀 포커싱하면 클리어
        text_box = (EditText) findViewById(R.id.msg_add_alarm);
        text_box.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    text_box.setText("");
                }
            }
        });
    }

    public void refresh()
    {
        int tag = Integer.parseInt(AlarmActivity.tag);
        if(tag < 0)
        {
            setTextWithNow();
        }
        else
        {
            setTextWithData();
        }
    }

    public void btnClickAddAlarm(View v){

        switch (v.getId()){
            case R.id.btn_save_add_alarm:
            {
                if( exit )
                {
                    exit = false;
                }
                else{
                    initMembers();      //설정값을 변수에 저장
                    text_box.setText("일어나♥");

                    int tag = Integer.parseInt(AlarmActivity.tag);
                    SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
                    String id = pref.getString("userid", "None");
                    String pw = pref.getString("password", "None");

                    if (tag != -1) {
                        //수정 일 때
                        int alarmid = InsertAlarm(id, true); //
                        //태그를 이용해 서버의 알람목록에서 수정
                    }
                    else{
                        //추가 일 때
                        int alarmid = InsertAlarm(id, false); //디비에 추가
                    }
                }



                NavigationGroup parent = ((NavigationGroup)getParent());
                parent.getGroup().getLocalActivityManager().destroyActivity("AddAlarmActivity", true);

                //NavigationGroup parent = ((NavigationGroup) getParent());
                parent.back();
                break;
            }
            case R.id.btn_cancel_add_alarm:
            {
                if( exit )
                {
                    exit = false;
                }
                text_box.setText("일어나♥");

                NavigationGroup parent = ((NavigationGroup) getParent());
                parent.back();
                break;
            }
        }
    }
}
