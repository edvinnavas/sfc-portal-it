package Controles;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Ctrl_Ambiente implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Ambiente() {

    }

    public String iniciar(Connection conn) {
        String resultado = "";

        try {
            // LIMPIAR TABLAS DE LA INICIALIZACIÓN DEL AMBIENTE.
            Controles.Ctrl_Base_Datos ctrl_base_datos = new Controles.Ctrl_Base_Datos();
            ctrl_base_datos.EjecutarCMD("DELETE FROM LOG_EVENTO WHERE ID_EVENTO > 0", conn);
            ctrl_base_datos.EjecutarCMD("DELETE FROM TIPO_EVENTO WHERE ID_TIPO_EVENTO > 0", conn);
            ctrl_base_datos.EjecutarCMD("DELETE FROM USUARIO WHERE ID_USUARIO > 0", conn);
            ctrl_base_datos.EjecutarCMD("DELETE FROM ROL_MENU WHERE ID_ROL > 0", conn);
            ctrl_base_datos.EjecutarCMD("DELETE FROM ROL WHERE ID_ROL > 0", conn);
            ctrl_base_datos.EjecutarCMD("DELETE FROM MENU WHERE ID_MENU > 0", conn);
            ctrl_base_datos.EjecutarCMD("DELETE FROM TIPO_MENU WHERE ID_TIPO_MENU > 0", conn);

            // OBJETO PARA SERIALIZAR JSON.
            Gson gson = new GsonBuilder().serializeNulls().create();

            // SECCIÓN PARA CREAR LOS TIPO DE MENU.
            Controles.Ctrl_Tipo_Menu ctrl_tipo_menu = new Controles.Ctrl_Tipo_Menu();

            Entidades.Tipo_Menu tipo_menu_1 = new Entidades.Tipo_Menu();
            tipo_menu_1.setId_tipo_menu(null);
            tipo_menu_1.setNombre("MENU");
            tipo_menu_1.setActivo(1);
            tipo_menu_1.setDescripcion("Tipo de menú creado por el sistema.");
            tipo_menu_1.setUsuario_m("SISTEMA");
            tipo_menu_1.setFecha_hora(new Date());
            tipo_menu_1 = ctrl_tipo_menu.crear(conn, gson.toJson(tipo_menu_1));

            Entidades.Tipo_Menu tipo_menu_2 = new Entidades.Tipo_Menu();
            tipo_menu_2.setId_tipo_menu(null);
            tipo_menu_2.setNombre("MENU-ITEM");
            tipo_menu_2.setActivo(1);
            tipo_menu_2.setDescripcion("Tipo de menú creado por el sistema.");
            tipo_menu_2.setUsuario_m("SISTEMA");
            tipo_menu_2.setFecha_hora(new Date());
            tipo_menu_2 = ctrl_tipo_menu.crear(conn, gson.toJson(tipo_menu_2));

            // SECCIÓN PARA CREAR LOS MENU.
            Controles.Ctrl_Menu ctrl_menu = new Controles.Ctrl_Menu();

            Entidades.Menu menu_1 = new Entidades.Menu();
            menu_1.setId_menu(null);
            menu_1.setNombre("Seguridad");
            menu_1.setTipo_menu(tipo_menu_1);
            menu_1.setActivo(1);
            menu_1.setDescripcion("Menú creado por el sistema.");
            menu_1.setUsuario_m("SISTEMA");
            menu_1.setFecha_hora(new Date());
            menu_1 = ctrl_menu.crear(conn, gson.toJson(menu_1));

            Entidades.Menu menu_2 = new Entidades.Menu();
            menu_2.setId_menu(null);
            menu_2.setNombre("Rol");
            menu_2.setTipo_menu(tipo_menu_2);
            menu_2.setActivo(1);
            menu_2.setDescripcion("Menú creado por el sistema.");
            menu_2.setUsuario_m("SISTEMA");
            menu_2.setFecha_hora(new Date());
            menu_2 = ctrl_menu.crear(conn, gson.toJson(menu_2));

            Entidades.Menu menu_3 = new Entidades.Menu();
            menu_3.setId_menu(null);
            menu_3.setNombre("Usuario");
            menu_3.setTipo_menu(tipo_menu_2);
            menu_3.setActivo(1);
            menu_3.setDescripcion("Menú creado por el sistema.");
            menu_3.setUsuario_m("SISTEMA");
            menu_3.setFecha_hora(new Date());
            menu_3 = ctrl_menu.crear(conn, gson.toJson(menu_3));

            Entidades.Menu menu_4 = new Entidades.Menu();
            menu_4.setId_menu(null);
            menu_4.setNombre("Cambio contraseña");
            menu_4.setTipo_menu(tipo_menu_2);
            menu_4.setActivo(1);
            menu_4.setDescripcion("Menú creado por el sistema.");
            menu_4.setUsuario_m("SISTEMA");
            menu_4.setFecha_hora(new Date());
            menu_4 = ctrl_menu.crear(conn, gson.toJson(menu_4));

            // SECCIÓN PARA CREAR LOS ROL.
            Controles.Ctrl_Rol ctrl_rol = new Controles.Ctrl_Rol();

            Entidades.Rol rol_1 = new Entidades.Rol();
            rol_1.setId_rol(null);
            rol_1.setNombre("ADMINISTRADOR");
            rol_1.setActivo(1);
            rol_1.setDescripcion("Rol creado por el sistema.");
            rol_1.setUsuario_m("SISTEMA");
            rol_1.setFecha_hora(new Date());
            rol_1 = ctrl_rol.crear(conn, gson.toJson(rol_1));

            List<Entidades.Menu> lista_menu_1 = new ArrayList<>();
            lista_menu_1.add(menu_1);
            lista_menu_1.add(menu_2);
            lista_menu_1.add(menu_3);
            lista_menu_1.add(menu_4);
            Entidades.Rol_Menu rol_menu_1 = new Entidades.Rol_Menu();
            rol_menu_1.setRol(rol_1);
            rol_menu_1.setLista_menu(lista_menu_1);
            rol_menu_1.setUsuario_m("SISTEMA");
            rol_menu_1.setFecha_hora(null);
            rol_menu_1 = ctrl_rol.crear_lista_rol_menu(conn, rol_1.getId_rol(), gson.toJson(rol_menu_1));

            Entidades.Rol rol_2 = new Entidades.Rol();
            rol_2.setId_rol(null);
            rol_2.setNombre("IT-OPERATIVO");
            rol_2.setActivo(1);
            rol_2.setDescripcion("Rol creado por el sistema.");
            rol_2.setUsuario_m("SISTEMA");
            rol_2.setFecha_hora(new Date());
            rol_2 = ctrl_rol.crear(conn, gson.toJson(rol_2));

            List<Entidades.Menu> lista_menu_2 = new ArrayList<>();
            lista_menu_2.add(menu_1);
            lista_menu_2.add(menu_4);
            Entidades.Rol_Menu rol_menu_2 = new Entidades.Rol_Menu();
            rol_menu_2.setRol(rol_2);
            rol_menu_2.setLista_menu(lista_menu_2);
            rol_menu_2.setUsuario_m("SISTEMA");
            rol_menu_2.setFecha_hora(null);
            rol_menu_2 = ctrl_rol.crear_lista_rol_menu(conn, rol_2.getId_rol(), gson.toJson(rol_menu_2));

            // SECCIÓN PARA CREAR LOS USUARIO.
            Controles.Ctrl_Usuario ctrl_usuario = new Controles.Ctrl_Usuario();

            Entidades.Usuario usuario_1 = new Entidades.Usuario();
            usuario_1.setId_usuario(null);
            usuario_1.setNombre_completo("Edvin Francisco Navas Mejía");
            usuario_1.setNombre_usuario("enavas");
            usuario_1.setConstrasena("Edfr@2024");
            usuario_1.setCorreo_electronico("enavas@servicioscompartidos.com");
            usuario_1.setActivo(1);
            usuario_1.setAutenticacion("LOCAL");
            usuario_1.setDescripcion("Usuario creado por el sistema.");
            usuario_1.setRol(rol_1);
            usuario_1.setUsuario_m("SISTEMA");
            usuario_1.setFecha_hora(new Date());
            usuario_1 = ctrl_usuario.crear(conn, gson.toJson(usuario_1));

            // SECCIÓN PARA CREAR LOS TIPO DE EVENTO.
            Controles.Ctrl_Tipo_Evento ctrl_tipo_evento = new Controles.Ctrl_Tipo_Evento();

            Entidades.Tipo_Evento tipo_evento_1 = new Entidades.Tipo_Evento();
            tipo_evento_1.setId_tipo_evento(null);
            tipo_evento_1.setNombre("AUTENTICACION");
            tipo_evento_1.setActivo(1);
            tipo_evento_1.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_1.setUsuario_m("SISTEMA");
            tipo_evento_1.setFecha_hora(new Date());
            tipo_evento_1 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_1));

            Entidades.Tipo_Evento tipo_evento_2 = new Entidades.Tipo_Evento();
            tipo_evento_2.setId_tipo_evento(null);
            tipo_evento_2.setNombre("ROL-CREAR");
            tipo_evento_2.setActivo(1);
            tipo_evento_2.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_2.setUsuario_m("SISTEMA");
            tipo_evento_2.setFecha_hora(new Date());
            tipo_evento_2 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_2));

            Entidades.Tipo_Evento tipo_evento_3 = new Entidades.Tipo_Evento();
            tipo_evento_3.setId_tipo_evento(null);
            tipo_evento_3.setNombre("ROL-MODIFICAR");
            tipo_evento_3.setActivo(1);
            tipo_evento_3.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_3.setUsuario_m("SISTEMA");
            tipo_evento_3.setFecha_hora(new Date());
            tipo_evento_3 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_3));

            Entidades.Tipo_Evento tipo_evento_4 = new Entidades.Tipo_Evento();
            tipo_evento_4.setId_tipo_evento(null);
            tipo_evento_4.setNombre("ROL-ELIMINAR");
            tipo_evento_4.setActivo(1);
            tipo_evento_4.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_4.setUsuario_m("SISTEMA");
            tipo_evento_4.setFecha_hora(new Date());
            tipo_evento_4 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_4));

            Entidades.Tipo_Evento tipo_evento_5 = new Entidades.Tipo_Evento();
            tipo_evento_5.setId_tipo_evento(null);
            tipo_evento_5.setNombre("USUARIO-CREAR");
            tipo_evento_5.setActivo(1);
            tipo_evento_5.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_5.setUsuario_m("SISTEMA");
            tipo_evento_5.setFecha_hora(new Date());
            tipo_evento_5 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_5));

            Entidades.Tipo_Evento tipo_evento_6 = new Entidades.Tipo_Evento();
            tipo_evento_6.setId_tipo_evento(null);
            tipo_evento_6.setNombre("USUARIO-MODIFICAR");
            tipo_evento_6.setActivo(1);
            tipo_evento_6.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_6.setUsuario_m("SISTEMA");
            tipo_evento_6.setFecha_hora(new Date());
            tipo_evento_6 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_6));

            Entidades.Tipo_Evento tipo_evento_7 = new Entidades.Tipo_Evento();
            tipo_evento_7.setId_tipo_evento(null);
            tipo_evento_7.setNombre("USUARIO-ELIMINAR");
            tipo_evento_7.setActivo(1);
            tipo_evento_7.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_7.setUsuario_m("SISTEMA");
            tipo_evento_7.setFecha_hora(new Date());
            tipo_evento_7 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_7));

            Entidades.Tipo_Evento tipo_evento_8 = new Entidades.Tipo_Evento();
            tipo_evento_8.setId_tipo_evento(null);
            tipo_evento_8.setNombre("USUARIO-CAMBIAR-CONTRASEÑA");
            tipo_evento_8.setActivo(1);
            tipo_evento_8.setDescripcion("Tipo de evento creado por el sistema.");
            tipo_evento_8.setUsuario_m("SISTEMA");
            tipo_evento_8.setFecha_hora(new Date());
            tipo_evento_8 = ctrl_tipo_evento.crear(conn, gson.toJson(tipo_evento_8));

            resultado = "AMBIENTE INICIADO CORRECTAMENTE.";
        } catch (Exception ex) {
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: iniciar()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: iniciar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Usuario prueba_metodos(Connection conn) {
        Entidades.Usuario resultado = new Entidades.Usuario();

        try {
            // SECCIÓN PARA CREAR LOS USUARIO.
            Controles.Ctrl_Usuario ctrl_usuario = new Controles.Ctrl_Usuario();
            resultado = ctrl_usuario.autenticar(conn, "enavas", "Edfr@2024");
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: iniciar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

}
