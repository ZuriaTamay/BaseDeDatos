package itsfcp.isc.edu.Hourhand;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import itsfcp.isc.edu.Hourhand.adapters.AgendaAdapter;
import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;



public class AgendaFragment extends Fragment {
    private static final String DESCRIBABLE_KEY = "describable_key";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param_id";
    private static final String ARG_TITLE = "param_title";
    ListView listaViewPersonas;
    Database conn;
    ArrayList<agenda> listaagendas;

    private ImageView Sinagenda;

    private LoadLocalData loadLocalData;

    private AgendaAdapter agendaAdapter;
    @BindView(R.id.sb_movie_list)
    View agendasReciclerView;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AgendaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AgendaFragment newInstance(String describable) {
        AgendaFragment fragment = new AgendaFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_agenda, container, false);

        FloatingActionButton btnLanzarActivity = (FloatingActionButton) view.findViewById(R.id.btn_flotante);
        ButterKnife.bind(this, view);
        conn = new Database(getActivity() , "BDHorario", null, 1);
        consultalistaPersonas();
        fillUI();

        Sinagenda = (ImageView) view.findViewById(R.id.sinagendas);
        consultaragenda1();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarDatos();
            }
        });

        btnLanzarActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                agenda.getInstance().reset();
                Intent intent = new Intent(getActivity(), AddAgendaActivity.class);
                startActivity(intent);
            }
        });


        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_agenda, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case  DialogInterface.BUTTON_POSITIVE:
                                System.out.println("Delete items");
                                agendaAdapter.deleteItems();
                                dialog.dismiss();
                                consultalistaPersonas();
                                fillUI();
                                consultaragenda1();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Seguro que desea eliminar las notas?")
                        .setPositiveButton("Si", dialogDeleteItem)
                        .setNegativeButton("No", dialogDeleteItem).show();
                Log.i("ActionBar", "Nuevo!");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargarDatos() {
        new UnaTarea().execute();
        consultalistaPersonas();
        fillUI();
        consultaragenda1();

    }
    private class UnaTarea extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity().getApplication(),"Actualizado ",Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void fillUI(){
        configRecyclerViewList();
        obtenerlista();
    }
    private void configRecyclerViewList() {
        if (agendasReciclerView instanceof RecyclerView) {
            Context context = agendasReciclerView.getContext();
            RecyclerView recyclerView = (RecyclerView) agendasReciclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            agendaAdapter = new AgendaAdapter(new ArrayList<agenda>(), getActivity());
            recyclerView.setAdapter(agendaAdapter);
        }
    }

    private void consultalistaPersonas() {
        Database conn = new Database(getActivity(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        agenda agendas = null;
        listaagendas = new ArrayList<>();

        Cursor cursor = db.rawQuery(" SELECT * FROM " + utilidades.TABLA_AGENDA + " ORDER BY fecha_agenda DESC ",null);
       // if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                agendas = new agenda();
                agendas.setId_agenda(cursor.getInt(0));
                agendas.setTitulo_agenda(cursor.getString(1));
                agendas.setFecha_agenda(cursor.getString(2));
                agendas.setHora_agenda(cursor.getString(3));
                agendas.setDecrip_agenda(cursor.getString(4));
                listaagendas.add(agendas);
         //   }
       // } else {
           // Pon la imagen
        }
    }
    private void obtenerlista() {

        ArrayList<agenda> listaagenda = new ArrayList<agenda>() {{

            for (int i = 0; i < listaagendas.size(); i++) {

                String dateString = listaagendas.get(i).getFecha_agenda();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(dateString);
                } catch (ParseException e) {
                }
                DateFormat outputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
                String output1 = outputFormatter1.format(convertedDate);
                //leer la hora
                String timeString = listaagendas.get(i).getHora_agenda();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date convertedtime = new Date();
                try {
                    convertedtime = timeFormat.parse(timeString);
                } catch (ParseException e) {
                }
                DateFormat outputFormatter = new SimpleDateFormat("h:mm a");
                String output = outputFormatter.format(convertedtime);

                add(new agenda(listaagendas.get(i).getId_agenda(),listaagendas.get(i).getTitulo_agenda(), output1,output,listaagendas.get(i).getDecrip_agenda(),0));
            }
        }};
        agendaAdapter.updateItems(listaagenda);
    }
    public void consultaragenda1(){
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT  agenda.id_agenda FROM agenda", null);
        if (cursor.getCount() > 0) {
            Sinagenda.setVisibility(View.INVISIBLE);

        } else {
           Sinagenda.setVisibility(View.VISIBLE);
        }
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
