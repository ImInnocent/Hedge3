package hedge.johnny.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import hedge.johnny.R;

/**
 * Created by Administrator on 2015-08-03.
 */
public class FriendAdapter extends ArrayAdapter<String[]>{
    ArrayList<String[]> items;
    Context context;

    public FriendAdapter(Context context, int resource, ArrayList<String[]> objects) {
        super(context, resource, objects);

        this.context = context;     // 외부에서 사용하려면 따로 필요해서..
        items = objects;            // 따로 필요해서..
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;   // 각 아이템이 늘어날때, 계속 뷰로 늘리지 않고 swap해준다.
        if(v == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.friend_row, null);
        }

        String p[] = items.get(position);
        if(p != null)
        {
            TextView tx1 = (TextView) v.findViewById(R.id.tx1);
            if( tx1 != null ) {
                tx1.setText(p[0]+" (" + p[1] + ")");
            }
        }

        ImageButton btnMsg = (ImageButton) v.findViewById(R.id.btn_msg);
        ImageButton btnModi = (ImageButton) v.findViewById(R.id.btn_modify);
        ImageButton btnDel = (ImageButton) v.findViewById(R.id.btn_del);

        v.setTag(p[1]);
        btnMsg.setTag(p[1]);
        btnModi.setTag(p[1]);
        btnDel.setTag(p[1]);

        return v;
    }
}
