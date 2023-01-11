package Entidad;

import java.io.Serializable;

public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_tipo_evento;
    private Long id_usuario;
    private Long fecha_hora;
    private String descripcion;

    public Evento(Long id_tipo_evento, Long id_usuario, Long fecha_hora, String descripcion) {
        this.id_tipo_evento = id_tipo_evento;
        this.id_usuario = id_usuario;
        this.fecha_hora = fecha_hora;
        this.descripcion = descripcion;
    }

    public Evento() {
    }

    public Long getId_tipo_evento() {
        return id_tipo_evento;
    }

    public void setId_tipo_evento(Long id_tipo_evento) {
        this.id_tipo_evento = id_tipo_evento;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Long getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Long fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Evento{" + "id_tipo_evento=" + id_tipo_evento + ", id_usuario=" + id_usuario + ", fecha_hora=" + fecha_hora + ", descripcion=" + descripcion + '}';
    }

}
