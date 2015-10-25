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
<<<<<<< HEAD
    String[] areaData = {"강릉", "광주", "군산", "�?�?", "??�??��", "??�?", "???��",
            "?��?��", "마산", "목포", "�??��", "벌교", "�??��", "?���??��",
            "?��?��", "?��?��", "?��?��", "?���?", "?��?��", "?��?��", "?��?��",
            "?��?��", "?��?��", "?��?��", "?��?��", "?��?��", "?��릉도", "?��?��",
            "?���?", "?���?", "?��?��", "?���?", "?���?", "?���?", "?���?",
            "북제�?", "진주", "진해", "창원", "천안", "철원", "추풍?��",
            "춘천", "충무", "충주", "?��?��", "?��?��"};
=======
    String[] areaData = {"����", "����", "����", "��õ", "�����", "�뱸", "����",
            "����", "����", "����", "�о�", "����", "�λ�", "������",
            "����", "����", "����", "����", "����", "�ȵ�", "�Ⱦ�",
            "���", "����", "����", "����", "�ϵ�", "�︪��", "���",
            "����", "����", "�Ǽ�", "�̸�", "��õ", "����", "����",
            "������", "����", "����", "â��", "õ��", "ö��", "��ǳ��",
            "��õ", "�湫", "����", "����", "�س�"};
>>>>>>> 74e36130a3fcc92f0827b2ea0158af9b84286030

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logoutBtn = (Button)findViewById(R.id.logout);
        logoutBtn.setOnClickListener(this);
        permissionBtn = (Button)findViewById(R.id.permission);
        permissionBtn.setOnClickListener(this);

        areaList = (Spinner)findViewById(R.id.area_list);
<<<<<<< HEAD
        areaList.setPrompt("�??�� ?��?��");
=======
        areaList.setPrompt("���� ����");
>>>>>>> 74e36130a3fcc92f0827b2ea0158af9b84286030
        areaList.setOnItemSelectedListener(this);
        adapterSpin = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, areaData);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaList.setAdapter(adapterSpin);

        areaList.setSelection(15);      // default�? = ?��?��

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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "NothingSelected", Toast.LENGTH_SHORT).show();
    }
}