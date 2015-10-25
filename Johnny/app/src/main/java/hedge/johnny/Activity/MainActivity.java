package hedge.johnny.Activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import hedge.johnny.HedgeObject.Navigators.AlarmNavigator;
import hedge.johnny.HedgeObject.Navigators.FriendNavigator;
import hedge.johnny.R;

//public class MainActivity extends TabActivity
public class MainActivity extends TabActivity {
    public static Context MainContext;
    private TabHost tabHost;

    private void setupTabHost()
    {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainContext = MainActivity.this;

        SharedPreferences pref = getSharedPreferences("HedgeMembers",0);
        String name = pref.getString("username","None");

        Context context = getApplicationContext();
        CharSequence text = name + "님 반갑습니다";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        setContentView(R.layout.activity_main);
        setupTabHost();

        tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_selector);

        setupTab(new TextView(this), "알람");
        setupTab(new TextView(this), "친구");
        setupTab(new TextView(this), "설정");
        tabHost.setCurrentTab(0);
    }

    private void setupTab(final View view, final String tag)
    {
        View tabview = createTabView(tabHost.getContext(), tag);

        // TabSpec은 공개된 생성자가 없으므로 직접 생성할 수 없으며, TabHost의 newTabSpec메서드로 생성
        TabHost.TabSpec content = tabHost.newTabSpec(tag).setIndicator(tabview);

        if(tag.equals("알람"))
            content.setContent(new Intent(this, AlarmNavigator.class));
        else if(tag.equals("친구"))
            content.setContent(new Intent(this, FriendNavigator.class));
        else if(tag.equals("설정"))
            content.setContent(new Intent(this, SettingActivity.class));

        tabHost.addTab(content);
    }

    private static View createTabView(final Context context, final String text)
    {
        // layoutinflater를 이용해 xml 리소스를 읽어옴
        View view = LayoutInflater.from(context).inflate(R.layout.tab_widget_design, null);
        ImageView img;

        img = (ImageView)view.findViewById(R.id.tabs_image);
        if(text.equals("알람"))
            img.setImageResource(R.drawable.tab1);
        else if(text.equals("친구"))
            img.setImageResource(R.drawable.tab2);
        else if(text.equals("설정"))
            img.setImageResource(R.drawable.tab3);

        return view;
    }
}
