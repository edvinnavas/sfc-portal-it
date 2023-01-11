package Entidad;

import java.io.Serializable;

public class Rol_Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_rol;
    private Long id_menu;
    private String nombre_menu;

    public Rol_Menu(Long id_rol, Long id_menu, String nombre_menu) {
        this.id_rol = id_rol;
        this.id_menu = id_menu;
        this.nombre_menu = nombre_menu;
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }

    public Long getId_menu() {
        return id_menu;
    }

    public void setId_menu(Long id_menu) {
        this.id_menu = id_menu;
    }

    public String getNombre_menu() {
        return nombre_menu;
    }

    public void setNombre_menu(String nombre_menu) {
        this.nombre_menu = nombre_menu;
    }

}
