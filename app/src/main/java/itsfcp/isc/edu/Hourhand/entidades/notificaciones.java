package itsfcp.isc.edu.Hourhand.entidades;

public class notificaciones {
    private Integer idnotificaciones;
    private String notificar;
    private String sonido;
    private String vibracion;

    public notificaciones(Integer idnotificaciones, String notificar, String sonido, String vibracion) {
        this.idnotificaciones = idnotificaciones;
        this.notificar = notificar;
        this.sonido = sonido;
        this.vibracion = vibracion;
    }

    public Integer getIdnotificaciones() {
        return idnotificaciones;
    }

    public void setIdnotificaciones(Integer idnotificaciones) {
        this.idnotificaciones = idnotificaciones;
    }

    public String getNotificar() {
        return notificar;
    }

    public void setNotificar(String notificar) {
        this.notificar = notificar;
    }

    public String getSonido() {
        return sonido;
    }

    public void setSonido(String sonido) {
        this.sonido = sonido;
    }

    public String getVibracion() {
        return vibracion;
    }

    public void setVibracion(String vibracion) {
        this.vibracion = vibracion;
    }
}