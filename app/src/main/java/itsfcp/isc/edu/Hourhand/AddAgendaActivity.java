package itsfcp.isc.edu.Hourhand;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import itsfcp.isc.edu.Hourhand.adapters.AgendaAdapter;
import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

public class AddAgendaActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddAgendaActivity";
    private LoadLocalData loadLocalData;
    private ClipboardManager myClipboard;

    private agenda agendas;
    int diaa;
    int mess;
    int añoo;
    int horaa;
    int minutoss;

    ImageButton salir;
    EditText campotitulo, campodescripcion;
    String campotitulos, campodescripcions;
    TextView campohora, campofecha;
    Button btn_fecha, btn_hora, btn_guardar;
    TextView selecciona_fecha, selecciona_hora;
    public agenda agend;
    agenda agendaInstance;
    String idagenda;
    private AgendaFragment agendaFragment;

    private int dia, mes, ano, hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadLocalData = new LoadLocalData(getApplicationContext(), null);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //CONEXION DE BOTONES
        campotitulo = (EditText) findViewById(R.id.añade_titulo);
        campohora = (TextView) findViewById(R.id.selecciona_hora);
        campofecha = (TextView) findViewById(R.id.selecciona_fecha);
        campodescripcion = (EditText) findViewById(R.id.descripcion);

        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        salir = (ImageButton) findViewById(R.id.btn_salir);
        btn_fecha = (Button) findViewById(R.id.btn_fecha);
        btn_hora = (Button) findViewById(R.id.btn_hora);
        selecciona_fecha = (TextView) findViewById(R.id.selecciona_fecha);
        selecciona_hora = (TextView) findViewById(R.id.selecciona_hora);
        btn_fecha.setOnClickListener(this);
        btn_hora.setOnClickListener(this);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campodescripcion.getText().toString().equals("") || campotitulo.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Ha dejado campos vacios", Toast.LENGTH_LONG).show();
                }else{
                    setAlarm();
                    guardardatos();
                    Intent intent = new Intent(AddAgendaActivity.this,ResumenActivity.class);
                    intent.putExtra("Check",1);
                    startActivity(intent);

                }
                    }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    getFragmentManager().popBackStack();
                } else {
                    getFragmentManager().popBackStack();
                }finish();
            }
        });

        campotitulos = loadLocalData.get_title();
        campodescripcions = loadLocalData.get_desc();
        hora= loadLocalData.get_hour();
        minutos = loadLocalData.get_min();
        ano = loadLocalData.get_year();
        mes = loadLocalData.get_month();
        dia = loadLocalData.get_day();

        // Consultar Instancia
        agendaInstance = agenda.getInstance();
        if (!agendaInstance.getIdNull()) {
            setValues();
        }
    }

   private void setValues() {
        campotitulo.setText(agendaInstance.getTitulo_agenda());
        campofecha.setText(agendaInstance.getFecha_agenda());
        campohora.setText(agendaInstance.getHora_agenda());
        campodescripcion.setText(agendaInstance.getDecrip_agenda());
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            getFragmentManager().popBackStack();
        } else {
            getFragmentManager().popBackStack();
        }
        finish();

    }

    private void guardardatos() {
        Database conn = new Database(this, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        //obtener los valores

        String dateString = campofecha.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(dateString);
            dateString = dateFormat2.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String TimeString = campohora.getText().toString();
        ContentValues values = new ContentValues();
        values.put(utilidades.CAMPO_TITULO, campotitulo.getText().toString());
        values.put(utilidades.CAMPO_FECHA, dateString.toString());
        values.put(utilidades.CAMPO_HORA, TimeString.toString());
        values.put(utilidades.CAMPO_DESCRIPCION, campodescripcion.getText().toString());
        values.put(utilidades.CAMPO_CHECK, 0);

        if (!agendaInstance.getIdNull()) {
            System.out.println("Editando");
            db.update(utilidades.TABLA_AGENDA, values, "id_agenda=" + agendaInstance.getId_agenda(), null);
        } else
            db.insert(utilidades.TABLA_AGENDA, utilidades.CAMPO_TITULO, values);
       limpiar();
        db.close();
    }
    private void limpiar() {
        campotitulo.setText("");
        campofecha.setText("");
        campohora.setText("");
        campodescripcion.setText("");
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if(v==btn_fecha) {
            final Calendar c= Calendar.getInstance();

            ano=c.get (Calendar.YEAR);
            mes=c.get (Calendar.MONTH);
            dia=c.get (Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);

                    if (dayOfMonth >= 1 && dayOfMonth <= 9) {
                        day = "0" + day;
                    }
                    if (monthOfYear >= 0 && monthOfYear <= 8) {
                        month = "0" + month;
                    }
                    selecciona_fecha.setText(year + "-" + month + "-" + day);
                    diaa= dayOfMonth;
                    mess = monthOfYear ;
                    añoo= year;

                  //  selecciona_fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            },ano,mes,dia);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        }
        if(v==btn_hora) {
            final Calendar c= Calendar.getInstance();
            hora=c.get (Calendar.HOUR_OF_DAY);
            minutos=c.get (Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String StrHora = String.valueOf(hourOfDay);
                    String StrMin = String.valueOf(minute);
                    if (hourOfDay >= 0 && hourOfDay <= 9) {
                        StrHora = "0" + StrHora;
                    }
                    if (minute >= 0 && minute <= 9) {
                        StrMin = "0" + StrMin;
                    }
                    selecciona_hora.setText (StrHora+":"+StrMin);
                    horaa = hourOfDay;
                    minutoss = minute;


                }
            },hora,minutos,false);
            timePickerDialog.show();

        }

    }
    public void setAlarm(){

        String name = diaa + "-" + mess + "-" + añoo + "-" + horaa + ":" + minutoss;
        int request = (int)(Math.random() * 30) + 1;

        loadLocalData = new LoadLocalData(getApplicationContext(), name);
        loadLocalData.setReminderStatus(true);
        loadLocalData.set_title(campotitulo.getText().toString());
        loadLocalData.set_desc(campodescripcion.getText().toString());
        loadLocalData.set_hour(horaa);
        loadLocalData.set_min(minutoss);
        loadLocalData.set_year(añoo);
        loadLocalData.set_month(mess);
        loadLocalData.set_day(diaa);
        loadLocalData.set_request(request);

        SharedPreferences preferences = getSharedPreferences("Alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        NotificationScheduler.setReminder(AddAgendaActivity.this, MyAlarmReceiver.class, loadLocalData.get_year(), loadLocalData.get_month(), loadLocalData.get_day(), loadLocalData.get_hour(), loadLocalData.get_min(), request);
        Toast.makeText(this, "Alarma programada", Toast.LENGTH_SHORT).show();
    }

}
