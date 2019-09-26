package itsfcp.isc.edu.Hourhand;

import android.app.AlarmManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.service.notification.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.entidades.notificaciones;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificacionesFragment extends Fragment {

    NotificationCompat.Builder notificacion;
    private static final int idUnica = 51623;
    Database conn;
    ArrayList<String> listaagenda;
    ArrayList<agenda> listaagendas;
    private LoadLocalData loadLocalData;

    ArrayList<notificaciones> notificaciones;
    Switch notificar;
    Switch sonido;
    Switch vibracion;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificacionesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificacionesFragment newInstance(String param1, String param2) {
        NotificacionesFragment fragment = new NotificacionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notificaciones, container, false);

        notificar = (Switch) view.findViewById(R.id.Sw_On_Notificacion);
        sonido = (Switch) view.findViewById(R.id.Sw_On_Sonido);
        vibracion = (Switch) view.findViewById(R.id.Sw_On_Vibracion);
        notificar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actualizarnotifi("notificar", 1);
                    sonido.setEnabled(true);
                    vibracion.setEnabled(true);
                } else {
                    actualizarnotifi("notificar", 0);
                    sonido.setEnabled(false);
                    vibracion.setEnabled(false);
                }
            }
        });
        sonido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actualizarnotifi("sonido", 1);
                } else {
                    actualizarnotifi("sonido", 0);
                }
            }
        });
        vibracion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    actualizarnotifi("vibracion", 1);
                } else {
                    actualizarnotifi("vibracion", 0);
                }
            }
        });
        notificarcion();

        return view;
    }

    private void notificarcion() {
        Database conn = new Database(getContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        Integer Var1;
        Integer Var2;
        Integer Var3;
        notificaciones notifi = null;
        notificaciones = new ArrayList<>();

        Cursor cursor = db.rawQuery(" SELECT * FROM " + utilidades.TABLA_NOTIFICACIONES, null);

        while (cursor.moveToNext()) {
            Var1 = Integer.valueOf(cursor.getString(1));
            Var2 =Integer.valueOf(cursor.getString(2));
            Var3 =Integer.valueOf(cursor.getString(3));
            if (Var1 == 1) {
                notificar.setChecked(true);
                sonido.setEnabled(true);
                vibracion.setEnabled(true);
            }else{
                notificar.setChecked(false);
                sonido.setEnabled(false);
                vibracion.setEnabled(false);
            }
            if(Var2 == 1){
                sonido.setChecked(true);
            }else{
                sonido.setChecked(false);
            }
            if(Var3== 1){
                vibracion.setChecked(true);
            }else{
                vibracion.setChecked(false);
            }
        }
    }
     public void actualizarnotifi(String columna, int valor){
         Database conn = new Database(getContext(), "BDHorario", null, 1);
         SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros={"1"};
            ContentValues values = new ContentValues();
             if( columna == "notificar") {
             values.put(utilidades.CAMPO_NOTIFICAR, valor);
                }
         if( columna == "sonido") {
             values.put(utilidades.CAMPO_SONIDO, valor);
         }
         if( columna == "vibracion") {
             values.put(utilidades.CAMPO_VIBRACION, valor);
         }
         db.update(utilidades.TABLA_NOTIFICACIONES,values,utilidades.CAMPO_ID_NOTIFICACIONES+"=?",parametros);
     }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
