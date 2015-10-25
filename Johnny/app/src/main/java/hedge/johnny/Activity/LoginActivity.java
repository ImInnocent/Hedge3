package hedge.johnny.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import hedge.johnny.R;

public class LoginActivity extends Activity implements OnClickListener{
    Button registerBtn, loginBtn;
    EditText userid, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerBtn = (Button)findViewById(R.id.register);
        registerBtn.setOnClickListener(this);
        loginBtn = (Button)findViewById(R.id.login);
        loginBtn.setOnClickListener(this);

        userid = (EditText)findViewById(R.id.userid);
        password = (EditText)findViewById(R.id.password);

        new AlertDialog.Builder(this)
                .setTitle("로그인 실패")
                .setMessage("로그인에 실패하였습니다.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
        String id = pref.getString("userid", "");
        String pw = pref.getString("password", "");
        userid.setText(id);
        password.setText(pw);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId() == R.id.register) // 회원가입 눌렀을때
        {
            Intent i = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(i);
        }
        else // 로그인 눌렀을때
        {
            login();
        }
    }

    private void login()
    {
        SharedPreferences pref = getSharedPreferences("HedgeMembers",0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("userid", userid.getText().toString());
        edit.putString("password", password.getText().toString());
        edit.commit();

        Intent i = new Intent(LoginActivity.this, InitialActivity.class);
        startActivity(i);
        finish();
    }

}
