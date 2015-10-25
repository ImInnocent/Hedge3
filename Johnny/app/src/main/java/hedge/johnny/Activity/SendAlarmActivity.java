package hedge.johnny.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import hedge.johnny.HedgeObject.AlarmSetting;
import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.HedgeObject.Navigators.NavigationGroup;
import hedge.johnny.R;

/**
 * Created by EDGE01 on 2015-08-21.
 */
public class SendAlarmActivity extends AlarmSetting{
    EditText text_box;

    private String toid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alarm);

        super.setActivityName("send");

       // toid = getIntent().getStringExtra("friend_id");
        refresh();

        // 알람 글귀 포커싱하면 클리어
        text_box = (EditText) findViewById(R.id.msg_send_alarm);
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
        toid = FriendActivity.tag;
        setTextWithNow();

    }

    public void btnClickSendAlarm(View v){
        switch (v.getId()){
            case R.id.btn_save_send_alarm:
            {
                initMembers();      //설정값을 변수에 저장
                text_box.setText("일어나♥");
                //디비에 추가
                int id = InsertAlarm(toid, false);
                NavigationGroup parent = ((NavigationGroup) getParent());
                parent.back();
                break;
            }
            case R.id.btn_cancel_send_alarm:
            {
                text_box.setText("일어나♥");

                NavigationGroup parent = ((NavigationGroup) getParent());
                parent.back();
                break;
            }
        }
    }
}
