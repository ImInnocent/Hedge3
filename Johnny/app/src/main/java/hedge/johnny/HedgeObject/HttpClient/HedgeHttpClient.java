package hedge.johnny.HedgeObject.HttpClient;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Gyu on 2015-08-16.
 */


public class HedgeHttpClient {
    private static final HedgeHttpClient instance = new HedgeHttpClient();
    private static DefaultHttpClient client = new DefaultHttpClient();
    private static String userid = "", password = "";
    public static HedgeHttpClient GetInstance()
    {
        return instance;
    }

    private HedgeHttpClient()
    {

    }

    private static JSONObject GetResult(RequestT t)
    {
        JSONObject result;
        try {
            result = t.get();
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    public static void addValues(JSONObject jsonObject, String l, String r)
    {
        try {
            jsonObject.accumulate(l, r);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getValues(JSONObject jsonObject, String key)
    {
        Object s = "";
        try {
            s = jsonObject.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            s = "Fail";
        }
        finally {
            return s.toString();
        }
    }

    public static JSONObject getObject(JSONObject jsonObject, String key)
    {
        JSONObject s = new JSONObject();
        try {
            s = jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            s = null;
        }
        finally {
            return s;
        }
    }

    public void SetAccount(String userid, String password)
    {
        this.userid = userid;
        this.password = password;
    }

    public static JSONObject HedgeRequest(String func, JSONObject jsonObject)
    {
        RequestT t = new RequestT();
        try {
            jsonObject.accumulate("func", func);
            jsonObject.accumulate("userid", userid);
            jsonObject.accumulate("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        t.execute(jsonObject);

        return GetResult(t);
    }

}

class RequestT extends AsyncTask<JSONObject, Void, JSONObject>{
    @Override
    protected JSONObject doInBackground(JSONObject... params){
            return (JSONObject)Request(params[0]);
        }

    private JSONObject Request(JSONObject jsonObject)
    {
        String result = "";

        try {
            //String url = "http://1.231.69.29:5000/" + jsonObject.getString("func");
            String url = "http://hglim.me:2025/" + jsonObject.getString("func");

            HttpPost httppost = new HttpPost(url);

            StringEntity se = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            //contentType: 'application/json;charset=UTF-8',
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httppost);
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while((line = br.readLine()) != null){
                result += line;
            }

        }catch(Exception e){
            e.printStackTrace();
            result = "Fail";
        }

        JSONObject jObj = null;
        try {
            jObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jObj == null)
        {
            jObj = new JSONObject();
            try {
                jObj.accumulate("result","100");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jObj;
    }
}