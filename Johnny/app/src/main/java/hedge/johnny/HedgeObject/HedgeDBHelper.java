package hedge.johnny.HedgeObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HedgeDBHelper extends SQLiteOpenHelper
{
    public HedgeDBHelper(Context context)
    {
        super(context, "HedgeDB", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL
                ("CREATE TABLE HedgeAlarm(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "title TEXT," +
                        "hour INTGER," +
                        "min INTGER,"+
                        "d0 BOOLEAN," +
                        "d1 BOOLEAN," +
                        "d2 BOOLEAN," +
                        "d3 BOOLEAN," +
                        "d4 BOOLEAN," +
                        "d5 BOOLEAN," +
                        "d6 BOOLEAN," +
                        "weather BOOLEAN," +
                        "alarm_type INTGER," +
                        "on_off BOOLEAN," +
                        "repeat BOOLEAN);"
                );
        //db.execSQL("INSERT INTO HedgeAlarm VALUES (null, 'title', 3,10, 1, 0, 1, 0, 1, 0, 1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS HedgeAlarm");
        onCreate(db);
    }
}
