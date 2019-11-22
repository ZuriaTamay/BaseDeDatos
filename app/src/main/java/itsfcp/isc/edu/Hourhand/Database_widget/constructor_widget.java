package itsfcp.isc.edu.Hourhand.Database_widget;

import android.support.v7.app.AppCompatActivity;

public class constructor_widget extends AppCompatActivity  {
    private String dia;
    private String materia;
    private String inicio;
    private String fin;
    private String unidad;
    private String carrera;
    private String semestre;
    private String aula;
    private Integer id_horario;
    private boolean visible;

    public boolean getVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public Integer getId_horario() {
        return id_horario;
    }
    public void setId_horario(Integer id_horario) {
        this.id_horario = id_horario;
    }
    public constructor_widget(Integer id_horario,String materia, String inicio, String fin, String aula) {
        this.id_horario = id_horario;
        this.dia = dia;
        this.materia = materia;
        this.inicio = inicio;
        this.fin = fin;
        this.unidad = unidad;
        this.carrera = carrera;
        this.semestre = semestre;
        this.aula = aula;

    }
    public String getDia() {

        return dia;
    }
    public void setDia(String dia) {

        this.dia = dia;
    }
    public String getMateria() {

        return materia;
    }
    public void setMateria(String materia) {
        this.materia = materia;
    }
    public String getInicio() {
        return inicio;
    }
    public void setInicio(String inicio) {
        this.inicio = inicio;
    }
    public String getFin() {
        return fin;
    }
    public void setFin(String fin) {
        this.fin = fin;
    }
    public String getUnidad() {
        return unidad;
    }
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    public String getSemestre() {
        return semestre;
    }
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
    public String getAula() {
        return aula;
    }
    public void setAula(String aula) {
        this.aula = aula;
    }
}