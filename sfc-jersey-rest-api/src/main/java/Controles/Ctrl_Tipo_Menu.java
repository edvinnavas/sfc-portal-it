package Controles;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Ctrl_Tipo_Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Tipo_Menu() {

    }

    public String obtener_lista() {
        String resultado = null;

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            List<Entidades.Tipo_Menu> lista_tipo_menu = new ArrayList<>();

            String sql = "SELECT "
                    + "A.ID_TIPO_MENU, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "TIPO_MENU A";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entidades.Tipo_Menu tipo_menu = new Entidades.Tipo_Menu();
                tipo_menu.setId_tipo_menu(rs.getLong(1));
                tipo_menu.setNombre(rs.getString(2));
                tipo_menu.setActivo(rs.getInt(3));
                tipo_menu.setDescripcion(rs.getString(4));
                tipo_menu.setUsuario_m(rs.getString(5));
                tipo_menu.setFecha_hora(rs.getDate(6));

                lista_tipo_menu.add(tipo_menu);
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(lista_tipo_menu);
        } catch (Exception ex) {
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        } finally {
            try {
                if(conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch(Exception ex) {
                System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()-Finally" + " ERROR: " + ex.toString());
            }
        }

        return resultado;
    }

    public String obtener_id(Long id_tipo_menu) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            Entidades.Tipo_Menu tipo_menu = new Entidades.Tipo_Menu();

            String sql = "SELECT "
                    + "A.ID_TIPO_MENU, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "TIPO_MENU A "
                    + "WHERE "
                    + "A.ID_TIPO_MENU=" + id_tipo_menu;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo_menu.setId_tipo_menu(rs.getLong(1));
                tipo_menu.setNombre(rs.getString(2));
                tipo_menu.setActivo(rs.getInt(3));
                tipo_menu.setDescripcion(rs.getString(4));
                tipo_menu.setUsuario_m(rs.getString(5));
                tipo_menu.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_menu);
        } catch (Exception ex) {
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_id()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_id()" + " ERROR: " + ex.toString());
        } finally {
            try {
                if(conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch(Exception ex) {
                System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_id()-Finally" + " ERROR: " + ex.toString());
            }
        }

        return resultado;
    }

    public String crear(String jsonString) {
        String resultado = "";

        Connection conn = null;

        try {
            Type ObjectType = new TypeToken<Entidades.Tipo_Menu>() {
            }.getType();
            Entidades.Tipo_Menu tipo_menu = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            Long id_tipo_menu = ctrl_base_datos.ObtenerLong("SELECT IFNULL(MAX(A.ID_TIPO_MENU),0) + 1 MAXIMO FROM TIPO_MENU A", conn);
            tipo_menu.setId_tipo_menu(id_tipo_menu);

            tipo_menu.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "INSERT INTO TIPO_MENU ("
                    + "ID_TIPO_MENU,"
                    + "NOMBRE,"
                    + "ACTIVO,"
                    + "DESCRIPCION,"
                    + "USUARIO_M,"
                    + "FECHA_HORA) VALUES ("
                    + tipo_menu.getId_tipo_menu() + ",'"
                    + tipo_menu.getNombre() + "',"
                    + tipo_menu.getActivo() + ",'"
                    + tipo_menu.getDescripcion() + "','"
                    + tipo_menu.getUsuario_m() + "','"
                    + db_formato_fecha_hora.format(tipo_menu.getFecha_hora()) + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_menu);
        } catch (Exception ex) {
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: crear()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: crear()" + " ERROR: " + ex.toString());
        } finally {
            try {
                if(conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch(Exception ex) {
                System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: crear()-Finally" + " ERROR: " + ex.toString());
            }
        }

        return resultado;
    }

    public String modificar(Long id_tipo_menu, String jsonString) {
        String resultado = "";

        Connection conn = null;

        try {
            Type ObjectType = new TypeToken<Entidades.Tipo_Menu>() {
            }.getType();
            Entidades.Tipo_Menu tipo_menu = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            tipo_menu.setId_tipo_menu(id_tipo_menu);

            tipo_menu.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "UPDATE TIPO_MENU SET "
                    + "NOMBRE='" + tipo_menu.getNombre() + "', "
                    + "ACTIVO=" + tipo_menu.getActivo() + ", "
                    + "DESCRIPCION='" + tipo_menu.getDescripcion() + "', "
                    + "USUARIO_M='" + tipo_menu.getUsuario_m() + "', "
                    + "FECHA_HORA='" + db_formato_fecha_hora.format(tipo_menu.getFecha_hora()) + "' "
                    + "WHERE "
                    + "ID_TIPO_MENU=" + id_tipo_menu;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_menu);
        } catch (Exception ex) {
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()" + " ERROR: " + ex.toString());
        } finally {
            try {
                if(conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch(Exception ex) {
                System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()-Finally" + " ERROR: " + ex.toString());
            }
        }

        return resultado;
    }

    public String eliminar(Long id_tipo_menu) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            Entidades.Tipo_Menu tipo_menu = new Entidades.Tipo_Menu();

            String sql = "SELECT "
                    + "A.ID_TIPO_MENU, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "TIPO_MENU A "
                    + "WHERE "
                    + "A.ID_TIPO_MENU=" + id_tipo_menu;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tipo_menu.setId_tipo_menu(rs.getLong(1));
                tipo_menu.setNombre(rs.getString(2));
                tipo_menu.setActivo(rs.getInt(3));
                tipo_menu.setDescripcion(rs.getString(4));
                tipo_menu.setUsuario_m(rs.getString(5));
                tipo_menu.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();

            sql = "DELETE FROM TIPO_MENU WHERE ID_TIPO_MENU=" + id_tipo_menu;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_menu);
        } catch (Exception ex) {
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: eliminar()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: eliminar()" + " ERROR: " + ex.toString());
        } finally {
            try {
                if(conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch(Exception ex) {
                System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: eliminar()-Finally" + " ERROR: " + ex.toString());
            }
        }

        return resultado;
    }

}
