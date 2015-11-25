package hedge.johnny.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import hedge.johnny.R;
/**
 * Created by Administrator on 2015-07-21.
 */
public class SettingActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button logoutBtn, permissionBtn;
    Spinner areaList;
    ArrayAdapter<CharSequence> adapterSpin;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    int selectAreaNum;

    String[] areaData = {"강릉", "광주", "군산", "김천", "대관령", "대구", "대전",
            "동해", "마산", "목포", "밀양", "벌교", "부산", "서귀포",
            "서산", "서울", "성남", "속초", "수원", "안동", "안양",
            "양양", "여수", "영월", "오산", "완도", "울릉도", "울산",
            "울진", "원주", "의송", "이리", "인천", "전주", "제주",
            "북제주", "진주", "진해", "창원", "천안", "철원", "추풍령",
            "춘천", "충무", "충주", "포항", "해남"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logoutBtn = (Button)findViewById(R.id.logout);
        logoutBtn.setOnClickListener(this);
        permissionBtn = (Button)findViewById(R.id.permission);
        permissionBtn.setOnClickListener(this);

        areaList = (Spinner)findViewById(R.id.area_list);

        areaList.setPrompt("지역 설정");

        areaList.setOnItemSelectedListener(this);
        adapterSpin = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, areaData);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaList.setAdapter(adapterSpin);


        pref = getSharedPreferences("HedgeMembers", 0);
        edit = pref.edit();
        selectAreaNum = pref.getInt("weather_area", -1);
        if(selectAreaNum == -1) {
            edit.putInt("weather_area", 15);      // default값 = 서울
            edit.commit();
        }
        else
        {
            areaList.setSelection(selectAreaNum);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                SharedPreferences pref = getSharedPreferences("HedgeMembers",0);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("userid", "");
                edit.putString("password", "");
                edit.commit();

                Intent i = new Intent(SettingActivity.this, InitialActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.permission:
                Intent intent = new Intent(SettingActivity.this, PermissionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected " + areaData[position], Toast.LENGTH_SHORT).show();
        edit.putInt("weather_area", position);      // default값 = 서울
        edit.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "NothingSelected", Toast.LENGTH_SHORT).show();
    }
}