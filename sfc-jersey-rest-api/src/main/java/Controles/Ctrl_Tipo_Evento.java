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

public class Ctrl_Tipo_Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Tipo_Evento() {

    }

    public List<Entidades.Tipo_Evento> obtener_lista(Connection conn) {
        List<Entidades.Tipo_Evento> resultado = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "A.ID_TIPO_EVENTO, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "TIPO_EVENTO A";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entidades.Tipo_Evento tipo_evento = new Entidades.Tipo_Evento();
                tipo_evento.setId_tipo_evento(rs.getLong(1));
                tipo_evento.setNombre(rs.getString(2));
                tipo_evento.setActivo(rs.getInt(3));
                tipo_evento.setDescripcion(rs.getString(4));
                tipo_evento.setUsuario_m(rs.getString(5));
                tipo_evento.setFecha_hora(rs.getDate(6));
                resultado.add(tipo_evento);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Tipo_Evento obtener_id(Connection conn, Long id_tipo_evento) {
        Entidades.Tipo_Evento resultado = new Entidades.Tipo_Evento();

        try {
            String sql = "SELECT "
                    + "A.ID_TIPO_EVENTO, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "TIPO_EVENTO A "
                    + "WHERE "
                    + "A.ID_TIPO_EVENTO=" + id_tipo_evento;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_tipo_evento(rs.getLong(1));
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

    public Entidades.Tipo_Evento crear(Connection conn, String jsonString) {
        Entidades.Tipo_Evento resultado = new Entidades.Tipo_Evento();

        try {
            Type ObjectType = new TypeToken<Entidades.Tipo_Evento>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();

            Long id_tipo_evento = ctrl_base_datos
                    .ObtenerLong("SELECT IFNULL(MAX(A.ID_TIPO_EVENTO),0) + 1 MAXIMO FROM TIPO_EVENTO A", conn);
            resultado.setId_tipo_evento(id_tipo_evento);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "INSERT INTO TIPO_EVENTO ("
                    + "ID_TIPO_EVENTO,"
                    + "NOMBRE,"
                    + "ACTIVO,"
                    + "DESCRIPCION,"
                    + "USUARIO_M,"
                    + "FECHA_HORA) VALUES ("
                    + resultado.getId_tipo_evento() + ",'"
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

    public Entidades.Tipo_Evento modificar(Connection conn, Long id_tipo_evento, String jsonString) {
        Entidades.Tipo_Evento resultado = new Entidades.Tipo_Evento();

        try {
            Type ObjectType = new TypeToken<Entidades.Tipo_Evento>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            resultado.setId_tipo_evento(id_tipo_evento);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "UPDATE TIPO_EVENTO SET "
                    + "NOMBRE='" + resultado.getNombre() + "', "
                    + "ACTIVO=" + resultado.getActivo() + ", "
                    + "DESCRIPCION='" + resultado.getDescripcion() + "', "
                    + "USUARIO_M='" + resultado.getUsuario_m() + "', "
                    + "FECHA_HORA='" + db_formato_fecha_hora.format(resultado.getFecha_hora()) + "' "
                    + "WHERE "
                    + "ID_TIPO_EVENTO=" + id_tipo_evento;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Tipo_Evento eliminar(Connection conn, Long id_tipo_evento) {
        Entidades.Tipo_Evento resultado = new Entidades.Tipo_Evento();

        try {
            String sql = "SELECT "
                    + "A.ID_TIPO_EVENTO, "
                    + "A.NOMBRE, "
                    + "A.ACTIVO, "
                    + "A.DESCRIPCION, "
                    + "A.USUARIO_M, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "TIPO_EVENTO A "
                    + "WHERE "
                    + "A.ID_TIPO_EVENTO=" + id_tipo_evento;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_tipo_evento(rs.getLong(1));
                resultado.setNombre(rs.getString(2));
                resultado.setActivo(rs.getInt(3));
                resultado.setDescripcion(rs.getString(4));
                resultado.setUsuario_m(rs.getString(5));
                resultado.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();

            sql = "DELETE FROM TIPO_EVENTO WHERE ID_TIPO_EVENTO=" + id_tipo_evento;
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
