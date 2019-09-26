package itsfcp.isc.edu.Hourhand;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import itsfcp.isc.edu.Hourhand.entidades.agenda;
import itsfcp.isc.edu.Hourhand.entidades.carreras;
import itsfcp.isc.edu.Hourhand.entidades.grupo;
import itsfcp.isc.edu.Hourhand.entidades.horario;
import itsfcp.isc.edu.Hourhand.entidades.perfil;
import itsfcp.isc.edu.Hourhand.entidades.semestre;
import itsfcp.isc.edu.Hourhand.entidades.unidades;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

public class ResumenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String URL_CONEXIONSEM= "http://isc-itsfcp.net/Hour-hand/consultaJSON/consulta.php";
    private DrawerLayout drawerLayout;

    TextView fechaCompleta;
    TextView Dia;
    TextView HoraInicio;
    TextView HoraFin;
    ListView actividadhoy;
    ImageView imagenes;
    ArrayList<String> listaagenda;
    ArrayList<agenda> listaagendas;
    String fechacComplString2;

    private ImageView Sinagendas;
    private TextView Sinpendientes;


    ArrayList<String> listahorario;
    ArrayList<horario> listahorarios;
    String unidades;
    String carreras;
    String semestres;
    String grupos;
    String horaINICIO;
    String horaFINAL;
    String NombreDia;
    ArrayList<perfil>perfillist;

    ArrayList<unidades> unidadesperfillist;
    ArrayList<carreras> carrerasperfillist;
    ArrayList<semestre> semestreperfillist;
    ArrayList<grupo> grupoperfillist;
    String titulo= "Agenda";
    Database conn;


    // Class variables for the Cursor that holds task data and the Context
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null) {
            this.setTitle(R.string.mi_titulo);
            addagenda();
        }
        Sinagendas = (ImageView) findViewById(R.id.sinagendas);
        Sinpendientes = (TextView) findViewById(R.id.txt_sinpendientes);


        Date d = new Date();
        setSupportActionBar(toolbar);
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        // SACAMOS EL DIA
        Dia = (TextView) findViewById(R.id.txt_hoy);
        SimpleDateFormat di=new SimpleDateFormat("EEEE");
        String currentDateTimeStrin = di.format(d);
        NombreDia = currentDateTimeStrin;
        NombreDia = Character.toUpperCase(NombreDia.charAt(0)) + NombreDia.substring(1,NombreDia.length());
        Dia.setText(NombreDia);


        consultarelementoseleccionado();
        consultaInicio();
        consultasalida();
        //notificar();

        //SACAMOS LA FECHA COMPLETA
        fechaCompleta = (TextView) findViewById(R.id.fecha_hoy);
        SimpleDateFormat fecc = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fecc2 = new SimpleDateFormat("yyyy-MM-dd");
        String fechacComplString = fecc.format(d);
        fechacComplString2 = fecc2.format(d);
        fechaCompleta.setText(fechacComplString);
        consultalistaPersonas();

        // SACAMOS HORA INICIO
        HoraInicio = (TextView) findViewById(R.id.txt_hora_entrada);
        HoraInicio.setText(horaINICIO);
        //SACAMOS HORA FIN
        HoraFin = (TextView) findViewById(R.id.txt_hora_salida);
        HoraFin.setText(horaFINAL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (imagenes !=null){
            View hView = navigationView.getHeaderView(0);
            imagenes = (ImageView) hView.findViewById(R.id.perfilmenu);
        }else{
            View hView = navigationView.getHeaderView(0);
            imagenes = (ImageView) hView.findViewById(R.id.perfilmenu);
            getimage();
        }
    }

    public void addagenda() {

        android.support.v4.app.Fragment FragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentoGenerico = new AgendaFragment();

        if (FragmentoGenerico != null) {
            fragmentManager.beginTransaction().replace(R.id.escenario, FragmentoGenerico).commit();
        }
        // Setear título actual

    }
    private void consultalistaPersonas() {
        Database conn = new Database(getApplication(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        agenda agendas = null;
        listaagendas = new ArrayList<>();

        Cursor cursor = db.rawQuery(" select * from agenda where Fecha_agenda ='" + fechacComplString2 + "'", null);

        while (cursor.moveToNext()) {
            agendas = new agenda();
            agendas.setId_agenda(cursor.getInt(0));
            agendas.setTitulo_agenda(cursor.getString(1));
            listaagendas.add(agendas);
            obtenerlista();
        }
    }
    private void obtenerlista() {
        listaagenda = new ArrayList<String>();

        for (int i = 0; i < listaagendas.size(); i++) {
            listaagenda.add(listaagendas.get(i).getTitulo_agenda());
            Sinagendas.setVisibility(View.INVISIBLE);
            Sinpendientes.setVisibility(View.INVISIBLE);
        }
        actividadhoy=(ListView) findViewById(R.id.txt_InfoGeneral);
        ArrayAdapter adaptador= new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaagenda);
        actividadhoy.setAdapter(adaptador);
    }
    private void consultarelementoseleccionado() {
        conn  = new Database(getApplication(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        perfil perfil = null;
        perfillist =new ArrayList<perfil>();
        itsfcp.isc.edu.Hourhand.entidades.unidades unidad = null;
        unidadesperfillist = new ArrayList<unidades>();
        itsfcp.isc.edu.Hourhand.entidades.carreras carrera = null;
        carrerasperfillist = new ArrayList<carreras>();
        semestre semestre = null;
        semestreperfillist = new ArrayList<semestre>();
        grupo grupo = null;
        grupoperfillist = new ArrayList<grupo>();
        Cursor cursor = db.rawQuery("SELECT  perfil.id_perfil, uni_academica.id_unidad_a, substr(n_carrera,4,4),substr(n_semestre,1,1),substr(n_grupo,2,1) FROM perfil INNER JOIN uni_academica ON perfil.id_unidad_a = uni_academica.id_unidad_a INNER JOIN carreras ON perfil.id_carrera = carreras.id_carrera INNER JOIN semestre ON perfil.id_semestre = semestre.id_semestre INNER JOIN grupo ON perfil.id_grupo = grupo.id_grupo ORDER BY id_perfil DESC", null);
        while (cursor.moveToNext()) {
            unidades = String.valueOf(cursor.getString(1));
            carreras =String.valueOf(cursor.getString(2));
            semestres =String.valueOf(cursor.getString(3));
            grupos =String.valueOf(cursor.getString(4));
        }
    }
    private void consultaInicio() {
        Database conn = new Database(getApplication(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        horario horarios = null;
        listahorarios = new ArrayList<>();

        String Consulta = "Select min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio ," +
                " max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin " +
                "from Horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN maestro ON Horario.id_maestro = maestro.id_maestro " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera "+
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a "+
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre " +
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula " +
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo " +
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario " +
                "INNER JOIN dias ON detalle_materias.dia = dias.dia " +
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods " +
                "where UPPER(nombre)= '"+NombreDia.toUpperCase()+"' and uni_academica.id_unidad_a = '"+unidades+"' and periods.id_unidad_a = '"+unidades+"' and substr(n_carrera,4,4) = '"+carreras+"' and substr(n_semestre,1,1) = '"+semestres+"' and substr(n_grupo,2,1) = '"+grupos+"' and visible = '1'  group by materias.n_materia " +
                " UNION " +
                "Select min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio , " +
                "max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin " +
                "from horario_irregulares " +
                "INNER JOIN horario ON horario.id_horario = horario_irregulares.id_horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN maestro ON Horario.id_maestro = maestro.id_maestro " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera " +
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre " +
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula " +
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo " +
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario " +
                "INNER JOIN dias ON detalle_materias.dia = dias.dia " +
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods " +
                "where UPPER(nombre) = '"+NombreDia.toUpperCase()+"'and periods.id_unidad_a = '"+unidades+"' group by materias.n_materia ORDER BY  hora_inicio ASC LIMIT 1 ";
        Cursor cursor = db.rawQuery(Consulta, null);
        while (cursor.moveToNext()) {
            horaINICIO=cursor.getString(0);
        }

    }
    private void notificar(){
        startService(new Intent(ResumenActivity.this,MyTestService.class));
    }
    private void consultasalida() {
        Database conn = new Database(getApplication(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        horario horarios = null;
        listahorarios = new ArrayList<>();

        String Consulta = "Select min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio ," +
                " max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin " +
                "from Horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN maestro ON Horario.id_maestro = maestro.id_maestro " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera "+
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a "+
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre " +
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula " +
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo " +
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario " +
                "INNER JOIN dias ON detalle_materias.dia = dias.dia " +
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods " +
                "where UPPER(nombre)= '"+NombreDia.toUpperCase()+"' and uni_academica.id_unidad_a = '"+unidades+"' and periods.id_unidad_a = '"+unidades+"' and substr(n_carrera,4,4) = '"+carreras+"' and substr(n_semestre,1,1) = '"+semestres+"' and substr(n_grupo,2,1) = '"+grupos+"' and visible = '1'  group by materias.n_materia " +
                " UNION " +
                "Select min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio , " +
                "max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin " +
                "from horario_irregulares " +
                "INNER JOIN horario ON horario.id_horario = horario_irregulares.id_horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN maestro ON Horario.id_maestro = maestro.id_maestro " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera " +
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre " +
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula " +
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo " +
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario " +
                "INNER JOIN dias ON detalle_materias.dia = dias.dia " +
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods " +
                "where UPPER(nombre) = '"+NombreDia.toUpperCase()+"' and periods.id_unidad_a = '"+unidades+"' group by materias.n_materia ORDER BY hora_fin DESC LIMIT 1 ";
        Cursor cursor = db.rawQuery(Consulta, null);
        while (cursor.moveToNext()) {
            horaFINAL = cursor.getString(1);

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ResumenActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actualizar_semestre:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public  String sacarperfil(){
        String idsemestre = "";
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id_semestre from perfil ", null);
        while (cursor.moveToNext()) {
            idsemestre = String.valueOf(cursor.getString(0));
        }
        cursor.close();
        return idsemestre;

    }
    public void comparar(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL_CONEXIONSEM + "?idsemestre=" + sacarperfil(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jArray = new JSONArray(response);
                            if (jArray.length() == 0 ) {
                                eliminarperfil();
                                eliminaragenda();
                                eliminarunidad();
                                eliminaraula();
                                eliminarcarrera();
                                eliminargrupo();
                                eliminarmaestro();
                                eliminarmateria();
                                eliminardias();
                                eliminarperiodo();
                                eliminarsemestre();
                                eliminarhorarioclases();
                                eliminarhorario();
                                eliminardetallematerias();
                            }else{
                                eliminarunidad();
                                eliminaraula();
                                eliminarcarrera();
                                eliminargrupo();
                                eliminarmaestro();
                                eliminarmateria();
                                eliminardias();
                                eliminarperiodo();
                                eliminarsemestre();
                                eliminarhorarioclases();
                                eliminarhorario();
                                eliminardetallematerias();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void Resetear(){
                 eliminarperfil();
                 eliminaragenda();
                 eliminarunidad();
                 eliminaraula();
                 eliminarcarrera();
                 eliminargrupo();
                 eliminarmaestro();
                 eliminarmateria();
                 eliminardias();
                 eliminarperiodo();
                 eliminarsemestre();
                 eliminarhorarioclases();
                 eliminarhorario();
                 eliminardetallematerias();
                            }
    public void eliminarperfil(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM perfil");
        db.close();
    }
    public void eliminarunidad(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM uni_academica");
        db.close();
    }
    public void eliminarsemestre(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM semestre");
        db.close();
    }
    public void eliminarmaestro(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM maestro");
        db.close();
    }
    public void eliminarmateria(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM materias");
        db.close();
    }
    public void eliminarcarrera(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM carreras");
        db.close();
    }
    public void eliminargrupo(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM grupo");
        db.close();
    }
    public void eliminaraula(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM aula");
        db.close();
    }
    public void eliminarperiodo(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM periods");
        db.close();
    }
    public void eliminardias(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM dias");
        db.close();
    }
    public void eliminarhorarioclases(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM horario_clases");
        db.close();
    }
    public void eliminarhorario(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM horario");
        db.close();
    }

    public void eliminardetallematerias(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM detalle_materias");
        db.close();
    }
    public void eliminaragenda(){
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("DELETE FROM agenda");
        db.close();
    }
    //Actualizar
    private void actualizar() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE actualizacion SET status = 0");
        db.close();
    }

    public void Reiniciar() {
        Intent siguiente = new Intent(ResumenActivity.this, splashActivity.class);
        startActivity(siguiente);
        // Activity being restarted from stopped state
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem itemDrawer) {
        // Handle navigation view item clicks here.
        int id = itemDrawer.getItemId();
        Fragment FragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {
            reloadIntent(ResumenActivity.class);

        } else if (id == R.id.nav_horario) {
            fragmentManager.beginTransaction().replace(R.id.escenario, new HorarioFragment()).commit();

        } else if (id == R.id.nav_agenda) {
            fragmentManager.beginTransaction().replace(R.id.escenario, new AgendaFragment()).addToBackStack(null).commit();
            //HelperManage.launchNewActivity(this,AgendaActivity.class, false);
        } else if (id == R.id.nav_notificaciones) {
            fragmentManager.beginTransaction().replace(R.id.escenario, new NotificacionesFragment()).commit();

        } else if (id == R.id.nav_acerca_de) {
            fragmentManager.beginTransaction().replace(R.id.escenario, new AcercadeFragment()).commit();
        }else if (id==R.id.nav_actualizar){
            DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case  DialogInterface.BUTTON_POSITIVE:
                            System.out.println("Actualizando");
                            comparar();
                            actualizar();
                            Reiniciar();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResumenActivity.this);
            builder.setMessage("¿Seguro que desea actualizar la aplicacion?")
                    .setPositiveButton("Si", dialogDeleteItem)
                    .setNegativeButton("No", dialogDeleteItem).show();
            Log.i("ActionBar", "Nuevo!");
        }else if (id==R.id.nav_reiniciar){
            DialogInterface.OnClickListener dialogDeleteItem = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case  DialogInterface.BUTTON_POSITIVE:
                            System.out.println("Reiniciando");
                            Resetear();
                            actualizar();
                            Reiniciar();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResumenActivity.this);
            builder.setMessage("¿Seguro que desea Reiniciar la aplicacion?,Se perdera toda su informacion guardada?")
                    .setPositiveButton("Si", dialogDeleteItem)
                    .setNegativeButton("No", dialogDeleteItem).show();
            Log.i("ActionBar", "Nuevo!");
        }else if (id==R.id.cerrarsesion){
            finishAffinity();
        }

        // Setear título actual
        getSupportActionBar().setTitle(itemDrawer.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void reloadIntent(Class activityHorarioClass) {
        Intent intent = new Intent(ResumenActivity.this, activityHorarioClass);
        startActivity(intent);
    }

    public void editperfil(View view) {
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);

        android.support.v4.app.Fragment FragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentoGenerico = new PerfilFragment();

        if (FragmentoGenerico != null) {
            fragmentManager.beginTransaction().replace(R.id.escenario, FragmentoGenerico).commit();
        }
        // Setear título actual
        getSupportActionBar().setTitle("Perfil");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    //para ver la imagen alamacenada en navheader
    public void getimage(){
        SQLiteDatabase db = conn.getWritableDatabase();
        Bitmap bmp = null;
        String sql = " SELECT * FROM perfil";
        Cursor cur=db.rawQuery(sql, new String[] {});
        while (cur.moveToNext()) {
            byte[] photo = cur.getBlob(5);
            if (photo != null) {
                bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                imagenes.setImageBitmap(bmp);
            }
        }
        return ;
    }
}