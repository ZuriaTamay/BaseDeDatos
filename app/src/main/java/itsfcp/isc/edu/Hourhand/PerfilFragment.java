package itsfcp.isc.edu.Hourhand;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.usage.ExternalStorageStats;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import itsfcp.isc.edu.Hourhand.entidades.carreras;
import itsfcp.isc.edu.Hourhand.entidades.grupo;
import itsfcp.isc.edu.Hourhand.entidades.perfil;
import itsfcp.isc.edu.Hourhand.entidades.semestre;
import itsfcp.isc.edu.Hourhand.entidades.unidades;
import itsfcp.isc.edu.Hourhand.utilidades.utilidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;
import static android.util.Log.e;


public class PerfilFragment extends Fragment {

    ImageView perfilmenu;

    private final  String APP_DIRECTORY= "MyPictureApp/";
    private final  String MEDIA_DIRECTORY= APP_DIRECTORY + "MyPictureApp/";

    private final int MY_PERMISSIONS =100;
    private final int PHOTO_CODE =200;
    private final int SELECT_PICTURE =300;
    private CircleImageView  imagen;
    // private ImageView imagen;
    private ImageView imagenid;
    private ConstraintLayout mRlView;
    Bitmap bmp = null;
    ArrayList<perfil>perfillistt;
    String unidadess;
    String carrerass;
    String semestress;
    String gruposs;
    private String mPath;

    private LinearLayoutManager linearLayout;
    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private ResumenActivity adaptador;

    Button actualizar, invisible;
    //perfil

    ArrayList<perfil>perfillist;
    ArrayList<perfil>perfilimagenlist;

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
    ArrayList<grupo> grupoperfillist;

    Spinner spinnerunidad, spinnercarrera, spinnersemetre, spinnergrupo,spinnergrupo1;
    Database conn;
    int CampoNombre;
    String idPerfil2;
    int IdSelectunidad;
    String IdSelectsemetre, IdSelectcarrera, IdSelectGrupo ;
    String IdSelectsemetre1, IdSelectcarrera1,IdSelectGrupo1 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_perfil, container, false);
        conn = new Database(getActivity().getApplicationContext(), "BDHorario", null, 1);

        if (imagen !=null){
            imagen = (CircleImageView) view.findViewById(R.id.perfil2);
        }else{
            imagen = (CircleImageView) view.findViewById(R.id.perfil2);
            getimage();
        }
        mRlView=(ConstraintLayout) view.findViewById(R.id.rl_view);
        consultarelementoseleccionados();
        consultarelementoseleccionado();
        //spinner unidades academicas
        spinnerunidad = (Spinner) view.findViewById(R.id.sp_perfil_unidades);
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(getActivity(), R.layout.item_spinner, listaunidades);
        spinnerunidad.setAdapter(adaptador);

        spinnercarrera = (Spinner) view.findViewById(R.id.sp_perfil_carrera);
        ArrayAdapter<CharSequence> adaptador1 = new ArrayAdapter(getActivity(), R.layout.item_spinner, listacarreras);
        spinnercarrera.setAdapter(adaptador1);


        spinnersemetre = (Spinner) view.findViewById(R.id.sp_perfil_semestre);
        ArrayAdapter<CharSequence> adaptador2 = new ArrayAdapter(getActivity(), R.layout.item_spinner, listasemestre);
        spinnersemetre.setAdapter(adaptador2);

        spinnergrupo = (Spinner) view.findViewById(R.id.sp_perfil_grupo);
        ArrayAdapter<CharSequence> adaptador3 = new ArrayAdapter(getActivity(), R.layout.item_spinner, listagrupo);
        spinnergrupo.setAdapter(adaptador3);


        imagenid= (ImageView) view.findViewById(R.id.Imagenid);

        //boton siguente

        actualizar = (Button) view.findViewById(R.id.btn_guardar);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagen != null) {
                    byte[] NewEntryImage = ImageViewTobyte(imagen);
                    AddData(NewEntryImage);
                }
                verificarexistenia();


            }
        });

        imagenid.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mayRequestStoragePermission()) {
                    imagenid.setEnabled(true);
                    showOptions();
                }else {
                    imagenid.setEnabled(false);
                }
            }
        });

        //SELECCION EN CADA SPINNER
        spinnerunidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position != 0) {
                    IdSelectunidad = unidadeslist.get(position - 1).getId_unidad_a().intValue();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnercarrera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position != 0) {
                    IdSelectcarrera = carreraslist.get(position - 1).getId_carrera();
                    IdSelectcarrera1 = carreraslist.get(position - 1).getN_carrera();
                    carrerass=IdSelectcarrera1;


                    consultarlistasemestres();
                    ArrayAdapter<CharSequence> adaptador2 = new ArrayAdapter(getActivity(), R.layout.item_spinner, listasemestre);
                    spinnersemetre.setAdapter(adaptador2);
                    consultarlistagrupos();
                    ArrayAdapter<CharSequence> adaptador3 = new ArrayAdapter(getContext(), R.layout.item_spinner, listagrupo);
                    spinnergrupo.setAdapter(adaptador3);
                                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnersemetre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position != 0) {
                    IdSelectsemetre = semestrelist.get(position - 1).getId_semestre();
                    IdSelectsemetre1 = semestrelist.get(position - 1).getN_semestre();
                     semestress=IdSelectsemetre1;

                     consultarlistagrupos();
                     ArrayAdapter<CharSequence> adaptador3 = new ArrayAdapter(getContext(), R.layout.item_spinner, listagrupo);
                     spinnergrupo.setAdapter(adaptador3);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnergrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {
                if (position != 0) {
                    IdSelectGrupo = grupolist.get(position - 1).getId_grupo();
                    IdSelectGrupo1 = grupolist.get(position - 1).getN_grupo();
                    gruposs = IdSelectGrupo1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
    private void verificarexistenia(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select horario.id_horario,nombre as dia,n_materia as materia,min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio ," +
                " max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin, " +
                "n_maestro as Maestro,n_unidad_a as unidad , "+
                "substr(n_carrera,4,4) as Carrera,substr(n_semestre,1,1) as Semestre, n_aula as Aula, substr(n_grupo,2,1) as Grupo from Horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN maestro ON Horario.id_maestro = maestro.id_maestro " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera "+
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a "+
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre "+
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula "+
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo "+
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario "+
                "INNER JOIN dias ON detalle_materias.dia = dias.dia "+
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods "+
                "where uni_academica.id_unidad_a = '"+unidadess+"' and periods.id_unidad_a = '"+unidadess+"' and substr(n_carrera,4,4) = '"+carrerass+"' "+
                " and substr(n_semestre,1,1) = '"+semestress+"' and substr(n_grupo,2,1) = '"+gruposs+"' and visible = '1' "+
                "group by materias.n_materia " , null);
        if (cursor.getCount() > 0){
            if (IdSelectunidad != 0 || IdSelectcarrera != null || IdSelectsemetre != null || IdSelectGrupo != null) {
                actualizarperfil();
                Intent siguiente = new Intent(getActivity(), ResumenActivity.class);
                startActivity(siguiente);
            } else {
                Intent siguiente = new Intent(getActivity(), ResumenActivity.class);
                startActivity(siguiente);
            }
        }else{
            Toast.makeText(getContext(), "No existe ningun horario con esos datos, Porfavor verifique sus datos.", Toast.LENGTH_LONG).show();

        }
    }

    //agregar los byte en la base de datos
    private void AddData(byte[] newEntryImage) {
        Database conn = new Database(getActivity(), "BDHorario", null, 1);
        SQLiteDatabase db= conn.getReadableDatabase();
        String[] parametros={idPerfil2};
        ContentValues values=new ContentValues();
        values.put(utilidades.CAMPO_IMAGEN,newEntryImage);
        db.update(utilidades.TABLA_PERFIL,values,utilidades.CAMPO_ID_PERIL+"=?",parametros);
        db.close();
    }
    //convertir imagen a byte
    private byte[] ImageViewTobyte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,70,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //permisos para poder cargar imagen
    private boolean mayRequestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
           Snackbar snackbar = Snackbar.make(mRlView,"Los permisos son necesarios para poder usar la aplicacion",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
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

        }else{
           Snackbar snackbar = Snackbar.make(mRlView,"Los permisos son necesarios para poder usar la aplicacion",
                Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
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
        return false;
    }
    //opciones para elegir las imagenes
    private void showOptions() {

        final CharSequence[] option = {"Tomar foto", "Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige una Opcion :D");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto" ){
                    if (mRlView == null){

                    }

                    openCamera();
                }else if (option[which] == "Elegir de Galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona  imagen para la app"), SELECT_PICTURE);

                }else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    //opcion abrir camera
    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated){
            Long timestamp= System.currentTimeMillis()/1000;
            String imageName = timestamp.toString() + ".JPG";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator+ imageName;

            File newfile = new File(mPath);

            Intent intent = new Intent ( MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newfile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }
    //para actualizar el perfil, si tiene alguna modificacion.
    private void actualizarperfil() {
        SQLiteDatabase db= conn.getReadableDatabase();
        String[] parametros={idPerfil2};
        ContentValues values = new ContentValues();
        if (IdSelectunidad != 0) {
            values.put(utilidades.CAMPO_UNIDAD, IdSelectunidad);
        }
        if (IdSelectcarrera != null) {
            values.put(utilidades.CAMPO_CARRERA, IdSelectcarrera);
        }
        if (IdSelectsemetre != null) {
            values.put(utilidades.CAMPO_SEMESTRE, IdSelectsemetre);
        }
        if (IdSelectGrupo != null) {
            values.put(utilidades.CAMPO_GRUPO, IdSelectGrupo);
        }
        db.update(utilidades.TABLA_PERFIL,values,utilidades.CAMPO_ID_PERIL+"=?",parametros);
        db.close();
    }
    //obtener la imagen de la base de datos
    public void getimage(){
        SQLiteDatabase db = conn.getWritableDatabase();
        String sql = " SELECT * FROM PERFIL";
        Cursor cur=db.rawQuery(sql, new String[] {});
        while (cur.moveToNext()) {
            byte[] photo = cur.getBlob(5);
            if (photo != null) {
                bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                imagen.setImageBitmap(bmp);
            }
        } return;
    }
    //Orientacion de las Imagenes obtenidas en camara o galeria
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
    //LISTA PERFIL
    private void consultarelementoseleccionado() {
        conn = new Database(getActivity().getApplicationContext(), "BDHorario", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();

        perfil perfil = null;
        perfillist =new ArrayList<perfil>();
        unidades unidad = null;
        unidadesperfillist = new ArrayList<unidades>();
        carreras carrera = null;
        carrerasperfillist = new ArrayList<carreras>();
        semestre semestre = null;
        semestreperfillist = new ArrayList<semestre>();
        grupo grupo = null;
        grupoperfillist = new ArrayList<grupo>();

        Cursor cursor = db.rawQuery("SELECT  perfil.id_perfil,uni_academica.n_unidad_a, substr(n_carrera,4,4),substr(n_semestre,1,1),substr(n_grupo,2,1) FROM perfil INNER JOIN uni_academica ON perfil.id_unidad_a = uni_academica.id_unidad_a INNER JOIN carreras ON perfil.id_carrera = carreras.id_carrera INNER JOIN semestre ON perfil.id_semestre = semestre.id_semestre INNER JOIN grupo ON perfil.id_grupo = grupo.id_grupo ORDER BY id_perfil DESC", null);
        //while (cursor.moveToNext()) {
        if (cursor.moveToFirst()) {
            perfil= new perfil();
            unidad = new unidades();
            carrera = new carreras();
            semestre = new semestre();
            grupo = new grupo();
            perfil.setId_perfil(cursor.getInt(0));
            unidad.setN_unidad_a(cursor.getString(1));
            carrera.setN_carrera(cursor.getString(2));
            semestre.setN_semestre(cursor.getString(3));
            grupo.setN_grupo(cursor.getString(4));
            perfillist.add(perfil);
            unidadesperfillist.add(unidad);
            carrerasperfillist.add(carrera);
            semestreperfillist.add(semestre);
            grupoperfillist.add(grupo);
        }
        idPerfil2 = perfillist.get(0).getId_perfil().toString();
        consultarlistaunidades();
        consultarlistacarreras();
        consultarlistasemestres();
        consultarlistagrupos();
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
        listaunidades.add(unidadesperfillist.get(0).getN_unidad_a());
        //mostrar las demas unidades academica
       /* for (int i = 0; i < unidadeslist.size(); i++) {
            listaunidades.add(unidadeslist.get(i).getN_unidad_a());
        }*/
    }
    //LISTA CARRERAS
    private void consultarlistacarreras() {
        conn = new Database(getActivity().getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        carreras carrera = null;
        carreraslist = new ArrayList<carreras>();

        Cursor cursor = db.rawQuery("SELECT carreras.id_carrera,substr(n_carrera,4,4) FROM horario " +
                "inner join horario_clases on horario.id_horario_c = horario_clases.id_horario_c " +
                "inner join uni_academica on horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "inner join carreras on horario_clases.id_carrera = carreras.id_carrera " +
                "where uni_academica.id_unidad_a = '"+unidadess+"'  " +
                "group by substr(n_carrera ,4,4)",null);
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
        listacarreras.add(carrerasperfillist.get(0).getN_carrera());

        for (int i = 0; i < carreraslist.size(); i++) {
            listacarreras.add(carreraslist.get(i).getN_carrera());
        }
    }
    //LISTA SEMESTRES
    private void consultarlistasemestres() {
        conn = new Database(getActivity().getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        semestre semestre = null;
        semestrelist = new ArrayList<semestre>();

        Cursor cursor = db.rawQuery("SELECT semestre.id_semestre,substr(n_semestre,1,1) FROM horario " +
                "inner join horario_clases on horario.id_horario_c = horario_clases.id_horario_c " +
                "inner join uni_academica on horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "inner join carreras on horario_clases.id_carrera = carreras.id_carrera " +
                "inner join semestre on horario_clases.id_semestre = semestre.id_semestre " +
                "where uni_academica.id_unidad_a = '"+unidadess+"' and substr(n_carrera,4,4) = '"+carrerass+"' " +
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
        listasemestre.add(semestreperfillist.get(0).getN_semestre());

        for (int i = 0; i < semestrelist.size(); i++) {
            listasemestre.add(semestrelist.get(i).getN_semestre());
        }
    }
    //LISTA GRUPO
    private void consultarlistagrupos() {
        conn = new Database(getActivity().getApplicationContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        grupo grupo = null;
        grupolist = new ArrayList<grupo>();

        Cursor cursor = db.rawQuery("SELECT grupo.id_grupo,substr(n_grupo,2,1) FROM horario " +
                "inner join horario_clases on horario.id_horario_c = horario_clases.id_horario_c " +
                "inner join uni_academica on horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "inner join carreras on horario_clases.id_carrera = carreras.id_carrera " +
                "inner join semestre on horario_clases.id_semestre = semestre.id_semestre " +
                "inner join grupo on horario_clases.id_grupo = grupo.id_grupo " +
                "where uni_academica.id_unidad_a = '"+unidadess+"' and substr(n_carrera,4,4) = '"+carrerass+"' and substr(n_semestre,1,1) = '"+semestress+"' " +
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
            listagrupo.add(grupoperfillist.get(0).getN_grupo());

            for (int i = 0; i < grupolist.size(); i++) {
                listagrupo.add(grupolist.get(i).getN_grupo());

        }
    }

    private void consultarelementoseleccionados() {
        conn = new Database(getContext(), "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        perfil perfil = null;
        perfillistt =new ArrayList<perfil>();
        unidades unidad = null;
        unidadesperfillist = new ArrayList<unidades>();
        carreras carrera = null;
        carrerasperfillist = new ArrayList<carreras>();
        grupo grupo = null;
        grupoperfillist = new ArrayList<grupo>();
        Cursor cursor = db.rawQuery("SELECT  perfil.id_perfil,  uni_academica.id_unidad_a, substr(n_carrera,4,4),substr(n_semestre,1,1),substr(n_grupo,2,1)  FROM perfil INNER JOIN uni_academica ON perfil.id_unidad_a = uni_academica.id_unidad_a INNER JOIN carreras ON perfil.id_carrera = carreras.id_carrera INNER JOIN semestre ON perfil.id_semestre = semestre.id_semestre INNER JOIN grupo ON perfil.id_grupo = grupo.id_grupo ORDER BY id_perfil DESC", null);
        while (cursor.moveToNext()) {
            unidadess = String.valueOf(cursor.getString(1));
            carrerass =String.valueOf(cursor.getString(2));
            semestress =String.valueOf(cursor.getString(3));
            gruposs =String.valueOf(cursor.getString(4));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path",mPath);
    }


    //aqui se especifica donde se mostrara la imagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getContext(),
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned" + path + ":");
                                    Log.i("ExternalStorage", "-> Uri " + uri);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    imagen.setImageBitmap(bitmap);
                    //orientacion de las Imagenes obtenidas en camara
                    bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                    Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                    imagen.setImageBitmap(bmRotated);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    //Orientacion de las imagenes seleccionadas en galeria
                    String filePath = null;
                    try {
                        filePath = getPath(getContext(), path);
                    } catch (URISyntaxException e) {
                    }
                    String fileExtension = getFileExtension(filePath);
                    File file = new File(filePath);

                    imagen.setImageURI(path);
                    //orientacion de las Imagenes obtenidas en camara
                    bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                    ExifInterface exif1 = null;
                    try {
                        exif1 = new ExifInterface(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation1 = exif1.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                    Bitmap bmRotated1 = rotateBitmap(bitmap, orientation1);
                    imagen.setImageBitmap(bmRotated1);
                    break;
            }
        }
    }
    public  String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public String getFileExtension(String filePath){
        String extension = "";
        try{
            extension = filePath.substring(filePath.lastIndexOf("."));
        }catch(Exception exception){
            e("Err", exception.toString()+"");
        }
        return  extension;
    }
    //permisos para poder elegir las imagenes en el celular
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions , grantResults);

        if (requestCode == MY_PERMISSIONS){
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults [1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(),"Permisos aceptados",Toast.LENGTH_SHORT).show();
                imagenid.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }
    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permisos Denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("Package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
            }
        });
        builder.show();
    }

}

