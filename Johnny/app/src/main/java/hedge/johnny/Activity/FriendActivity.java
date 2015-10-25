package hedge.johnny.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.HedgeObject.Navigators.NavigationActivity;
import hedge.johnny.R;

/**
 * Created by Administrator on 2015-07-21.
 */
public class FriendActivity extends NavigationActivity implements AdapterView.OnItemClickListener{

    FriendAdapter adapter;
    ArrayList<String[]> array;
    ListView listView;
    public static String tag = "-1";
    //ImageButton btn_add, btn_del, btn_modi;
    int selectPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        init();
        refresh();
    }

    public void refresh(){
        array.clear();

        JSONObject jsonObject = new JSONObject();
        jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("friend_list",jsonObject);

        for(int i=0; i<jsonObject.length()-1; i++)
        {
            JSONObject iter = HedgeHttpClient.getObject(jsonObject,String.valueOf(i));
            String[] d = new String[2];
            d[0] = HedgeHttpClient.getValues(iter,"friendname");
            d[1] = HedgeHttpClient.getValues(iter,"friendid");
            array.add(i,d);
        }

        listView.setAdapter(adapter);
    }

    void init()
    {
        selectPos = -1; // 변수 초기화

        listView = (ListView)findViewById(R.id.friendList);
        listView.setDivider(new ColorDrawable(Color.WHITE)); // 리스트내 아이템간 경계선
        listView.setDividerHeight(5);

        // 데이터 삽입
        array = new ArrayList<String[]>();

        // 배열 연결
        adapter = new FriendAdapter(this, R.layout.friend_row, array);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), array.get(position)[0], Toast.LENGTH_SHORT).show();
        selectPos = position;
    }

    public void btnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_add:
            {
                //Toast.makeText(getApplicationContext(), "친구 추가", Toast.LENGTH_SHORT).show();
                // 친구 추가 다이얼로그 띄우기
                Intent intent = new Intent(FriendActivity.this, AddFriendActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                goNextHistory("AddFriendActivity", intent);
            }
                break;
            case R.id.btn_del:
               // HedgeHttpClient.GetInstance().DeleteFriend(id, pw, v.getTag().toString());
                JSONObject jsonObject = new JSONObject();
                HedgeHttpClient.addValues(jsonObject,"friendid",v.getTag().toString());
                jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("delete_friend",jsonObject);
                Toast.makeText(getApplicationContext(), v.getTag().toString() + "님과 친구를 끊었습니다.", Toast.LENGTH_LONG).show();
                refresh();
                break;
            case R.id.btn_modify:
                Intent intent = new Intent(FriendActivity.this, SendAlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.putExtra("friend_id", v.getTag().toString());
                tag = v.getTag().toString();
                goNextHistory("SendAlarmActivity", intent);
                //Toast.makeText(getApplicationContext(), (Integer)v.getTag() + " mod", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_msg:
                // 친구에게 메세지 보내기
                MyDialog dlg = new MyDialog(v.getTag().toString());                  // dialog 호출
                dlg.show(getParent().getFragmentManager(), "alert");
                break;
            default:
                break;
        }
    }
}