package itsfcp.isc.edu.Hourhand.entidades;

import android.content.Context;
import android.content.SharedPreferences;

public class agenda {

    private static final String APP_SHARED_PREFS = "Recordar";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private Integer id_agenda;
    private String titulo_agenda;
    private String fecha_agenda;
    private String hora_agenda;
    private String decrip_agenda;
    private Integer check_agenda;

    public agenda(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public agenda(){}

    public agenda(Integer id_agenda,String titulo_agenda, String fecha_agenda, String hora_agenda, String decrip_agenda, Integer check_agenda) {
        this.id_agenda = id_agenda;
        this.titulo_agenda = titulo_agenda;
        this.fecha_agenda = fecha_agenda;
        this.hora_agenda = hora_agenda;
        this.decrip_agenda = decrip_agenda;
        this.check_agenda = check_agenda;
    }

    private static agenda instance = null;

    public static agenda getInstance() {
        if (instance == null) {
            synchronized (agenda.class) {
                if (instance == null) {
                    instance = new agenda();
                }
            }
        }
        return instance;
    }

    public static void setInstance(agenda instance) {
        agenda.instance = instance;
    }

    public void reset(){
        instance = new agenda();
    }

    public Integer getId_agenda() {return id_agenda;}

    public void setId_agenda(Integer id_agenda) {
        this.id_agenda = id_agenda;
    }

    public String getTitulo_agenda() {
        return titulo_agenda;
    }

    public void setTitulo_agenda(String titulo_agenda) {
        this.titulo_agenda = titulo_agenda;
    }

    public String getFecha_agenda() {
        return fecha_agenda;
    }

    public void setFecha_agenda(String fecha_agenda) {
        this.fecha_agenda = fecha_agenda;
    }

    public String getHora_agenda() {
        return hora_agenda;
    }

    public void setHora_agenda(String hora_agenda) {
        this.hora_agenda = hora_agenda;
    }

    public String getDecrip_agenda() {
        return decrip_agenda;
    }

    public void setDecrip_agenda(String decrip_agenda) {
        this.decrip_agenda = decrip_agenda;
    }

    public boolean getIdNull(){
        return this.id_agenda == null ? true : false;
    }

    public Integer getcheck_agenda() {
        return check_agenda;
    }

    public void setcheck_agenda(Integer check_agenda) {
        check_agenda = check_agenda;
    }
}