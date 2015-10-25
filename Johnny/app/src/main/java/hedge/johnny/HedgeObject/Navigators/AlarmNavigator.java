package hedge.johnny.HedgeObject.Navigators;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import hedge.johnny.Activity.AddAlarmActivity;
import hedge.johnny.Activity.AlarmActivity;

/**
 * Created by Administrator on 2015-08-13.
 */
public class AlarmNavigator extends NavigationGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Window a = getLocalActivityManager().startActivity("AlarmActivity", intent);
        View view = a.getDecorView();
        view.setTag("AlarmActivity");
        replaceView(view);
    }

    @Override
    public void onResume(){
        super.onResume();

        View v = history.get(history.size()-1);
        String a = (String)v.getTag();

        if(a.equals("AlarmActivity")){
            AlarmActivity currentActivity = (AlarmActivity)v.getContext();
            currentActivity.refresh();
        }
        else if(a.equals("AddAlarmActivity")){
            AddAlarmActivity currentActivity = (AddAlarmActivity)v.getContext();
            currentActivity.refresh();
        }

    }

    @Override
    public void onBackPressed() { // Back Key에 대한 처리 요청
        super.onBackPressed();
    }

    @Override
    public void back() { // Back Key가 눌려졌을 경우에 대한 처리
        if(history.size() > 1) {
            history.remove(history.size() - 1);
            View next = history.get(history.size() - 1);
            onResume();
            setContentView(next);
        }
        else {
            finish(); // 최상위 Level의 경우 TabActvity를 종료해야 한다.
        }
    }
}