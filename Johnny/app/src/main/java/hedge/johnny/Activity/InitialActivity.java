package hedge.johnny.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import hedge.johnny.HedgeMessage.QuickstartPreferences;
import hedge.johnny.HedgeMessage.RegistrationIntentService;
import hedge.johnny.HedgeObject.AlarmBackgroundService;
import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.R;

/**
 * Created by Administrator on 2015-08-08.
 */
public class InitialActivity extends Activity {
    Timer mTimer;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
                String id = pref.getString("userid", "");
                String pw = pref.getString("password", "");
                HedgeHttpClient.GetInstance().SetAccount(id,pw);

                //if(true)
                if(id.equals("")==false && pw.equals("")==false) //  저장된 아이디가 있음
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("ensure_member", jsonObject);

                    if(HedgeHttpClient.getValues(jsonObject,"result").equals("1")) // 저장된 아이디가 로그인에 성공하면 메인액티비티로
                    {
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("username", HedgeHttpClient.getValues(jsonObject, "username"));
                        edit.putString("phonenum", HedgeHttpClient.getValues(jsonObject, "phonenum"));
                        edit.commit();

                        //디바이스 등록
                        registDevice();

                        Intent i = new Intent(InitialActivity.this, MainActivity.class);
                        startActivity(i);

                        Intent s = new Intent(InitialActivity.this, AlarmBackgroundService.class);
                        startService(s);

                        finish(); return;
                    }
                    else // 저장된 아이디가 있으나 로그인에 실패하면 로그인시도
                    {
                        Intent i = new Intent(InitialActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish(); return;
                    }

                }
                else // 저장된 아이디가 없으면 로그인시도
                {
                    Intent i = new Intent(InitialActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish(); return;
                }

            }
        }, 1200);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void registDevice(){
        //registBroadcastReceiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //토큰값이 들어옴 이걸 서버로 보냄
                String token = intent.getStringExtra("token");
                JSONObject jsonObject = new JSONObject();
                HedgeHttpClient.addValues(jsonObject,"devicekey", token);
                jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("set_device_key",jsonObject);
            }
        };

        //getInstanceIdToken
        if (checkPlayServices()) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(InitialActivity.this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy(){
        mTimer.cancel();
        super.onDestroy();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
