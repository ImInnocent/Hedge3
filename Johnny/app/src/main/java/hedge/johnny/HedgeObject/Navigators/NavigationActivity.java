package hedge.johnny.HedgeObject.Navigators;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by Administrator on 2015-08-13.
 */
public class NavigationActivity extends Activity {
    public void goNextHistory(String id,Intent intent)  { //앞으로 가기 처리
        NavigationGroup parent = ((NavigationGroup)getParent());

        View view = parent.group.getLocalActivityManager()
                .startActivity(id,intent)
                .getDecorView();
        view.invalidate();
        view.setTag(id);
        parent.group.replaceView(view);
        parent.onResume();
    }

    @Override
    public void onBackPressed() { //뒤로가기 처리
        NavigationGroup parent = ((NavigationGroup)getParent());
        parent.back();
    }

    protected int getHedgeId(String name){
        return getResources().getIdentifier(name, "id", getPackageName());
    }
}