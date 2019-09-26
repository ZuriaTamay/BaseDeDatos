package itsfcp.isc.edu.Hourhand.entidades;

public class horario {
    private String dia;
    private String materia;
    private String inicio;
    private String fin;
    private String maestro;
    private String unidad;
    private String carrera;
    private String semestre;
    private String aula;
    private String grupo;
    private Integer id_horario;
    private boolean visible;

    public horario(){}

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

    public horario(Integer id_horario,String materia, String inicio, String fin, String maestro, String aula) {
        this.id_horario = id_horario;
        this.dia = dia;
        this.materia = materia;
        this.inicio = inicio;
        this.fin = fin;
        this.maestro = maestro;
        this.unidad = unidad;
        this.carrera = carrera;
        this.semestre = semestre;
        this.aula = aula;
        this.grupo = grupo;
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

    public String getMaestro() {
        return maestro;
    }

    public void setMaestro(String maestro) {
        this.maestro = maestro;
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

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}