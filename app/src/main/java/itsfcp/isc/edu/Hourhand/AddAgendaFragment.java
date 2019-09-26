package itsfcp.isc.edu.Hourhand;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddAgendaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAgendaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageButton salir;
    EditText campotitulo, campodescripcion;
    TextView campohora, campofecha;
    Button btn_fecha, btn_hora, btn_guardar;
    TextView selecciona_fecha, selecciona_hora;
    public agenda agend;
    agenda agendaInstance;
    String idagenda;
    private AgendaFragment agendaFragment;

    private int dia, mes, ano, hora, minutos;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public AddAgendaFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static AddAgendaFragment newInstance(String param1, String param2) {
        AddAgendaFragment fragment = new AddAgendaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_add_agenda, container, false);
        //CONEXION DE BOTONES
        campotitulo = (EditText) view.findViewById(R.id.añade_titulo);
        campohora = (TextView) view.findViewById(R.id.selecciona_hora);
        campofecha = (TextView) view.findViewById(R.id.selecciona_fecha);
        campodescripcion = (EditText) view.findViewById(R.id.descripcion);

        btn_guardar = (Button) view.findViewById(R.id.btn_guardar);
        salir = (ImageButton) view.findViewById(R.id.btn_salir);
        btn_fecha = (Button) view.findViewById(R.id.btn_fecha);
        btn_hora = (Button) view.findViewById(R.id.btn_hora);
        selecciona_fecha = (TextView) view.findViewById(R.id.selecciona_fecha);
        selecciona_hora = (TextView) view.findViewById(R.id.selecciona_hora);

        btn_fecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v == btn_fecha) {
                    final Calendar c = Calendar.getInstance();

                    ano = c.get(Calendar.YEAR);
                    mes = c.get(Calendar.MONTH);
                    dia = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

                            //  selecciona_fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                        }
                    }, ano, mes, dia);
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    datePickerDialog.show();

            }
        }
       });
        btn_hora.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == btn_hora) {
                final Calendar c = Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
                        selecciona_hora.setText(StrHora + ":" + StrMin);
                    }
                }, hora, minutos, false);
                timePickerDialog.show();

            }
        }

    });
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardardatos();
                //Declaramos el nuevo Fragment que queremos abrir
                android.support.v4.app.Fragment FragmentoGenerico = null;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                FragmentoGenerico = new AgendaFragment();

                if (FragmentoGenerico != null) {
                    fragmentManager.beginTransaction().replace(R.id.escenario, FragmentoGenerico).commit();
                }
                // Setear título actual
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Agenda");
            }
        });

        // Consultar Instancia
        agendaInstance = agenda.getInstance();
        if (!agendaInstance.getIdNull()) {
            setValues();
        }
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setValues() {
        campotitulo.setText(agendaInstance.getTitulo_agenda());
        campofecha.setText(agendaInstance.getFecha_agenda());
        campohora.setText(agendaInstance.getHora_agenda());
        campodescripcion.setText(agendaInstance.getDecrip_agenda());
    }

    private void guardardatos() {
        Database conn = new Database(getContext(), "BDHorario", null, 1);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
