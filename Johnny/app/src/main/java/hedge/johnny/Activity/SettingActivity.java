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
    String[] areaData = {"ê°•ë¦‰", "ê´‘ì£¼", "êµ°ì‚°", "ê¹?ì²?", "??ê´?? ¹", "??êµ?", "??? „",
            "?™?•´", "ë§ˆì‚°", "ëª©í¬", "ë°??–‘", "ë²Œêµ", "ë¶??‚°", "?„œê·??¬",
            "?„œ?‚°", "?„œ?š¸", "?„±?‚¨", "?†ì´?", "?ˆ˜?›", "?•ˆ?™", "?•ˆ?–‘",
            "?–‘?–‘", "?—¬?ˆ˜", "?˜?›”", "?˜¤?‚°", "?™„?„", "?š¸ë¦‰ë„", "?š¸?‚°",
            "?š¸ì§?", "?›ì£?", "?˜?†¡", "?´ë¦?", "?¸ì²?", "? „ì£?", "? œì£?",
            "ë¶ì œì£?", "ì§„ì£¼", "ì§„í•´", "ì°½ì›", "ì²œì•ˆ", "ì² ì›", "ì¶”í’? ¹",
            "ì¶˜ì²œ", "ì¶©ë¬´", "ì¶©ì£¼", "?¬?•­", "?•´?‚¨"};
=======
    String[] areaData = {"°­¸ª", "±¤ÁÖ", "±º»ê", "±èÃµ", "´ë°ü·É", "´ë±¸", "´ëÀü",
            "µ¿ÇØ", "¸¶»ê", "¸ñÆ÷", "¹Ð¾ç", "¹ú±³", "ºÎ»ê", "¼­±ÍÆ÷",
            "¼­»ê", "¼­¿ï", "¼º³²", "¼ÓÃÊ", "¼ö¿ø", "¾Èµ¿", "¾È¾ç",
            "¾ç¾ç", "¿©¼ö", "¿µ¿ù", "¿À»ê", "¿Ïµµ", "¿ï¸ªµµ", "¿ï»ê",
            "¿ïÁø", "¿øÁÖ", "ÀÇ¼Û", "ÀÌ¸®", "ÀÎÃµ", "ÀüÁÖ", "Á¦ÁÖ",
            "ºÏÁ¦ÁÖ", "ÁøÁÖ", "ÁøÇØ", "Ã¢¿ø", "Ãµ¾È", "Ã¶¿ø", "ÃßÇ³·É",
            "ÃáÃµ", "Ãæ¹«", "ÃæÁÖ", "Æ÷Ç×", "ÇØ³²"};
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
        areaList.setPrompt("ì§??—­ ?„¤? •");
=======
        areaList.setPrompt("Áö¿ª ¼³Á¤");
>>>>>>> 74e36130a3fcc92f0827b2ea0158af9b84286030
        areaList.setOnItemSelectedListener(this);
        adapterSpin = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, areaData);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaList.setAdapter(adapterSpin);
<<<<<<< HEAD
        areaList.setSelection(15);      // defaultê°? = ?„œ?š¸
=======
        areaList.setSelection(15);      // default°ª = ¼­¿ï
>>>>>>> 74e36130a3fcc92f0827b2ea0158af9b84286030
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