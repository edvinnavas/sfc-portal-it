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
import com.google.gson.reflect.TypeToken;

public class Ctrl_Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Menu() {

    }

    public List<Entidades.Menu> obtener_lista(Connection conn) {
        List<Entidades.Menu> resultado = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "A.ID_MENU, "
                    + "A.ID_TIPO_MENU, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "MENU A";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entidades.Menu menu = new Entidades.Menu();
                menu.setId_menu(rs.getLong(1));
                menu.setTipo_menu(new Ctrl_Tipo_Menu().obtener_id(conn, rs.getLong(2)));
                menu.setNombre(rs.getString(3));
                menu.setActivo(rs.getInt(4));
                menu.setDescripcion(rs.getString(5));
                menu.setUsuario_m(rs.getString(6));
                menu.setFecha_hora(rs.getDate(7));
                resultado.add(menu);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Menu obtener_id(Connection conn, Long id_menu) {
        Entidades.Menu resultado = new Entidades.Menu();

        try {
            String sql = "SELECT "
                    + "A.ID_MENU, "
                    + "A.ID_TIPO_MENU, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "MENU A "
                    + "WHERE A.ID_MENU=" + id_menu;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_menu(rs.getLong(1));
                resultado.setTipo_menu(new Ctrl_Tipo_Menu().obtener_id(conn, rs.getLong(2)));
                resultado.setNombre(rs.getString(3));
                resultado.setActivo(rs.getInt(4));
                resultado.setDescripcion(rs.getString(5));
                resultado.setUsuario_m(rs.getString(6));
                resultado.setFecha_hora(rs.getDate(7));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Menu crear(Connection conn, String jsonString) {
        Entidades.Menu resultado = new Entidades.Menu();

        try {
            Type ObjectType = new TypeToken<Entidades.Menu>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();

            Long id_menu = ctrl_base_datos.ObtenerLong("SELECT IFNULL(MAX(A.ID_MENU),0) + 1 MAXIMO FROM MENU A", conn);
            resultado.setId_menu(id_menu);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "INSERT INTO MENU ("
                    + "ID_MENU,"
                    + "ID_TIPO_MENU,"
                    + "NOMBRE,"
                    + "ACTIVO,"
                    + "DESCRIPCION,"
                    + "USUARIO_M,"
                    + "FECHA_HORA) VALUES ("
                    + resultado.getId_menu() + ","
                    + resultado.getTipo_menu().getId_tipo_menu() + ",'"
                    + resultado.getNombre() + "',"
                    + resultado.getActivo() + ",'"
                    + resultado.getDescripcion() + "','"
                    + resultado.getUsuario_m() + "','"
                    + db_formato_fecha_hora.format(resultado.getFecha_hora()) + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: crear()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Menu modificar(Connection conn, Long id_menu, String jsonString) {
        Entidades.Menu resultado = new Entidades.Menu();

        try {
            Type ObjectType = new TypeToken<Entidades.Menu>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            resultado.setId_menu(id_menu);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "UPDATE MENU SET "
                    + "ID_TIPO_MENU=" + resultado.getTipo_menu().getId_tipo_menu() + ", "
                    + "NOMBRE='" + resultado.getNombre() + "', "
                    + "ACTIVO=" + resultado.getActivo() + ", "
                    + "DESCRIPCION='" + resultado.getDescripcion() + "', "
                    + "USUARIO_M='" + resultado.getUsuario_m() + "', "
                    + "FECHA_HORA='" + db_formato_fecha_hora.format(resultado.getFecha_hora()) + "' "
                    + "WHERE "
                    + "ID_MENU=" + id_menu;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Menu eliminar(Connection conn, Long id_menu) {
        Entidades.Menu resultado = new Entidades.Menu();

        try {
            String sql = "SELECT "
                    + "A.ID_MENU, "
                    + "A.ID_TIPO_MENU, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "MENU A "
                    + "WHERE A.ID_MENU=" + id_menu;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_menu(rs.getLong(1));
                resultado.setTipo_menu(new Ctrl_Tipo_Menu().obtener_id(conn, rs.getLong(2)));
                resultado.setNombre(rs.getString(3));
                resultado.setActivo(rs.getInt(4));
                resultado.setDescripcion(rs.getString(5));
                resultado.setUsuario_m(rs.getString(6));
                resultado.setFecha_hora(rs.getDate(7));
            }
            rs.close();
            stmt.close();

            sql = "DELETE FROM MENU WHERE ID_MENU=" + id_menu;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: eliminar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

}
