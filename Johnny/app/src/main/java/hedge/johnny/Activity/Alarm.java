package hedge.johnny.Activity;

/**
 * Created by Administrator on 2015-08-06.
 */
public class Alarm {
    private int db_Id;
    private boolean ampm;   // 0이면 오전, 1이면 오후
    private int hour, min;
    private boolean mon, tue, wed, thu, fri, sat, sun;
    private boolean onoff;
    private String title, sender, receiver;
    private int type;
    private String identifier;

    Alarm(int db_Id, boolean ampm, int hour, int min, boolean[] days, boolean onoff, String title, String sender, String receiver, int type, String identifier)
    {
        this.db_Id = db_Id;
        this.ampm = ampm;
        this.hour = hour;
        this.min = min;
        this.mon = days[0];        this.tue = days[1];        this.wed = days[2];
        this.thu = days[3];        this.fri = days[4];        this.sat = days[5];       this.sun = days[6];
        this.onoff = onoff;
        this.title = title;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.identifier = identifier;
    }

    String getIdentifier() {return identifier;}

    int getType() {return type; }

    String getTitle(){
        return title;
    }

    String getSender(){
        return sender;
    }

    String getReceiver(){
        return receiver;
    }

    int getDb_Id() {return db_Id;}

    boolean getAmPm(){
        return ampm;
    }

    String getTime(){
        String str;
        if(min < 10)
            str = new String( hour + ":0" + min );
        else
            str = new String( hour + ":" + min );

        return str;
    }

    boolean[] getDays(){
        boolean[] days = {mon, tue, wed, thu, fri, sat, sun};
        return days;
    }

    boolean getOnOff(){
        return onoff;
    }

    void setOnOff(boolean onoff) {
        this.onoff = onoff;
    }
}
