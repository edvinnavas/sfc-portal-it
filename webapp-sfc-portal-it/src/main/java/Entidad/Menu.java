package Entidad;

import java.io.Serializable;

public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_menu;
    private String nombre;

    public Menu(Long id_menu, String nombre) {
        this.id_menu = id_menu;
        this.nombre = nombre;
    }

    public Long getId_menu() {
        return id_menu;
    }

    public void setId_menu(Long id_menu) {
        this.id_menu = id_menu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
