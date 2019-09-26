package itsfcp.isc.edu.Hourhand;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import itsfcp.isc.edu.Hourhand.utilidades.utilidadess;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ProgressDialog progressDialog;

    // Storage Permissions
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(26)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    private ConstraintLayout mr;
    Button siguiente, siguientedisable;
    public TextView descargando3;
    Database conn;
    int count = 0;
       //URLs PARA CONECTAR CON LOS WEB-SERVICES///
    private static final String URL_CONEXION = "http://isc-itsfcp.net/Hour-hand/consultaJSON/consultajson.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mr = (ConstraintLayout) findViewById(R.id.rl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        conn = new Database(getApplicationContext(), "BDHorario", null, 1);
        descargando3 =(TextView) findViewById(R.id.descargardatos);
        siguiente = (Button) findViewById(R.id.btnalumno);
        siguientedisable = (Button) findViewById(R.id.btnalumnodisable);
        // siguiente =(Button)findViewById(R.id.btnadministrador);
        conexion();
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = conn.getReadableDatabase();

                Cursor cursor = db.rawQuery("SELECT  uni_academica.n_unidad_a, carreras.n_carrera,semestre.n_semestre,grupo.n_grupo FROM perfil INNER JOIN uni_academica ON perfil.id_unidad_a = uni_academica.id_unidad_a INNER JOIN carreras ON perfil.id_carrera = carreras.id_carrera INNER JOIN semestre ON perfil.id_semestre = semestre.id_semestre INNER JOIN grupo ON perfil.id_grupo = grupo.id_grupo ORDER BY id_perfil DESC", null);
                if (cursor.getCount() > 0) {
                    Intent siguiente = new Intent(MainActivity.this, ResumenActivity.class);
                    startActivity(siguiente);

                } else {
                    Intent siguiente = new Intent(MainActivity.this, SpinnerActivity.class);
                    startActivity(siguiente);
                }
            }
        });

    }

    public void conexion() {
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM actualizacion WHERE status = 0", null);

        if (cursor.getCount() > 0) {

               if (ValidaInternet() == true){
                   Cargardatos();
               }
        }
    }

    public void sinconexion() {
        Snackbar snackbar = Snackbar.make(mr, "No tiene conexion a la red, verifique su conexion",
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.Reintentar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar1();
                Reiniciar();
            }
        });
        //CAMBIAR EL COLOR DE LA LETRA ACEPTAR
        snackbar.setActionTextColor(getResources().getColor(R.color.coloracept));
        View snackBarView = snackbar.getView();
        //BACKGROUND
        snackBarView.setBackgroundColor(getResources().getColor(R.color.texto));
        //MESSAGE
        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getResources().getColor(R.color.textoblanco));

        snackbar.show();
    }

    public void Cargardatos(){
            botones();
            cargarall();


    }
    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadunidad() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=uni_academica",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //JSONObject jo = new JSONObject(response);
                            // JSONArray jArray = jo.getJSONArray("id_unidad_a");
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject unidad = jArray.getJSONObject(i);
                                String idUnidad = unidad.getString("id_unidad_a");
                                String NombreUnidad = unidad.getString("n_unidad_a");
                                consultarunidad(idUnidad, NombreUnidad);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadcarrera() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=semestre",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject carrera = jArray.getJSONObject(i);
                                String Idcarrera = carrera.getString("id_semestre");
                                String Ncarrera = carrera.getString("n_semestre");
                                consultarCarrera(Idcarrera, Ncarrera);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadsemestre() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=semestre",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject semestre = jArray.getJSONObject(i);
                                String IdSmestre = semestre.getString("id_semestre");
                                String NSmestre = semestre.getString("n_semestre");
                                consultarSmestre(IdSmestre, NSmestre);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadgrupo() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=semestre",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject grupo = jArray.getJSONObject(i);
                                String IDGrupo = grupo.getString("id_semestre");
                                String NombreGrupo = grupo.getString("n_semestre");
                                consultarGrupo(IDGrupo, NombreGrupo);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadperiods() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=periods",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject periodo = jArray.getJSONObject(i);
                                String Idstime = periodo.getString("id_periods");
                                String firsttime = periodo.getString("hora_inicio");
                                String endtime = periodo.getString("hora_fin");
                                String idunidad = periodo.getString("id_unidad_a");
                                consultarperiods(Idstime, firsttime, endtime, idunidad);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadaula() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=aula",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject aula = jArray.getJSONObject(i);
                                String IDAula = aula.getString("id_aula");
                                String NombreAula = aula.getString("n_aula");
                                consultarAula(IDAula, NombreAula);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadmaterias() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=materias",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject materia = jArray.getJSONObject(i);
                                String IDMateria = materia.getString("id_materia");
                                String NombreMateria = materia.getString("n_materia");
                                consultarMaterias(IDMateria, NombreMateria);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loadmaestros() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=maestro",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject maestro = jArray.getJSONObject(i);
                                String IDMaestros = maestro.getString("id_maestro");
                                String NombreMaestros = maestro.getString("n_maestro");
                                consultarMaestros(IDMaestros, NombreMaestros);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////OBTENIENDO LOS DATOS DESDE LA BASE DE DATOS///
    private void loaddias() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=dias",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject dia = jArray.getJSONObject(i);
                                String Iddias = dia.getString("id_dia");
                                String nomberdias = dia.getString("nombre");
                                String dias = dia.getString("dia");
                                consultardias(Iddias, nomberdias, dias);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadhorario_c() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=horario_clases",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject horarioc = jArray.getJSONObject(i);
                                String IDhoario_clases = horarioc.getString("id_horario_c");
                                String Idunidad = horarioc.getString("id_unidad_a");
                                String Idcarrera = horarioc.getString("id_carrera");
                                String Idsemestre = horarioc.getString("id_semestre");
                                String Idgrupos = horarioc.getString("id_grupo");
                                String Idaula = horarioc.getString("id_aula");
                                consultarHoario_Clases(IDhoario_clases, Idunidad, Idcarrera, Idsemestre, Idgrupos, Idaula);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadhorario() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=horario",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject horario = jArray.getJSONObject(i);
                                String Idhorario = horario.getString("id_horario");
                                String IDhoario_clases = horario.getString("id_horario_c");
                                String IdMaterias = horario.getString("id_materia");
                                String IdMaestros = horario.getString("id_maestro");
                                consultarHorario(Idhorario, IDhoario_clases, IdMaterias, IdMaestros);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadetallematerias() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CONEXION + "?tabla=detalle_materias",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {

                                //OBTENEMOS LOS OBJETOS DEL JSON ARRAY
                                JSONObject detallemateria = jArray.getJSONObject(i);
                                String Iddetallemateria = detallemateria.getString("id_detalle_materias");
                                String IdHorario = detallemateria.getString("id_horario");
                                String Dia = detallemateria.getString("dia");
                                String periods = detallemateria.getString("id_periods");
                                consultarDetalle_materia(Iddetallemateria, IdHorario, Dia, periods);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sinconexion();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    ////////////////////////UNIDAD ACADEMICA CARRILLO//////////////////////
    //UNIDAD
    private void consultarunidad(String idUnidad, String NombreUnidad) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM uni_academica where id_unidad_a='" + idUnidad + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_UNIDAD_A, idUnidad);
                values.put(utilidadess.CAMPO_NOMBRE_UNIDAD_A, NombreUnidad);
                db.insert(utilidadess.TABLA_UNIDAD_A, utilidadess.CAMPO_ID_UNIDAD_A, values);
            }
        cursor.close();
    }

    //CARRERA
    private void consultarCarrera(String Idcarrera, String Ncarrera) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM carreras where id_carrera='" + Idcarrera + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_CARRERA, Idcarrera);
                values.put(utilidadess.CAMPO_NOMBRE_CARRERA, Ncarrera);
                db.insert(utilidadess.TABLA_CARRERAS, utilidadess.CAMPO_ID_CARRERA, values);
            }
        cursor.close();
    }

    //SEMESTRE
    private void consultarSmestre(String IdSmestre, String NSmestre) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM semestre where id_semestre='" + IdSmestre + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_SEMESTRE, IdSmestre);
                values.put(utilidadess.CAMPO_NOMBRE_SEMESTRE, NSmestre);
                db.insert(utilidadess.TABLA_SEMESTRE, utilidadess.CAMPO_ID_SEMESTRE, values);
            }
        cursor.close();
    }

    //GRUPO
    private void consultarGrupo(String IDGrupo, String NombreGrupo) {

            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM grupo where id_grupo='" + IDGrupo + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_GRUPO, IDGrupo);
                values.put(utilidadess.CAMPO_NOMBRE_GRUPO, NombreGrupo);
                db.insert(utilidadess.TABLA_GRUPO, utilidadess.CAMPO_ID_GRUPO, values);
            }
        cursor.close();
    }


    //PERIODOS
    private void consultarperiods(String Idstime, String firsttime, String endtime, String idunidad) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM periods where id_periods='" + Idstime + "' and id_unidad_a ='" + idunidad + "' ", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_PERIODS, Idstime);
                values.put(utilidadess.CAMPO_HORA_INICIO, firsttime);
                values.put(utilidadess.CAMPO_HORA_FINAL, endtime);
                values.put(utilidadess.CAMPO_IDUNIDAD, idunidad);
                db.insert(utilidadess.TABLA_PERIODS, utilidadess.CAMPO_ID_PERIODS, values);
            }
        cursor.close();
    }

    //AULA
    private void consultarAula(String IDAula, String NombreAula) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM aula where id_aula='" + IDAula + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_AULA, IDAula);
                values.put(utilidadess.CAMPO_NOMBRE_AULA, NombreAula);
                db.insert(utilidadess.TABLA_AULA, utilidadess.CAMPO_ID_AULA, values);
            }
            cursor.close();
    }

    //MATERIAS
    private void consultarMaterias(String IDMateria, String NombreMateria) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM materias where id_materia='" + IDMateria + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_MATERIA, IDMateria);
                values.put(utilidadess.CAMPO_NOMBRE_MATERIA, NombreMateria);
                db.insert(utilidadess.TABLA_MATERIA, utilidadess.CAMPO_ID_MATERIA, values);
            }
        cursor.close();
    }

    //MAESTROS
    private void consultarMaestros(String IDMaestros, String NombreMaestros) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM maestro where id_maestro='" + IDMaestros + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_MAESTRO, IDMaestros);
                values.put(utilidadess.CAMPO_NOMBRE_MAESTRO, NombreMaestros);
                db.insert(utilidadess.TABLA_MAESTROS, utilidadess.CAMPO_ID_MAESTRO, values);
            }
        cursor.close();
    }


    //DIAS
    private void consultardias(String Iddias, String nomberdias, String dias) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM dias where id_dia='" + Iddias + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_DIAS, Iddias);
                values.put(utilidadess.CAMPO_NOMBRE_DIAS, nomberdias);
                values.put(utilidadess.CAMPO_DIAS, dias);
                db.insert(utilidadess.TABLA_DIAS, utilidadess.CAMPO_ID_DIAS, values);
            }
        cursor.close();
    }

    //HORARIO CLASES
    private void consultarHoario_Clases(String IDhoario_clases, String Idunidad, String Idcarrera, String Idsemestre, String Idgrupos, String Idaula) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM horario_clases where id_horario_c ='" + IDhoario_clases + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_HORARIO_CLASES, IDhoario_clases);
                values.put(utilidadess.CAMPO_ID_UNIDAD, Idunidad);
                values.put(utilidadess.CAMPO_ID_CARRERAS, Idcarrera);
                values.put(utilidadess.CAMPO_ID_SEMESTRES, Idsemestre);
                values.put(utilidadess.CAMPO_ID_AULAS, Idaula);
                values.put(utilidadess.CAMPO_ID_GRUPOS, Idgrupos);

                db.insert(utilidadess.TABLA_HORARIO_CLASES, utilidadess.CAMPO_ID_HORARIO_CLASES, values);
            }
        cursor.close();
    }

    //HORARIO
    private void consultarHorario(String Idhorario, String IDhoario_clases, String IdMaterias, String IdMestros) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM horario where id_horario_c ='" + IDhoario_clases + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_HORARIO, Idhorario);
                values.put(utilidadess.CAMPO_ID_HORARIO_C, IDhoario_clases);
                values.put(utilidadess.CAMPO_ID_MATERIAS, IdMaterias);
                values.put(utilidadess.CAMPO_ID_MAESTROS, IdMestros);
                values.put(utilidadess.CAMPO_VISIBLE, true);
                db.insert(utilidadess.TABLA_HORARIO, utilidadess.CAMPO_ID_HORARIO_C, values);
            }
    }

    //DETALLE MATERIAS
    private void consultarDetalle_materia(String Iddetallemateria, String IdHorario, String Dia, String periods) {
            SQLiteDatabase db = conn.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM detalle_materias where id_horario ='" + IdHorario + "' and dia='" + Dia + "' and id_periods='" + periods + "'", null);
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(utilidadess.CAMPO_ID_DETALLE_MATERIA, Iddetallemateria);
                values.put(utilidadess.CAMPO_ID_HORARIO_CLASS, IdHorario);
                values.put(utilidadess.CAMPO_DIA, Dia);
                values.put(utilidadess.CAMPO_PERIODS, periods);
                db.insert(utilidadess.TABLA_DETALLE_MATERIA, utilidadess.CAMPO_ID_HORARIO_CLASS, values);
            }
        cursor.close();
    }
    public void Reiniciar() {
        Intent siguiente = new Intent(MainActivity.this, splashActivity.class);
        startActivity(siguiente);
    }

    //Actualizar
    private void actualizar() {
            SQLiteDatabase db = conn.getWritableDatabase();
            db.execSQL("UPDATE actualizacion SET status = 1");
            db.close();


    }
    //Actualizar
    private void actualizar1() {
        SQLiteDatabase db = conn.getWritableDatabase();
        db.execSQL("UPDATE actualizacion SET status = 0");
        db.close();
    }
    public  boolean ValidaInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            sinconexion();
            return false;
        }

    }
    public void botones (){
        siguiente.setVisibility(View.INVISIBLE);
        siguientedisable.setVisibility(View.VISIBLE);

    }

    public void cargarall(){
        Thread t = new Thread(){
            @Override
            public  void run(){
                while(count<=11){

                    try {
                        Thread.sleep(1500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                if (count == 1) {
                                    if (ValidaInternet() == true) {
                                        loadunidad();
                                        descargando3.setText("Descargando Datos 10%");
                                    }
                                }
                                if (count == 2) {
                                    if (ValidaInternet() == true) {
                                        loadsemestre();
                                        loadcarrera();
                                        loadgrupo();
                                        descargando3.setText("Descargando Datos 20%");
                                    }
                                }
                                if (count == 3) {
                                    if (ValidaInternet() == true) {
                                        loadmaestros();
                                        descargando3.setText("Descargando Datos 30%");
                                    }
                                }
                                if (count == 4) {
                                    if (ValidaInternet() == true) {
                                        loadmaterias();
                                        descargando3.setText("Descargando Datos 40%");
                                    }
                                }
                                if (count == 5) {
                                    if (ValidaInternet() == true) {
                                        loadaula();
                                        descargando3.setText("Descargando Datos 50%");
                                    }
                                }
                                if (count == 6) {
                                    if (ValidaInternet() == true) {
                                        loadperiods();
                                        descargando3.setText("Descargando Datos 60%");
                                    }
                                }
                                if (count == 7) {
                                    if (ValidaInternet() == true) {
                                        loaddias();
                                        descargando3.setText("Descargando Datos 70%");
                                    }
                                }
                                if (count == 8) {
                                    if (ValidaInternet() == true) {
                                        loadhorario_c();
                                        descargando3.setText("Descargando Datos 80%");
                                    }
                                }
                                if (count == 9) {
                                    if (ValidaInternet() == true) {
                                        loadhorario();
                                        descargando3.setText("Descargando Datos 90%");
                                    }
                                }
                                if (count == 10) {
                                    if (ValidaInternet() == true) {
                                        loadetallematerias();
                                        descargando3.setText("Descargando Datos 100%");
                                    }
                                }
                                if (count == 11) {
                                    if (ValidaInternet() == true) {
                                        actualizar();
                                        descargando3.setText("Descarga Completa");
                                        siguiente.setVisibility(View.VISIBLE);
                                        siguientedisable.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    public void manualpdf(View view) {
        if (shouldAskPermissions()) {
            askPermissions();
        }
        opciones();

    }
    ///// aqui empieza la opcion de descargar manual///
    private void opciones() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Hour Hand Manual")
                .setMessage("¿Esta seguro que desea descargar el Manual?")
                .setPositiveButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Abrir y descargar",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                abrirydescargar();
                                dialog.cancel();
                            }
                        }).show();
    }

    private void tareaLarga()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }

    private void tiempoespera() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Descargando..."); // Setting Message
        progressDialog.setTitle("Descargando Manual"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);

        new Thread(new Runnable() {
            public void run() {
                new Runnable() {
                    public void run() {
                        progressDialog.setProgress(0);
                    }
                };
                for (int i = 1; i <= 10; i++) {
                    tareaLarga();
                    progressDialog.incrementProgressBy(10);
                }
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Se ha descargado el Manual, Por favor verifique en su telefono!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
    private void abrirydescargar() {
        CopyRawToSDCard(R.raw.manual, getExternalStorageDirectory() + "/ManualHourHand.pdf");

        File pdfFile = new File(getExternalStorageDirectory(), "/ManualHourHand.pdf");//File path
        if (pdfFile.exists()) { //Revisa si el archivo existe!
            Uri path = Uri.fromFile(pdfFile);
            tiempoespera();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //define el tipo de archivo
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Inicia pdf viewer
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No existe archivo! ", Toast.LENGTH_SHORT).show();
        }

    }

    private void CopyRawToSDCard(int id, String path) {

        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            in.close();
            out.close();
            Log.i(TAG, "copyFile, success!");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "copyFile FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "copyFile IOException " + e.getMessage());
        }

    }
    ///// aqui termina las opciones de descarga del manual///

}

