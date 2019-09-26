package itsfcp.isc.edu.Hourhand.entidades;

public class addhorario {

    private Integer id_horario;
    private String materia;
    private String maestro;
    private String unidad;
    private String carrera;
    private String semestre;
    private String aula;
    private String grupo;

    public addhorario(){}

    public Integer getId_horario() {
        return id_horario;
    }

    public void setId_horario(Integer id_horario) {
        this.id_horario = id_horario;
    }

    public addhorario(Integer id_horario,String materia, String maestro) {
        this.id_horario = id_horario;
        this.materia = materia;
        this.maestro = maestro;
        this.unidad = unidad;
        this.carrera = carrera;
        this.semestre = semestre;
        this.aula = aula;
        this.grupo = grupo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
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