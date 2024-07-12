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

public class Ctrl_Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Rol() {

    }

    public List<Entidades.Rol> obtener_lista(Connection conn) {
        List<Entidades.Rol> resultado = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "A.ID_ROL, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "ROL A";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entidades.Rol rol = new Entidades.Rol();
                rol.setId_rol(rs.getLong(1));
                rol.setNombre(rs.getString(2));
                rol.setActivo(rs.getInt(3));
                rol.setDescripcion(rs.getString(4));
                rol.setUsuario_m(rs.getString(5));
                rol.setFecha_hora(rs.getDate(6));
                resultado.add(rol);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Rol obtener_id(Connection conn, Long id_rol) {
        Entidades.Rol resultado = new Entidades.Rol();

        try {
            String sql = "SELECT "
                    + "A.ID_ROL, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "ROL A "
                    + "WHERE "
                    + "A.ID_ROL=" + id_rol;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_rol(rs.getLong(1));
                resultado.setNombre(rs.getString(2));
                resultado.setActivo(rs.getInt(3));
                resultado.setDescripcion(rs.getString(4));
                resultado.setUsuario_m(rs.getString(5));
                resultado.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_id()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Rol crear(Connection conn, String jsonString) {
        Entidades.Rol resultado = new Entidades.Rol();

        try {
            Type ObjectType = new TypeToken<Entidades.Rol>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();

            Long id_rol = ctrl_base_datos.ObtenerLong("SELECT IFNULL(MAX(A.ID_ROL),0) + 1 MAXIMO FROM ROL A", conn);
            resultado.setId_rol(id_rol);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "INSERT INTO ROL ("
                    + "ID_ROL,"
                    + "NOMBRE,"
                    + "ACTIVO,"
                    + "DESCRIPCION,"
                    + "USUARIO_M,"
                    + "FECHA_HORA) VALUES ("
                    + resultado.getId_rol() + ",'"
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

    public Entidades.Rol modificar(Connection conn, Long id_rol, String jsonString) {
        Entidades.Rol resultado = new Entidades.Rol();

        try {
            Type ObjectType = new TypeToken<Entidades.Rol>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            resultado.setId_rol(id_rol);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "UPDATE ROL SET "
                    + "NOMBRE='" + resultado.getNombre() + "', "
                    + "ACTIVO=" + resultado.getActivo() + ", "
                    + "DESCRIPCION='" + resultado.getDescripcion() + "', "
                    + "USUARIO_M='" + resultado.getUsuario_m() + "', "
                    + "FECHA_HORA='" + db_formato_fecha_hora.format(resultado.getFecha_hora()) + "' "
                    + "WHERE "
                    + "ID_ROL=" + id_rol;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Rol eliminar(Connection conn, Long id_rol) {
        Entidades.Rol resultado = new Entidades.Rol();

        try {
            String sql = "SELECT "
                    + "A.ID_ROL, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "ROL A "
                    + "WHERE "
                    + "A.ID_ROL=" + id_rol;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_rol(rs.getLong(1));
                resultado.setNombre(rs.getString(2));
                resultado.setActivo(rs.getInt(3));
                resultado.setDescripcion(rs.getString(4));
                resultado.setUsuario_m(rs.getString(5));
                resultado.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();

            sql = "DELETE FROM ROL WHERE ID_ROL=" + id_rol;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: eliminar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

}
