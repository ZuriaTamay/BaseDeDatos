package itsfcp.isc.edu.Hourhand.Database_widget;
//lol

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.util.ArrayList;
import butterknife.BindView;
import itsfcp.isc.edu.Hourhand.Database;
import itsfcp.isc.edu.Hourhand.R;
import itsfcp.isc.edu.Hourhand.adapters.HorarioAdapter;
import itsfcp.isc.edu.Hourhand.entidades.carreras;
import itsfcp.isc.edu.Hourhand.entidades.grupo;
import itsfcp.isc.edu.Hourhand.entidades.horario;
import itsfcp.isc.edu.Hourhand.entidades.perfil;
import itsfcp.isc.edu.Hourhand.entidades.semestre;
import itsfcp.isc.edu.Hourhand.entidades.unidades;

public class consulta_widget extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Database conn;
    ArrayList<horario> listahorarios;
    ArrayList<perfil>perfillist;
    String unidades;
    String carreras;
    String semestres;
    String grupos;
    String dia;
    
    private HorarioAdapter horarioAdapter;
    @BindView(R.id.horario_list)
    View horariosReciclerView;
    //unidades
    ArrayList<itsfcp.isc.edu.Hourhand.entidades.unidades> unidadeslist;
    ArrayList<unidades> unidadesperfillist;
    //carreras
    ArrayList<itsfcp.isc.edu.Hourhand.entidades.carreras> carreraslist;
    ArrayList<carreras> carrerasperfillist;
    //semestre
    ArrayList<semestre> semestreperfillist;
    //grupo
    ArrayList<grupo> grupoperfillist;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private void consultalistaPersonas() {
        Database conn = new Database(consulta_widget.this, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        horario horarios = null;
        listahorarios = new ArrayList<>();

        String Consulta ="Select horario.id_horario,nombre as dia,n_materia as materia,min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio ," +
                " max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin, " +
                "n_maestro as Maestro,n_unidad_a as unidad , "+
                "substr(n_carrera,4,4) as Carrera,substr(n_semestre,1,1) as Semestre, n_aula as Aula, substr(n_grupo,2,1) as Grupo from Horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera "+
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a "+
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre "+
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula "+
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo "+
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario "+
                "INNER JOIN dias ON detalle_materias.dia = dias.dia "+
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods "+
                "where nombre = '"+dia+"' and uni_academica.id_unidad_a = '"+unidades+"' and periods.id_unidad_a = '"+unidades+"' and substr(n_carrera,4,4) = '"+carreras+"' and substr(n_semestre,1,1) = '"+semestres+"' and substr(n_grupo,2,1) = '"+grupos+"' and visible = '1' group by materias.n_materia " +
                " UNION " +
                "Select horario.id_horario,nombre as dia,n_materia as materia,min(CASE WHEN length(Hora_inicio) = 4 then '0' || Hora_inicio ELSE Hora_inicio END) as Hora_inicio , " +
                "max(CASE WHEN length(hora_fin) = 4 then '0' || hora_fin ELSE hora_fin END) as hora_fin, " +
                "n_maestro as Maestro,n_unidad_a as unidad , "+
                "substr(n_carrera,4,4) as Carrera,substr(n_semestre,1,1) as Semestre, n_aula as Aula, substr(n_grupo,2,1) as Grupo " +
                "from horario_irregulares " +
                "INNER JOIN horario ON horario.id_horario = horario_irregulares.id_horario " +
                "INNER JOIN materias ON Horario.id_materia = materias.id_materia " +
                "INNER JOIN horario_clases ON Horario.id_horario_c = horario_clases.id_horario_c " +
                "INNER JOIN carreras ON horario_clases.id_carrera = carreras.id_carrera " +
                "INNER JOIN uni_academica ON horario_clases.id_unidad_a = uni_academica.id_unidad_a " +
                "INNER JOIN semestre ON horario_clases.id_semestre = semestre.id_semestre " +
                "INNER JOIN aula ON horario_clases.id_aula = aula.id_aula " +
                "INNER JOIN grupo ON horario_clases.id_grupo = grupo.id_grupo " +
                "INNER JOIN detalle_materias ON horario.id_horario = detalle_materias.id_horario " +
                "INNER JOIN dias ON detalle_materias.dia = dias.dia " +
                "INNER JOIN periods ON detalle_materias.id_periods = periods.id_periods " +
                "where nombre = '"+dia+"' and periods.id_unidad_a = '"+ unidades +"'  group by materias.n_materia ORDER BY  Hora_inicio, hora_fin ASC  ";

        Cursor cursor = db.rawQuery(Consulta,null);
        while (cursor.moveToNext()) {
            horarios = new horario();
            horarios.setId_horario(cursor.getInt(0));
            horarios.setDia(cursor.getString(1));
            horarios.setMateria(cursor.getString(2));
            horarios.setInicio(cursor.getString(3));
            horarios.setFin(cursor.getString(4));
            horarios.setMaestro(cursor.getString(5));
            horarios.setUnidad(cursor.getString(6));
            horarios.setCarrera(cursor.getString(7));
            horarios.setSemestre(cursor.getString(8));
            horarios.setAula(cursor.getString(9));
            horarios.setGrupo(cursor.getString(10));
            listahorarios.add(horarios);
        }
    }
    private void obtenerlista() {
        final ArrayList<horario> listahorario = new ArrayList<horario>() {
            {
                for (int i = 0; i < listahorarios.size(); i++) {

                    add(new horario(listahorarios.get(i).getId_horario(),listahorarios.get(i).getMateria(),listahorarios.get(i).getInicio(), listahorarios.get(i).getFin(),listahorarios.get(i).getMaestro(), listahorarios.get(i).getAula()));
                }
            }};
        horarioAdapter.updateItems(listahorario);
    }
    private void consultarelementoseleccionado() {
        conn = new Database(consulta_widget.this, "BDHorario", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        perfil perfil = null;
        perfillist = new ArrayList<perfil>();
        unidades unidad = null;
        unidadesperfillist = new ArrayList<unidades>();
        carreras carrera = null;
        carrerasperfillist = new ArrayList<carreras>();
        semestre semestre = null;
        semestreperfillist = new ArrayList<semestre>();
        grupo grupo = null;
        grupoperfillist = new ArrayList<grupo>();
        Cursor cursor = db.rawQuery("SELECT  perfil.id_perfil,  uni_academica.id_unidad_a,  substr(n_carrera,4,4),substr(n_semestre,1,1),substr(n_grupo,2,1) FROM perfil INNER JOIN uni_academica ON perfil.id_unidad_a = uni_academica.id_unidad_a INNER JOIN carreras ON perfil.id_carrera = carreras.id_carrera INNER JOIN semestre ON perfil.id_semestre = semestre.id_semestre INNER JOIN grupo ON perfil.id_grupo = grupo.id_grupo ORDER BY id_perfil DESC", null);
        while (cursor.moveToNext()) {
            unidades = String.valueOf(cursor.getString(1));
            carreras = String.valueOf(cursor.getString(2));
            semestres = String.valueOf(cursor.getString(3));
            grupos = String.valueOf(cursor.getString(4));
        }
    }

}
