package hedge.johnny.Activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.R;

public class MyDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener, View.OnClickListener{

    String tag = "";        // 입력된 다이얼로그의 tag
    EditText edit;          // 사용자가 입력한 정보를 받아옴
    Button btn_msg1, btn_msg2, btn_msg3, btn_cancel, btn_send;      // 레이아웃 내의 버튼
    View theView;           // 버튼 리스너 등록을 위해
    AlertDialog alert;      // alert 종료를 위해
    String friendId;

    public MyDialog() {
        super();
    }

    public MyDialog(String id){
        friendId = id;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        tag = getTag();

        if(tag.equals("time_start") || tag.equals("time_end"))
        {
            Calendar cal = Calendar.getInstance();
            return new TimePickerDialog(getActivity(), this, cal.get(cal.HOUR), cal.get(cal.MINUTE), false);
        }
        else if(tag.equals("alert"))
        {
            // 빌더 만들기
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Edit
            LayoutInflater inflater = getActivity().getLayoutInflater();
            theView = inflater.inflate(R.layout.activity_alert_dialog, null);
            builder.setView(theView);

            TextView name = (TextView)theView.findViewById(R.id.alert_who);
            name.setText(friendId);

            alertInit();
            alert =  builder.create();
            return alert;
        }
        else
            return null;
    }

    public void alertInit(){
        edit = (EditText)theView.findViewById(R.id.alert_edit);
        btn_msg1 = (Button)theView.findViewById(R.id.btn_msg1);
        btn_msg2 = (Button)theView.findViewById(R.id.btn_msg2);
        btn_msg3 = (Button)theView.findViewById(R.id.btn_msg3);
        btn_msg1.setOnClickListener(this);
        btn_msg2.setOnClickListener(this);
        btn_msg3.setOnClickListener(this);

        btn_cancel = (Button)theView.findViewById(R.id.alert_cancel);
        btn_send = (Button)theView.findViewById(R.id.alert_send);
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    @Override   // alert btn onclick
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_msg1: case R.id.btn_msg2: case R.id.btn_msg3:
                Button btn = (Button)view;
                if(edit.getText().toString().equals(""))
                    edit.setText(edit.getText().toString() + btn.getText().toString());
                else
                    edit.setText(edit.getText().toString() + "\n" + btn.getText().toString());
                break;
            case R.id.alert_send:
                // 서버에 메세지 저장
                // 친구ID : friendId
                // 메세지 내용 : edit.getText().toString()

                Toast.makeText(theView.getContext(), friendId + "\n" + edit.getText().toString(), Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                HedgeHttpClient.addValues(jsonObject,"message", edit.getText().toString());
                HedgeHttpClient.addValues(jsonObject,"friendid",friendId);
                jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("send_push",jsonObject);

                alert.cancel();
                break;
            case R.id.alert_cancel:
                alert.cancel();
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        String str, status = "AM";

        if(hour > 11){
            status = "PM";
            hour -= 12;
        }
        if(min<10)
            str = status + " " + hour + ":0" + min;
        else
            str = status + " " + hour + ":" + min;

        if (getActivity() instanceof TimeDlgListener) {   // 해당 리스너를 구현한 액티비티이면~
            ((TimeDlgListener) getActivity()).time(str, tag);    // 해당 액티비티의 함수 호출
        }
    }

    public interface TimeDlgListener{
        public void time(String str, String tag);
    }
}
