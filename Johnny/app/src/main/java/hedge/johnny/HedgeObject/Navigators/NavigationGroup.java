package hedge.johnny.HedgeObject.Navigators;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-08-13.
 */
public class NavigationGroup extends ActivityGroup {
    ArrayList<View> history; // View들을 관리하기 위한 List
    protected NavigationGroup group; // Activity들이 접근하기 위한 Group

    public NavigationGroup getGroup(){
        return group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        history = new ArrayList<View>();
        group = this;
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    public void changeView(View v)  { // 동일한 Level의 Activity를 다른 Activity로 변경하는 경우
        history.remove(history.size()-1);
        history.add(v);
        setContentView(v);
    }

    public void replaceView(View v) {   // 새로운 Level의 Activity를 추가하는 경우
        history.add(v);
        setContentView(v);
    }

    public void back() { // Back Key가 눌려졌을 경우에 대한 처리
        if(history.size() > 1) {
            history.remove(history.size()-1);
            setContentView(history.get(history.size()-1));
        }
        else {
            finish(); // 최상위 Level의 경우 TabActvity를 종료해야 한다.
        }
    }

    @Override
    public void onBackPressed() { // Back Key에 대한 Event Handler
        group.back();
        return;
    }
}