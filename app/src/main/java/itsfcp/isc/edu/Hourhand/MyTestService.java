package itsfcp.isc.edu.Hourhand;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import itsfcp.isc.edu.Hourhand.entidades.agenda;

public class MyTestService extends Service {
    public static final int REQUEST_CODE = 12345;
    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 1010;
    Database conn;
    private Context context = this;
    ArrayList<agenda> listaagendas;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // START YOUR TASKS

        final Handler mHandler = new Handler();
        Runnable mTicker = new Runnable() {
            @Override
            public void run() {
                notificacion();
                //Toast.makeText(context, "El servicio ha Iniciado", Toast.LENGTH_LONG).show();
                mHandler.postDelayed(this, 30000);
            }
        };
        mHandler.postDelayed(mTicker, 30000);

        return START_STICKY;
    }
    private void notificacion(){
        conn = new Database(context, "BDHorario", null, 1);
        Calendar calendario = Calendar.getInstance();
        String cadenaF, cadenaH, fecha_sistema, hora_sistema;
        int hora, min, dia, mes, ano;
        Date d = new Date();
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        min = calendario.get(Calendar.MINUTE);
        SimpleDateFormat fecc = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fecc2 = new SimpleDateFormat("yyyy-MM-dd");
        String fechacComplString = fecc.format(d);
        SimpleDateFormat horas = new SimpleDateFormat("HH:mm");
        SimpleDateFormat hora1 = new SimpleDateFormat("HH:mm");
        String horaComplString = horas.format(d);
        fecha_sistema = fecc2.format(d);
        hora_sistema = hora1.format(d);

        SQLiteDatabase db = conn.getReadableDatabase();

        agenda agendas = null;
        listaagendas = new ArrayList<>();

        Cursor cursor = db.rawQuery(" select * from agenda where fecha_agenda='" + fecha_sistema + "' AND hora_agenda= '" + hora_sistema + "'", null);


        if (cursor.moveToNext()) {
            agendas = new agenda();
            agendas.setTitulo_agenda(cursor.getString(1));
            agendas.setDecrip_agenda(cursor.getString(4));
            listaagendas.add(agendas);
            triggerNotification(context,MyTestService.class,cursor.getString(1) + "\n" + cursor.getString(4));
        }
        db.close();

    }
    private void triggerNotification(Context contexto,Class<?> cls, String t) {
        Intent notificationIntent = new Intent(contexto, ResumenActivity.class);
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(contexto, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long firstMillis = System.currentTimeMillis();
        /******#Repetir una vez#******/
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMillis, AlarmManager.INTERVAL_DAY, contentIntent);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{2000, 1000, 2000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);
        builder.setContentIntent(contentIntent)

                .setTicker("")
                .setContentTitle("NUEVA NOTIFICACION")
                .setContentTitle("")
                .setContentText(t)
                .setContentInfo("Info")
                .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true) //Cuando se pulsa la notificación ésta desaparece
                .setSound(defaultSound)
                .setVibrate(pattern);

        Notification notificacion = new NotificationCompat.BigTextStyle(builder)
                .bigText(t)
                .setBigContentTitle("NUEVA NOTIFICACION")
                .setSummaryText("Resumen de tareas")
                .build();

        notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificacion);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}