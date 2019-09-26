package itsfcp.isc.edu.Hourhand.utilidades;

public class utilidades {

    //constantes de los campos de tabla unidades
    public static final String TABLA_UNIDAD="uni_academica";
    public static final String CAMPO_ID="id_unidad_a";
    public static final String CAMPO_NOMBRE="n_unidad_a";
    public static final  String CREAR_TABLA_UNIDAD ="CREATE TABLE "+TABLA_UNIDAD+"("+CAMPO_ID+" INTEGER, "+CAMPO_NOMBRE+" STRING)";
    //constantes de los campos de tabla agenda
    public static final String TABLA_NOTIFICACIONES="notificaciones";
    public static final String CAMPO_ID_NOTIFICACIONES="idnotificaciones";
    public static final String CAMPO_NOTIFICAR="notificar";
    public static final String CAMPO_SONIDO="sonido";
    public static final String CAMPO_VIBRACION="vibracion";

    public static final String TABLA_AGENDA="agenda";
    public static final String CAMPO_ID_AGENDA="id_agenda";
    public static final String CAMPO_TITULO="titulo_agenda";
    public static final String CAMPO_FECHA="fecha_agenda";
    public static final String CAMPO_HORA="hora_agenda";
    public static final String CAMPO_DESCRIPCION="decrip_agenda";
    public static final String CAMPO_CHECK = "check_agenda";
    //constantes de los campos de tabla perfil
    public static final String TABLA_PERFIL="perfil";
    public static final String CAMPO_ID_PERIL="id_perfil";
    public static final String CAMPO_IMAGEN="picture";
    public static final String CAMPO_UNIDAD="id_unidad_a";
    public static final String CAMPO_CARRERA="id_carrera";
    public static final String CAMPO_SEMESTRE="id_semestre";
    public static final String CAMPO_GRUPO="id_grupo";

    public static final String TABLA_CARRERA="carreras";
    public static final String CAMPO_ID_CARRERA="id_carrera";
    public static final String CAMPO_NOMBRE_CARRERA="n_carrera";
    public static final String CAMPO_CLAVE_CARRERA="clave_carrera";

    public static final String TABLA_SEMESTRE="semestre";
    public static final String CAMPO_ID_SEMESTRE="id_semestre";
    public static final String CAMPO_NOMBRE_SEMESTRE="n_semestre";

    public static final String TABLA_GRUPO="grupo";
    public static final String CAMPO_ID_GRUPO="id_grupo";
    public static final String CAMPO_NOMBRE_GRUPO="n_grupo";


    public static final String TABLA_MATERIA="materias";
    public static final String CAMPO_ID_MATERIA="id_materia";
    public static final String CAMPO_NOMBRE_MATERIA="n_materia";

    public static final String TABLA_HORARIO_IRREGULARES="horario_irregulares";
    public static final String CAMPO_ID_HORARIO_IRREGULARES="id_horario_irregulares";
    public static final String CAMPO_ID_HORARIO="id_horario";

    public static final String TABLA_HORARIO="horario";
    public static final String CAMPO_ID_HORARIOS="id_horario";
    public static final String CAMPO_ID_HORARIO_C="id_horario_c";
    public static final String CAMPO_IDS_MATERIA="id_materia";
    public static final String CAMPO_IDS_MAESTRO="id_maestro";
    public static final String CAMPO_VISIBLE="visible";




}