package Entidad;

import java.io.Serializable;
import java.util.List;

public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_rol;
    private String nombre;
    private Integer activo;
    private Integer editable;
    private Integer eliminable;
    private String descripcion;
    private List<Rol_Menu> lst_menu;

    public Rol(Long id_rol, String nombre, Integer activo, Integer editable, Integer eliminable, String descripcion, List<Rol_Menu> lst_menu) {
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.activo = activo;
        this.editable = editable;
        this.eliminable = eliminable;
        this.descripcion = descripcion;
        this.lst_menu = lst_menu;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Rol_Menu> getLst_menu() {
        return lst_menu;
    }

    public void setLst_menu(List<Rol_Menu> lst_menu) {
        this.lst_menu = lst_menu;
    }

}
