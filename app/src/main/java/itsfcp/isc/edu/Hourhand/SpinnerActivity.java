package itsfcp.isc.edu.Hourhand;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import itsfcp.isc.edu.Hourhand.entidades.carreras;
import itsfcp.isc.edu.Hourhand.entidades.grupo;
import itsfcp.isc.edu.Hourhand.entidades.horario;
import itsfcp.isc.edu.Hourhand.entidades.semestre;
import itsfcp.isc.edu.Hourhand.entidades.unidades;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

import java.util.ArrayList;


public class SpinnerActivity extends AppCompatActivity {
    Spinner spinnerunidades, spinnercarreras, spinnersemestre, spinnergrupo;
    Database conn;
    ArrayList<horario> listahorarios;
    //unidades
    ArrayList<String> listaunidades;
    ArrayList<unidades> unidadeslist;
    ArrayList<unidades> unidadesperfillist;
    //carreras
    ArrayList<String> listacarreras;
    ArrayList<carreras> carreraslist;
    ArrayList<carreras> carrerasperfillist;
    //semestre
    ArrayList<String> listasemestre;
    ArrayList<semestre> semestrelist;
    ArrayList<semestre> semestreperfillist;
    //grupo
    ArrayList<String> listagrupo;
    ArrayList<grupo> grupolist;

    Button siguiente;
    Button Actualizar;
    protected String selection;
    protected int position;
    private Cursor cursor;
    private ArrayAdapter<String> listAdapter;
    protected ArrayAdapter<CharSequence> adapter;
    Integer IdSelectunidad;
    String IdSelectsemetre, IdSelectcarrera, IdSelectGrupo ;
    String IdSelectsemetre1, IdSelectcarrera1 ;

    @SuppressLint("ResourceType")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);

        //spinner unidades academicas
        spinnerunidades = (Spinner) findViewById(R.id.sp_uni_academ);
        consultarlistaunidades();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, R.layout.item_spinner, listaunidades);
        spinnerunidades.setAdapter(adaptador);

        //spinner carreras
        spinnercarreras = (Spinner) findViewById(R.id.sp_carrera);
        consultarlistcarreras();
        ArrayAdapter<CharSequence> adaptador1 = new ArrayAdapter(this, R.layout.item_spinner, listacarreras);
        spinnercarreras.setAdapter(adaptador1);

        //spinner semestre
        spinnersemestre = (Spinner) findViewById(R.id.sp_semestre);
        consultarlistsemestre();
        ArrayAdapter<CharSequence> adaptador2 = new ArrayAdapter(this, R.layout.item_spinner, listasemestre);
        spinnersemestre.setAdapter(adaptador2);

        //spinner grupo
        spinnergrupo = (Spinner) findViewById(R.id.sp_grupo);
        consultarlistgrupo();
        ArrayAdapter<CharSequence> adaptador3 = new ArrayAdapter(this, R.layout.item_spinner, listagrupo);
        spinnergrupo.setAdapter(adaptador3);

        //boton siguente
        siguiente = (Button) findViewById(R.id.btn_aceptar);

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IdSelectunidad == null || IdSelectcarrera == null || IdSelectsemetre == null || IdSelectGrupo == null) {
                    Toast.makeText(getApplicationContext(), "Ha dejado campos vacios", Toast.LENGTH_LONG).show();
                }else {
                    registrarenperfil();
                    Intent siguiente = new Intent(SpinnerActivity.this, ResumenActivity.class);
                    startActivity(siguiente);
                }
            }
        });
        //Boton Actualizar
        Actualizar = (Button) findViewById(R.id.btnactualizardatos);
        Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case  DialogInterface.BUTTON_POSITIVE:
                                System.out.println("Actualizando");
                                actualizar();
                                Reiniciar();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SpinnerActivity.this);
                builder.setMessage("¿Seguro que desea actualizar los datos de la aplicacion?")
                        .setPositiveButton("Si", dialogDeleteItem)
                        .setNegativeButton("No", dialogDeleteItem).show();
            }
        });

        //SELECCION EN CADA SPINNER
        spinnerunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position !=0 ){
                    IdSelectunidad = unidadeslist.get(position-1).getId_unidad_a().intValue();
                   /* IdSelectcarrera="";
                    IdSelectsemetre="";
                    IdSelectGrupo=""; */
                    //spinner carreras
                    spinnercarreras = (Spinner) findViewById(R.id.sp_carrera);
                    consultarlistcarreras();
                    ArrayAdapter<CharSequence> adaptador1 = new ArrayAdapter( SpinnerActivity.this, R.layout.item_spinner,listacarreras);
                    spinnercarreras.setAdapter(adaptador1);
                    IdSelectcarrera = null;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnercarreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position !=0 ){
                    IdSelectcarrera=carreraslist.get(position-1).getId_carrera();
                    IdSelectcarrera1=carreraslist.get(position-1).getN_carrera();
                    //spinner semestre
                    spinnersemestre = (Spinner) findViewById(R.id.sp_semestre);
                    consultarlistsemestre();
                    ArrayAdapter<CharSequence> adaptador2 = new ArrayAdapter(SpinnerActivity.this, R.layout.item_spinner, listasemestre);
                    spinnersemestre.setAdapter(adaptador2);
                    IdSelectsemetre = null;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnersemestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position !=0 ){
                    IdSelectsemetre= semestrelist.get(position-1).getId_semestre();
                    IdSelectsemetre1=semestrelist.get(position-1).getN_semestre();

                    //spinner grupos
                    spinnergrupo = (Spinner) findViewById(R.id.sp_grupo);
                    consultarlistgrupo();
                    ArrayAdapter<CharSequence> adaptador3 = new ArrayAdapter(SpinnerActivity.this, R.layout.item_spinner,listagrupo);
                    spinnergrupo.setAdapter(adaptador3);
                    IdSelectGrupo = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnergrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position !=0 ){
                    IdSelectGrupo= grupolist.get(position-1).getId_grupo();
                }else{
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void registrarenperfil() {

        Database conn = new Database(this, "BDHorario", null, 1);
        SQLiteDatabase db= conn.getReadableDatabase();
        //obtener los valores
        ContentValues values=new ContentValues();
        values.put(utilidades.CAMPO_UNIDAD,IdSelectunidad);
        values.put(utilidades.CAMPO_CARRERA,IdSelectcarrera);
        values.put(utilidades.CAMPO_SEMESTRE,IdSelectsemetre);
        values.put(utilidades.CAMPO_GRUPO,IdSelectGrupo);

        Long idResultante=db.insert(utilidades.TABLA_PERFIL,utilidades.CAMPO_UNIDAD,values);

        db.close();
    }
    //LISTA UNIDADES
    private void consultarlistaunidades() {

        SQLiteDatabase db = conn.getReadableDatabase();

        unidades persona = null;
        unidadeslist = new ArrayList<unidades>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_UNIDAD, null);

        while (cursor.moveToNext()) {
            persona = new unidades();
            persona.setId_unidad_a(cursor.getInt(0));
            persona.setN_unidad_a(cursor.getString(1));

            unidadeslist.add(persona);
        }
        obtenerlistaunidades();
    }

    private void obtenerlistaunidades() {
        listaunidades = new ArrayList<String>();
        listaunidades.add("Seleccione la Unidad");

        for (int i = 0; i < unidadeslist.size(); i++) {
            listaunidades.add(unidadeslist.get(i).getN_unidad_a());
        }
    }
    //LISTA CARRERAS
    private void consultarlistcarreras() {
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        carreras carrera = null;
        carreraslist = new ArrayList<carreras>();

        Cursor cursor = db.rawQuery("SELECT carreras.id_carrera,substr(n_carrera,4,4) FROM horario " +
                "inner join horario_clases on horario.id_horario_c = horario_clases.id_horario_c " +
                "inner join uni_academica on horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "inner join carreras on horario_clases.id_carrera = carreras.id_carrera " +
                "where uni_academica.id_unidad_a = '"+IdSelectunidad+"' " +
                "group by substr(n_carrera ,4,4) ", null);
        while (cursor.moveToNext()) {

            carrera = new carreras();
            carrera.setId_carrera(cursor.getString(0));
            carrera.setN_carrera(cursor.getString(1));
            carreraslist.add(carrera);
        }
        obtenerlistacarrera();
    }
    private void obtenerlistacarrera() {
        listacarreras = new ArrayList<String>();
        listacarreras.add("Seleccione su carrera");

        for (int i = 0; i < carreraslist.size(); i++) {
            listacarreras.add(carreraslist.get(i).getN_carrera());
        }
    }
    //LISTA SEMESTRES
    private void consultarlistsemestre() {
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        semestre semestre = null;
        semestrelist = new ArrayList<semestre>();

        Cursor cursor = db.rawQuery("SELECT semestre.id_semestre,substr(n_semestre,1,1) FROM horario " +
                "inner join horario_clases on horario.id_horario_c = horario_clases.id_horario_c " +
                "inner join uni_academica on horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "inner join carreras on horario_clases.id_carrera = carreras.id_carrera " +
                "inner join semestre on horario_clases.id_semestre = semestre.id_semestre " +
                "where uni_academica.id_unidad_a = '"+IdSelectunidad+"' and substr(n_carrera,4,4) = '"+IdSelectcarrera1+"'  " +
                "group by substr(n_semestre ,1,1) ", null);

        while (cursor.moveToNext()) {

            semestre = new semestre();
            semestre.setId_semestre(cursor.getString(0));
            semestre.setN_semestre(cursor.getString(1));

            semestrelist.add(semestre);
        }
        obtenerlistsemestre();
    }

    private void obtenerlistsemestre() {
        listasemestre = new ArrayList<String>();
        listasemestre.add("Seleccione su Semestre");

        for (int i = 0; i < semestrelist.size(); i++) {
            listasemestre.add(semestrelist.get(i).getN_semestre());
        }
    }
    //LISTA GRUPO
    private void consultarlistgrupo() {
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        grupo grupo = null;
        grupolist = new ArrayList<grupo>();

        Cursor cursor = db.rawQuery("SELECT grupo.id_grupo,substr(n_grupo,2,1) FROM horario " +
                "inner join horario_clases on horario.id_horario_c = horario_clases.id_horario_c " +
                "inner join uni_academica on horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "inner join carreras on horario_clases.id_carrera = carreras.id_carrera " +
                "inner join semestre on horario_clases.id_semestre = semestre.id_semestre " +
                "inner join grupo on horario_clases.id_grupo = grupo.id_grupo " +
                "where uni_academica.id_unidad_a = '"+IdSelectunidad+"' and substr(n_carrera,4,4) = '"+IdSelectcarrera1+"' and substr(n_semestre,1,1) = '"+IdSelectsemetre1+"' " +
                "group by substr(n_grupo ,2,1)", null);

        while (cursor.moveToNext()) {
            grupo = new grupo();
            grupo.setId_grupo(cursor.getString(0));
            grupo.setN_grupo(cursor.getString(1));

            grupolist.add(grupo);
        }
        obtenerlistgrupo();
    }
    private void obtenerlistgrupo() {
        listagrupo = new ArrayList<>();
        listagrupo.add("Seleccione su Grupo");

        for (int i = 0; i < grupolist.size(); i++) {
            listagrupo.add(grupolist.get(i).getN_grupo());

        }
    }

    private void actualizar() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE actualizacion SET status = 0");
        db.close();
    }
    public void Reiniciar() {
        Intent siguiente = new Intent(SpinnerActivity.this, splashActivity.class);
        startActivity(siguiente);
        // Activity being restarted from stopped state
    }

}
