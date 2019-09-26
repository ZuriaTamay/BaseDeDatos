package itsfcp.isc.edu.Hourhand;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import itsfcp.isc.edu.Hourhand.adapters.AddhorarioAdapter;
import itsfcp.isc.edu.Hourhand.adapters.HorarioAdapter;
import itsfcp.isc.edu.Hourhand.entidades.addhorario;
import itsfcp.isc.edu.Hourhand.entidades.carreras;
import itsfcp.isc.edu.Hourhand.entidades.grupo;
import itsfcp.isc.edu.Hourhand.entidades.horario;
import itsfcp.isc.edu.Hourhand.entidades.materias;
import itsfcp.isc.edu.Hourhand.entidades.perfil;
import itsfcp.isc.edu.Hourhand.entidades.semestre;
import itsfcp.isc.edu.Hourhand.entidades.unidades;

public class AddHorarioActivity extends AppCompatActivity {
    ImageButton atras;
    Button buscar;
    Spinner   sp_add_semestre, sp_add_grupo,sp_add_materia;
    Database conn;
    String IdSelectsemetre,IdSelectgrupo ;
    ArrayList<String> listasemestre;
    ArrayList<semestre> semestrelist;
    //grupo
    ArrayList<String> listagrupo;
    ArrayList<grupo> grupolist;
    //materia
    ArrayList<String> listamateria;
    ArrayList<materias> materialist;

    private AddhorarioAdapter addhorarioAdapter;
    @BindView(R.id.add_horario_list)
    View horariosReciclerView;


    ArrayList<addhorario> listahorarios;
    ArrayList<perfil>perfillist;
    String unidades;
    String carreras;
    String semestres;
    String grupos;
    //unidades
    ArrayList<unidades> unidadesperfillist;
    //carreras
    ArrayList<carreras> carrerasperfillist;
    //grupo
    ArrayList<grupo> grupoperfillist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        consultarelementoseleccionado();
        //Conexion de spinners
        sp_add_semestre = (Spinner) findViewById(R.id.sp_add_semestre);
        consultarlistsemestre();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, R.layout.item_spinner,listasemestre);
        sp_add_semestre.setAdapter(adaptador);

        sp_add_grupo = (Spinner) findViewById(R.id.sp_add_grupo);
        consultarlistgrupo();
        ArrayAdapter<CharSequence> adaptador1 = new ArrayAdapter(this, R.layout.item_spinner,listagrupo);
        sp_add_grupo.setAdapter(adaptador1);

        atras =(ImageButton)findViewById(R.id.btn_atras);
        buscar =(Button)findViewById(R.id.btn_horario_Buscar);


        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IdSelectsemetre == null || IdSelectgrupo == null){
                    Toast.makeText(getApplication(),"Ha dejado campos vacios",Toast.LENGTH_LONG).show();
                } else{
                    consultarelementoseleccionado();
                    consultalistahorarios();
                    fillUI();
                }
            }
        });
        //SELECCION EN CADA SPINNER
        sp_add_semestre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position !=0 ) {
                    IdSelectsemetre = semestrelist.get(position - 1).getN_semestre();
                    semestres = IdSelectsemetre;
                    IdSelectgrupo = null;

                    sp_add_grupo = (Spinner) findViewById(R.id.sp_add_grupo);
                    consultarlistgrupo();
                    ArrayAdapter<CharSequence> adaptador1 = new ArrayAdapter(AddHorarioActivity.this, R.layout.item_spinner,listagrupo);
                    sp_add_grupo.setAdapter(adaptador1);


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //SELECCION EN CADA SPINNER
        sp_add_grupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position !=0 ) {
                    IdSelectgrupo= grupolist.get(position - 1).getN_grupo();
                    grupos = IdSelectgrupo;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void fillUI(){
        configRecyclerViewList();
        obtenerlista();
    }
    private void configRecyclerViewList() {
        if (horariosReciclerView instanceof RecyclerView) {
            Context context = horariosReciclerView.getContext();
            RecyclerView recyclerView = (RecyclerView) horariosReciclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            addhorarioAdapter = new AddhorarioAdapter(new ArrayList<addhorario>(), this);
            recyclerView.setAdapter(addhorarioAdapter);
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
                        "where uni_academica.id_unidad_a = '"+unidades+"' and substr(n_carrera,4,4) = '"+carreras+"' " +
                        "group by substr(n_semestre ,1,1) ",null);

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
        listasemestre.add("Seleccione el semestre");

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
                "where uni_academica.id_unidad_a = '"+unidades+"' and substr(n_carrera,4,4) = '"+carreras+"' and substr(n_semestre,1,1) = '"+semestres+"' " +
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
        listagrupo = new ArrayList<String>();
        listagrupo.add("Seleccione el Grupo");

        for (int i = 0; i < grupolist.size(); i++) {
            listagrupo.add(grupolist.get(i).getN_grupo());

        }
    }

    private void consultalistahorarios() {

        Database conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        addhorario horarios = null;
        listahorarios = new ArrayList<>();

        String Consulta ="Select horario.id_horario,n_materia as materia, n_maestro as Maestro,n_unidad_a as unidad , substr(n_carrera,4,4) as Carrera,substr(n_semestre,1,1) as Semestre, n_aula as Aula, substr(n_grupo,2,1) as Grupo from Horario " +
        "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
        "INNER JOIN maestro ON Horario.id_maestro = maestro.id_maestro " +
        "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c "+
        "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera " +
        "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
        "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre " +
        "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula " +
        "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo " +
        "where uni_academica.id_unidad_a = '"+unidades+"' and substr(n_carrera,4,4) = '"+carreras+"' and substr(n_semestre,1,1) = '"+IdSelectsemetre+"' and substr(n_grupo,2,1) = '"+IdSelectgrupo+"' group by n_materia ";
        Cursor cursor = db.rawQuery(Consulta, null);
         while (cursor.moveToNext()) {
            horarios = new addhorario();
            horarios.setId_horario(cursor.getInt(0));
            horarios.setMateria(cursor.getString(1));
            horarios.setMaestro(cursor.getString(2));
            horarios.setUnidad(cursor.getString(3));
            horarios.setCarrera(cursor.getString(4));
            horarios.setSemestre(cursor.getString(5));
            horarios.setAula(cursor.getString(6));
            horarios.setGrupo(cursor.getString(7));
            listahorarios.add(horarios);
        }
    }
    private void obtenerlista() {
        ArrayList<addhorario> listahorario = new ArrayList<addhorario>() {
            {
                for (int i = 0; i < listahorarios.size(); i++) {

                    add(new addhorario(listahorarios.get(i).getId_horario(),listahorarios.get(i).getMateria(),listahorarios.get(i).getMaestro()));
                }
            }};
        addhorarioAdapter.updateItems(listahorario);
    }
    private void consultarelementoseleccionado() {
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        perfil perfil = null;
        perfillist =new ArrayList<perfil>();
        unidades unidad = null;
        unidadesperfillist = new ArrayList<unidades>();
        carreras carrera = null;
        carrerasperfillist = new ArrayList<carreras>();
        grupo grupo = null;
        grupoperfillist = new ArrayList<grupo>();
        Cursor cursor = db.rawQuery("SELECT  perfil.id_perfil,  uni_academica.id_unidad_a,  substr(n_carrera,4,4),substr(n_semestre,1,1),substr(n_grupo,2,1) FROM perfil INNER JOIN uni_academica ON perfil.id_unidad_a = uni_academica.id_unidad_a INNER JOIN carreras ON perfil.id_carrera = carreras.id_carrera INNER JOIN semestre ON perfil.id_semestre = semestre.id_semestre INNER JOIN grupo ON perfil.id_grupo = grupo.id_grupo ORDER BY id_perfil DESC", null);
        while (cursor.moveToNext()) {
            unidades = String.valueOf(cursor.getString(1));
            carreras =String.valueOf(cursor.getString(2));
            semestres =String.valueOf(cursor.getString(3));
            grupos =String.valueOf(cursor.getString(4));
        }
    }
}
