package itsfcp.isc.edu.Hourhand.entidades;

public class perfil {
    private Integer id_perfil;
    private Integer id_unidad_a;
    private String id_carrera;
    private String id_semestre;
    private String id_grupo;
    private byte picture;


    public byte getPicture() {
        return picture;
    }

    public void setPicture() {
        this.picture = picture;
    }

    public perfil() {
        this.id_perfil = id_perfil;
        this.id_unidad_a = id_unidad_a;
        this.id_carrera = id_carrera;
        this.id_semestre = id_semestre;
        this.id_grupo = id_grupo;
    }

    public Integer getId_perfil() {
        return id_perfil;
    }

    public void setId_perfil(Integer id_perfil) {
        this.id_perfil = id_perfil;
    }

    public Integer getId_unidad_a() {
        return id_unidad_a;
    }

    public void setId_unidad_a(Integer id_unidad_a) {
        this.id_unidad_a = id_unidad_a;
    }

    public String getId_carrera() {
        return id_carrera;
    }

    public void setId_carrera(String id_carrera) { this.id_carrera = id_carrera; }

    public String getId_semestre() {
        return id_semestre;
    }

    public void setId_semestre(String id_semestre) {
        this.id_semestre = id_semestre;
    }

    public String getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(String id_grupo) {
        this.id_grupo = id_grupo;
    }
}