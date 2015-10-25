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
    String[] areaData = {"Í∞ïÎ¶â", "Í¥ëÏ£º", "Íµ∞ÏÇ∞", "Íπ?Ï≤?", "??Í¥??†π", "??Íµ?", "???†Ñ",
            "?èô?ï¥", "ÎßàÏÇ∞", "Î™©Ìè¨", "Î∞??ñë", "Î≤åÍµê", "Î∂??Ç∞", "?ÑúÍ∑??è¨",
            "?Ñú?Ç∞", "?Ñú?ö∏", "?Ñ±?Ç®", "?ÜçÏ¥?", "?àò?õê", "?ïà?èô", "?ïà?ñë",
            "?ñë?ñë", "?ó¨?àò", "?òÅ?õî", "?ò§?Ç∞", "?ôÑ?èÑ", "?ö∏Î¶âÎèÑ", "?ö∏?Ç∞",
            "?ö∏Ïß?", "?õêÏ£?", "?ùò?Ü°", "?ù¥Î¶?", "?ù∏Ï≤?", "?†ÑÏ£?", "?†úÏ£?",
            "Î∂ÅÏ†úÏ£?", "ÏßÑÏ£º", "ÏßÑÌï¥", "Ï∞ΩÏõê", "Ï≤úÏïà", "Ï≤†Ïõê", "Ï∂îÌíç?†π",
            "Ï∂òÏ≤ú", "Ï∂©Î¨¥", "Ï∂©Ï£º", "?è¨?ï≠", "?ï¥?Ç®"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logoutBtn = (Button)findViewById(R.id.logout);
        logoutBtn.setOnClickListener(this);
        permissionBtn = (Button)findViewById(R.id.permission);
        permissionBtn.setOnClickListener(this);

        areaList = (Spinner)findViewById(R.id.area_list);
        areaList.setPrompt("Ïß??ó≠ ?Ñ§?†ï");
        areaList.setOnItemSelectedListener(this);
        adapterSpin = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, areaData);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaList.setAdapter(adapterSpin);
        areaList.setSelection(15);      // defaultÍ∞? = ?Ñú?ö∏
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