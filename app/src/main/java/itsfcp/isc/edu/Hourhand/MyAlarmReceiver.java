package itsfcp.isc.edu.Hourhand;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import itsfcp.isc.edu.Hourhand.entidades.agenda;

import static itsfcp.isc.edu.Hourhand.NotificationScheduler.EXTRA_ARCHIVO;


public class MyAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private LoadLocalData loadLocalData;
    private int count;
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        String nombre = intent.getStringExtra(EXTRA_ARCHIVO);

        loadLocalData = new LoadLocalData(context, nombre);

        Log.d(TAG, "Estoy recibiendo este nombre " + nombre);

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, MyAlarmReceiver.class, loadLocalData.get_year(), loadLocalData.get_month(), loadLocalData.get_day(), loadLocalData.get_hour(), loadLocalData.get_min(), loadLocalData.get_request());
                //NotificationScheduler.cancelReminder(context, AlarmReceiver.class, loadLocalData.get_request());
                return;
            }
        }
        Log.d(TAG, "onReceive: ");

        NotificationScheduler.showNotification(context, ResumenActivity.class,
                loadLocalData.get_title(), loadLocalData.get_desc(), loadLocalData.get_request());

    }
}