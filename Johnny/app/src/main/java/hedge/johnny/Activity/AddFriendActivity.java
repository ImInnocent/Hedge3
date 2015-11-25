package hedge.johnny.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import hedge.johnny.HedgeObject.HttpClient.HedgeHttpClient;
import hedge.johnny.HedgeObject.Navigators.NavigationActivity;
import hedge.johnny.R;
/**
 * Created by Administrator on 2015-07-21.
 */

public class AddFriendActivity extends NavigationActivity implements OnClickListener {
    EditText friend_id;
    Button add_friend;
    AddFriendAdapter adapter1, adapter2;

    ListView list1, list2;
    //to_me가 내게 온 요청, to_him이 내가 보낸 요청
    ArrayList<String[]> to_me, to_him;

    ArrayList<Integer> mSelectedItems;
    ArrayList<String> mPhoneList;

    final int TYPE_PHONENUM = 1;
    final int TYPE_NAME = 2;


    void  launchAddItems() {
        ArrayList<String> numList;
        numList = getContactList(TYPE_PHONENUM);
        mPhoneList.clear();

        JSONObject jsonObject = new JSONObject();
        for(int i=0; i < numList.size(); i++)
            HedgeHttpClient.addValues(jsonObject, "phone_list", numList.get(i));

        jsonObject = HedgeHttpClient.HedgeRequest("get_phone_list", jsonObject);
        numList.clear();

        for(int i=0; i < jsonObject.length() - 1; i++){
            String s[] = HedgeHttpClient.getValues(jsonObject, String.valueOf(i)).split(",");
            mPhoneList.add(s[1]);
            numList.add(s[0] + "(" + s[1] + ")");
        }


        //if(mPhoneList.isEmpty())
        //    mPhoneList= getSelectedList(numList);

        if(!mSelectedItems.isEmpty())
            mSelectedItems.clear();

        String items[] = new String[numList.size()];
        items = numList.toArray(items);

        AlertDialog.Builder ab=new AlertDialog.Builder(getParent());
        ab.setTitle("친구 추가")
                .setMultiChoiceItems(items, null,  new DialogInterface.OnMultiChoiceClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked){
                            mSelectedItems.add(which);
                        }
                        else if(mSelectedItems.contains(which)){
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }

                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog

                for(int i=0; i < mSelectedItems.size(); i++){
                    int sp = mSelectedItems.get(i);
                    JSONObject jsonObject = new JSONObject();
                    HedgeHttpClient.addValues(jsonObject, "friendid", mPhoneList.get(sp));
                    jsonObject = HedgeHttpClient.HedgeRequest("request_friend", jsonObject);
                }

                Invalidate();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mSelectedItems.clear();
            }
        });

        ab.create();;
        ab.show();
    }

    private ArrayList<String> getSelectedList(ArrayList list) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> phoneList = new ArrayList<String>();

        if (contactCursor.moveToFirst()) {
            do {
                nameList.add(contactCursor.getString(1));
                phoneList.add(contactCursor.getString(0));

            } while (contactCursor.moveToNext());
        }

        ArrayList<String> contactlist = new ArrayList<String>();

        int idx = 0;
        for(int i=0; i < nameList.size(); i++){
            if(phoneList.get(idx).equals(phoneList.get(i))){
                contactlist.add(nameList.get(idx));
                idx++;
            }
        }

        return contactlist;
    }

    private ArrayList<String> getContactList(int type) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER }; // 연락처 이름.

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = managedQuery(uri, projection, null,
                selectionArgs, sortOrder);

        ArrayList<String> contactlist = new ArrayList<String>();

        if (contactCursor.moveToFirst()) {
            do {
                contactlist.add(contactCursor.getString(0));
            } while (contactCursor.moveToNext());
        }

        return contactlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        friend_id = (EditText)findViewById(R.id.friend_id);
        add_friend = (Button)findViewById(R.id.add_friend);
        add_friend.setOnClickListener(this);
        mSelectedItems = new ArrayList();
        mPhoneList = new ArrayList<String>();

        //리스트 init
        list1 = (ListView)findViewById(R.id.friend_to_him);
        list2 = (ListView)findViewById(R.id.friend_to_me);

        list1.setDivider(new ColorDrawable(Color.WHITE)); // 리스트내 아이템간 경계선
        list1.setDividerHeight(5);
        list2.setDivider(new ColorDrawable(Color.WHITE)); // 리스트내 아이템간 경계선
        list2.setDividerHeight(5);

        to_him = new ArrayList<String[]>();
        to_me = new ArrayList<String[]>();

        adapter1 = new AddFriendAdapter(this,this, R.layout.add_friend_row, to_him, 1);
        adapter2 = new AddFriendAdapter(this,this, R.layout.add_friend_row, to_me, 2);

        refresh();
    }

    public void refresh(){
        to_him.clear();
        to_me.clear();
        Invalidate();
    }

    private void Invalidate()
    {
        SharedPreferences pref = getSharedPreferences("HedgeMembers", 0);
        String id = pref.getString("userid", "None");
        String pw = pref.getString("password", "None");

        JSONObject jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject, "sended", "1");
        JSONObject sended = HedgeHttpClient.HedgeRequest("friend_request_list",jsonObject);
        jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject,"sended","0");
        JSONObject receieved = HedgeHttpClient.HedgeRequest("friend_request_list",jsonObject);

        for(int i=0; i<sended.length()-1; i++)
        {
            JSONObject iter = HedgeHttpClient.getObject(sended,String.valueOf(i));
            String[] d = new String[2];
            d[0] = HedgeHttpClient.getValues(iter,"friendname");
            d[1] = HedgeHttpClient.getValues(iter,"friendid");
            to_him.add(i,d);
        }

        for(int i=0; i<receieved.length()-1; i++)
        {
            JSONObject iter = HedgeHttpClient.getObject(receieved,String.valueOf(i));
            String[] d = new String[2];
            d[0] = HedgeHttpClient.getValues(iter,"friendname");
            d[1] = HedgeHttpClient.getValues(iter,"friendid");
            to_me.add(i,d);
        }

        list1.setAdapter(adapter1);
        list2.setAdapter(adapter2);
    }

    private void RequestFriend()
    {
        JSONObject jsonObject = new JSONObject();
        HedgeHttpClient.addValues(jsonObject,"friendid",friend_id.getText().toString());
        jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("request_friend",jsonObject);

        EditText a = (EditText)findViewById(R.id.friend_id);
        a.setText("");
    }

    public void btnClickAddFriend(View v){
        JSONObject jsonObject;
        switch (v.getId()){
            case R.id.btn_cancel:
                //취소 버튼
                jsonObject = new JSONObject();
                HedgeHttpClient.addValues(jsonObject,"friendid",v.getTag().toString());
                jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("delete_friend",jsonObject);
                Toast.makeText(getApplicationContext(), v.getTag().toString() + "님께 보낸 친구요청을 취소했습니다.", Toast.LENGTH_LONG).show();
                refresh();
                break;
            case R.id.btn_accept:
                //수락버튼
                String friendid = (String) v.getTag();
               // HedgeHttpClient.AcceptFriend(id, pw, friendid);
                jsonObject = new JSONObject();
                HedgeHttpClient.addValues(jsonObject,"friendid",friendid);
                jsonObject = HedgeHttpClient.GetInstance().HedgeRequest("accept_friend",jsonObject);
                refresh();
                break;
            case R.id.phone_book:
                launchAddItems();
                break;
        }
    }

    @Override
    public void onClick(View v) {
       // result = HedgeHttpClient.GetInstance().FindMember(friend_id.getText().toString()).split(",");

        JSONObject jsonObject = new JSONObject();
        String memberid = friend_id.getText().toString();
        HedgeHttpClient.addValues(jsonObject, "memberid", memberid);
        JSONObject result = HedgeHttpClient.GetInstance().HedgeRequest("find_member",jsonObject);

        if(HedgeHttpClient.getValues(result,"result").equals("1"))
        {
            new AlertDialog.Builder(MainActivity.MainContext)
                    .setTitle("확인")
                    .setMessage("이름 : " + HedgeHttpClient.getValues(result,"username") + "\n위 정보와 일치합니까?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            RequestFriend();
                            refresh();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
            new AlertDialog.Builder(MainActivity.MainContext)
                    .setTitle("네트워크 오류 혹은 친구를 찾을 수 없습니다")
                    .setMessage("입력한 친구 아이디를 확인해주세요.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}

class AddFriendAdapter extends ArrayAdapter<String[]> implements OnClickListener{
    ArrayList<String[]> items;
    Context context;
    AddFriendActivity addFriendActivity;
    //1이면 보낸 알람, 2이면 받은 알람
    int type;

    public AddFriendAdapter(AddFriendActivity addFriendActivity, Context context, int resource, ArrayList<String[]> objects, int type) {
        super(context, resource, objects);

        this.type = type;
        this.context = context;     // 외부에서 사용하려면 따로 필요해서..
        items = objects;            // 따로 필요해서..
        this.addFriendActivity = addFriendActivity;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;   // 각 아이템이 늘어날때, 계속 뷰로 늘리지 않고 swap해준다.
        if(v == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.add_friend_row, null);
        }

        String[] p = items.get(position);
        if(p != null)
        {
            TextView tx1 = (TextView) v.findViewById(R.id.tx2);
            if( tx1 != null ) {
                tx1.setText(p[0] + " (" + p[1] + ")");
            }
        }

        Button btn;

        if(type == 2){
            btn = (Button) v.findViewById(R.id.btn_accept);
            btn.setText("");
            btn = (Button) v.findViewById(R.id.btn_cancel);
            btn.setText("|  취소  ");
        }
        else{
            btn = (Button) v.findViewById(R.id.btn_cancel);
            btn.setText("");
            btn = (Button) v.findViewById(R.id.btn_accept);
            btn.setText("|  수락  ");
        }

        btn.setTag(p[1]);
        //btn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        addFriendActivity.btnClickAddFriend(v);
    }

    public void btnClickAddFriend(View v){
        switch (v.getId()){
            case R.id.btn_cancel:
                //취소 버튼
                break;
            case R.id.btn_accept:
                //수락버튼
                String tag = (String) v.getTag();
                break;
        }
    }
}