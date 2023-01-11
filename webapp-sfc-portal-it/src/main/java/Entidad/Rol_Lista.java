package Entidad;

import java.io.Serializable;

public class Rol_Lista implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_rol;
    private String nombre;
    private String activo;
    private String editable;
    private String eliminable;

    public Rol_Lista(Long id_rol, String nombre, String activo, String editable, String eliminable) {
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.activo = activo;
        this.editable = editable;
        this.eliminable = eliminable;
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getEliminable() {
        return eliminable;
    }

    public void setEliminable(String eliminable) {
        this.eliminable = eliminable;
    }

}
