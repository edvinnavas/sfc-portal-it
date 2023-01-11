package Entidad;

import java.io.Serializable;

public class Usuario_Lista implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_usuario;
    private String usuario;
    private String rol;
    private String activo;
    private String editable;
    private String eliminable;

    public Usuario_Lista(Long id_usuario, String usuario, String rol, String activo, String editable, String eliminable) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.rol = rol;
        this.activo = activo;
        this.editable = editable;
        this.eliminable = eliminable;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
