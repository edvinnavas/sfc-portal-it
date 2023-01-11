package Entidad;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_usuario;
    private String usuario;
    private String nombre_completo;
    private String correo;
    private Long fecha_nacimiento;
    private String contrasena;
    private String descripcion;
    private Integer activo;
    private Integer editable;
    private Integer eliminable;
    private Long id_rol;

    public Usuario(Long id_usuario, String usuario, String nombre_completo, String correo, Long fecha_nacimiento, String contrasena, String descripcion, Integer activo, Integer editable, Integer eliminable, Long id_rol) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.nombre_completo = nombre_completo;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contrasena = contrasena;
        this.descripcion = descripcion;
        this.activo = activo;
        this.editable = editable;
        this.eliminable = eliminable;
        this.id_rol = id_rol;
    }

    public Usuario() {
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

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Long fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }

    public Integer getEliminable() {
        return eliminable;
    }

    public void setEliminable(Integer eliminable) {
        this.eliminable = eliminable;
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id_usuario=" + id_usuario + ", usuario=" + usuario + ", nombre_completo=" + nombre_completo + ", correo=" + correo + ", fecha_nacimiento=" + fecha_nacimiento + ", contrasena=" + contrasena + ", descripcion=" + descripcion + ", activo=" + activo + ", editable=" + editable + ", eliminable=" + eliminable + ", id_rol=" + id_rol + '}';
    }

}
