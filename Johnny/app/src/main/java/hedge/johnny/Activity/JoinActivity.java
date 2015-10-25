package hedge.johnny.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.R;

public class JoinActivity extends Activity implements View.OnClickListener {
    Button joinBtn, backBtn;
    EditText username, userid, password, phonenum;
    String errorMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        username = (EditText)findViewById(R.id.username);
        userid = (EditText)findViewById(R.id.userid);
        password = (EditText)findViewById(R.id.password);
        phonenum = (EditText)findViewById(R.id.phonenum);

        joinBtn = (Button)findViewById(R.id.join);
        joinBtn.setOnClickListener(this);

        backBtn = (Button)findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        // 필터
        // 필터
        username.setFilters(new InputFilter[]   // max 10글자, 한글만
                {filterKor, new InputFilter.LengthFilter(10)});
        userid.setFilters(new InputFilter[]   // max 18글자, 영문+숫자
                {filterEngNum, new InputFilter.LengthFilter(18)});
        password.setFilters(new InputFilter[]   // max 10글자, 영문+숫자
                {filterNum, new InputFilter.LengthFilter(10)});
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back)
        {
            finish();
            return;
        }

        if(checkEditText() == false)
            return;

        String[] result = new String[2];

        // 회원가입
        JSONObject jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject, "newname",username.getText().toString());
        HedgeHttpClient.addValues(jsonObject, "newid",userid.getText().toString());
        HedgeHttpClient.addValues(jsonObject, "newpassword",password.getText().toString());
        HedgeHttpClient.addValues(jsonObject, "newphonenum",phonenum.getText().toString());
        jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("insert_member", jsonObject);
        String msg, tmsg;

        String code = HedgeHttpClient.getValues(jsonObject, "result");

        if(code.equals("1"))
        {
            tmsg = "축하합니다!";
            msg = HedgeHttpClient.getValues(jsonObject,"username")+"님 환영합니다.";
        }
        else
        {
            tmsg = "가입실패";
            msg = "네트워크 및 입력한 정보를 확인해주세요";
        }

        new AlertDialog.Builder(this)
                .setTitle(tmsg)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // 한글
    public InputFilter filterKor = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅣ가-힣]*$");
            if(!ps.matcher(source).matches()) {
                errorMsg = "한글만 입력 가능합니다.";
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };

    // 영문 + 숫자
    public InputFilter filterEngNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if(!ps.matcher(source).matches()) {
                errorMsg = "영문자와 숫자의 조합이어야 합니다.";
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };

    // 영문 + 숫자
    public InputFilter filterNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[0-9]+$");
            if(!ps.matcher(source).matches()) {
                errorMsg = "숫자만 입력 가능합니다.";
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };

    boolean checkEditText(){
        if(username.getText().toString().length() < 1)   // 이름은 1자~10자
            errorMsg = "이름은 최소 1자 이상 써야 합니다.";
        else if(userid.getText().toString().length() < 5)     // 아이디는 5자~18자
            errorMsg = "아이디는 최소 5자 이상 써야 합니다.";
        else if(password.getText().toString().length() < 5)
            errorMsg = "비밀번호는 최소 5자 이상 써야 합니다.";
        else if(phonenum.getText().toString().length() < 10)
            errorMsg = "전화번호는 10자리 이상 써야 합니다.";

        if(errorMsg != "")
        {
            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
            errorMsg = "";
            return false;
        }
        else
            return true;
    }
}
