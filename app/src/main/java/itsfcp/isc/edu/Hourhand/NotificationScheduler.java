package itsfcp.isc.edu.Hourhand;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.entidades.notificaciones;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler {

    Switch notificar;
    Switch sonido;
    Switch vibracion;
    public static Integer Var3;
    public static Integer Var1;
    public static Integer Var2;

    static ArrayList<notificaciones> notificaciones;

    public static int DAILY_REMINDER_REQUEST_CODE = 2;
    public static final int numAle = 0;
    public static final String TAG = "NotificationScheduler";

    private static final String CHANNEL_ID = "my_channel_01";
    private static final CharSequence name = "myAlarm";
    private static final int importance = android.app.NotificationManager.IMPORTANCE_HIGH;
    public static final String EXTRA_ARCHIVO = "archivo";

    public static void setReminder(Context context, Class<?> cls, int year, int month, int day, int hour, int min, int request)
    {
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.setTimeInMillis(System.currentTimeMillis());
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.YEAR, year);
        setcalendar.set(Calendar.MONTH, month);
        setcalendar.set(Calendar.DAY_OF_MONTH, day);

        String nombre = day + "-" + month + "-" + year + "-" + hour + ":" + min;
        Log.d(TAG, "Este es mi resultado" + nombre);

        //cancelReminder(context,cls);

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        DAILY_REMINDER_REQUEST_CODE = request;


        Intent intent1 = new Intent(context, cls);
        intent1.putExtra(EXTRA_ARCHIVO, nombre);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        /******#Repetir una vez#******/
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        /******#Repetir cada dos min, si no se desactiva la alarma#******/
        //am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
        //      setcalendar.getTimeInMillis(), 1000 * 60 * 2,  pendingIntent);

    }

    public static void cancelReminder(Context context,Class<?> cls, int request)
    {

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, request, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context, Class<?> cls, String title, String content, int request)
    {
            notificacion(context);
            if (Var1 ==1) {
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Intent notificationIntent = new Intent(context, cls);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(cls);
                stackBuilder.addNextIntent(notificationIntent);

                DAILY_REMINDER_REQUEST_CODE = request;

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                long[] pattern = new long[]{2000, 1000, 2000};
                NotificationCompat.Builder notification = builder.setContentTitle(title);
                notification.setContentText(content);
                notification.setAutoCancel(true);
                if (Var2 == 1) {
                    notification.setSound(alarmSound);
                }
                if (Var3 == 1) {
                    notification.setVibrate(pattern);
                }
                notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setContentIntent(pendingIntent).build();

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mNotificationManager.createNotificationChannel(mChannel);
                    builder.setChannelId(CHANNEL_ID);
                }
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification.build());

                NotificationScheduler.cancelReminder(context, MyAlarmReceiver.class, request);
            }
            else{
                NotificationScheduler.cancelReminder(context, MyAlarmReceiver.class, request);
                Toast.makeText(context, "No fue posible notificar su Recordatorio, Porfavor habilite la opcion notificar", Toast.LENGTH_LONG).show();
            }

    }
    private static void notificacion(Context context) {
        Database conn = new Database(context, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        notificaciones notifi = null;
        notificaciones = new ArrayList<>();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + utilidades.TABLA_NOTIFICACIONES, null);

        while (cursor.moveToNext()) {
            Var1 = Integer.valueOf(cursor.getString(1));
            Var2 =Integer.valueOf(cursor.getString(2));
            Var3 =Integer.valueOf(cursor.getString(3));
        }
    }
}
