package hedge.johnny.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Set;

import hedge.johnny.HedgeObject.HedgeDBHelper;
import hedge.johnny.HedgeWidget.TimePickerPreference;
import hedge.johnny.R;

/**
 * Created by EDGE01 on 2015-07-24.
 */
public class SettingAlarmActivity  extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_setting);

        final HedgeDBHelper dbHelper = new HedgeDBHelper(this);
        final PreferenceManager pm = getPreferenceManager();

        Preference cancle_button = (Preference)pm.findPreference("cancle");
        if (cancle_button != null) {
            cancle_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    finish();
                    return true;
                }
            });
        }

        Preference save_button = (Preference)pm.findPreference("save");
        if (save_button != null) {
            save_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    EditTextPreference title = (EditTextPreference)pm.findPreference("title");
                    TimePickerPreference time = (TimePickerPreference)pm.findPreference("time");
                    Preference volume = (Preference)pm.findPreference("volume");
                    MultiSelectListPreference dlist = (MultiSelectListPreference)pm.findPreference("daylistOptions");

                    String dTitle = (String) title.getText();
                    int h = time.lastHour;
                    int m = time.lastMinute;
                    Set<String> d = dlist.getValues();
                    Object[] dayState = d.toArray();

                    ContentValues row = new ContentValues();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    row.put("title", dTitle);
                    row.put("hour", h);
                    row.put("min", m);

                    int[] dayStateInt = new int[7];
                    for(int i=0; i<7; i++) dayStateInt[i] = 0;
                    for(int i=0; i<d.size(); i++)
                        dayStateInt[Integer.parseInt((String) dayState[i])] = 1;
                    for(int i=0; i<7; i++)
                        row.put("d" + i, dayStateInt[i] );

                    db.insert("HedgeAlarm", null, row);

                    CreateAlarm(dayStateInt, h, m);

                    finish();
                    return true;
                }
            });
        }
    }

    private void CreateAlarm(int dayStateInt[], int hour, int minute)
    {
        AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SettingAlarmActivity.this, TimeoutActivity.class);
        intent.putExtra("day_of_week", dayStateInt);    //intent에 요일 on/off정보를 넣음.
        PendingIntent pender = PendingIntent.getActivity(SettingAlarmActivity.this, 0, intent, 0);

        long OneDay = 24 * 60 * 60 * 1000;  //하루를 MilliSecond로 표현

        Calendar current = Calendar.getInstance();
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);
        long cTime = current.getTimeInMillis(); //currentTime = cTime

        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, hour);
        target.set(Calendar.MINUTE, minute);
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);
        long tTime = target.getTimeInMillis();  //targetTime = tTime

        long trigger = cTime > tTime ? tTime + OneDay - cTime : tTime - cTime;

        long intervalTime = OneDay; //하루마다 반복하고, 알람시 켜지는 클래스에서 요일반복을 처리한다.

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, trigger, intervalTime, pender);
    }
}
