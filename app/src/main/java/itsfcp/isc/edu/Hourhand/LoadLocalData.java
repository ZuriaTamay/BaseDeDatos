package itsfcp.isc.edu.Hourhand;

import android.content.Context;
import android.content.SharedPreferences;

public class LoadLocalData {

    private String APP_SHARED_PREFS;

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static final String reminderStatus="reminderStatus";
    private static final String title = "title";
    private static final String desc = "desc";
    private static final String hour = "hour";
    private static final String min = "min";
    private static final String year = "year";
    private static final String month = "month";
    private static final String day = "day";
    private static final String request = "request";

    public LoadLocalData(Context context, String nameFile)
    {
        this.APP_SHARED_PREFS = nameFile;
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public boolean getReminderStatus()
    {
        Boolean re = false;
        if (APP_SHARED_PREFS != null){
            re = true;
        }
        return appSharedPrefs.getBoolean(reminderStatus, re);
    }

    public void setReminderStatus(boolean status)
    {
        prefsEditor.putBoolean(reminderStatus, status);
        prefsEditor.commit();
    }

    public String get_title()
    {
        return appSharedPrefs.getString(title, "Title");
    }

    public void set_title(String t)
    {
        prefsEditor.putString(title, t);
        prefsEditor.commit();
    }


    public String get_desc()
    {
        return appSharedPrefs.getString(desc, "Description");
    }

    public void set_desc(String de)
    {
        prefsEditor.putString(desc, de);
        prefsEditor.commit();
    }

    public int get_hour()
    {
        return appSharedPrefs.getInt(hour, 20);
    }

    public void set_hour(int h)
    {
        prefsEditor.putInt(hour, h);
        prefsEditor.commit();
    }

    public int get_min()
    {
        return appSharedPrefs.getInt(min, 0);
    }

    public void set_min(int m)
    {
        prefsEditor.putInt(min, m);
        prefsEditor.commit();
    }


    public int get_year()
    {
        return appSharedPrefs.getInt(year, 0);
    }

    public void set_year(int y)
    {
        prefsEditor.putInt(year, y);
        prefsEditor.commit();
    }


    public int get_month()
    {
        return appSharedPrefs.getInt(month, 0);
    }

    public void set_month(int me)
    {
        prefsEditor.putInt(month, me);
        prefsEditor.commit();
    }


    public int get_day()
    {
        return appSharedPrefs.getInt(day, 0);
    }

    public void set_day(int d)
    {
        prefsEditor.putInt(day, d);
        prefsEditor.commit();
    }

    public int get_request()
    {
        return appSharedPrefs.getInt(request, 0);
    }

    public void set_request(int r)
    {
        prefsEditor.putInt(request, r);
        prefsEditor.commit();
    }

    public void reset()
    {
        prefsEditor.clear();
        prefsEditor.commit();

    }
}
