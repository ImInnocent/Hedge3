package hedge.johnny.HedgeObject.Navigators;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import hedge.johnny.Activity.AddFriendActivity;
import hedge.johnny.Activity.FriendActivity;
import hedge.johnny.Activity.SendAlarmActivity;

/**
 * Created by Administrator on 2015-08-13.
 */
public class FriendNavigator extends NavigationGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, FriendActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Window a = getLocalActivityManager().startActivity("FriendActivity", intent);
        View view = a.getDecorView();
        view.setTag("FriendActivity");
        replaceView(view);
    }

    @Override
    public void onResume(){
        super.onResume();

        View v = history.get(history.size()-1);
        String a = (String)v.getTag();

        if(a.equals("FriendActivity")){
            FriendActivity currentActivity = (FriendActivity)v.getContext();
            currentActivity.refresh();
        }
        else if(a.equals("AddFriendActivity")){
            AddFriendActivity currentActivity = (AddFriendActivity)v.getContext();
            currentActivity.refresh();
        }
        else if(a.equals("SendAlarmActivity")){
            SendAlarmActivity currentActivity = (SendAlarmActivity)v.getContext();
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
