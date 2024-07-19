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

public class Ctrl_Log_Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Log_Evento() {

    }

    public List<Entidades.Log_Evento> obtener_lista(Connection conn) {
        List<Entidades.Log_Evento> resultado = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "A.ID_EVENTO, "
                    + "A.ID_TIPO_EVENTO, "
                    + "A.ID_USUARIO, "
                    + "A.DESCRIPCION, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "LOG_EVENTO A";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entidades.Log_Evento log_evento = new Entidades.Log_Evento();
                log_evento.setId_evento(rs.getLong(1));
                log_evento.setTipo_evento(new Ctrl_Tipo_Evento().obtener_id(conn, rs.getLong(2)));
                log_evento.setUsuario(new Ctrl_Usuario().obtener_id(conn, rs.getLong(3)));
                log_evento.setDescripcion(rs.getString(4));
                log_evento.setFecha_hora(rs.getDate(5));
                resultado.add(log_evento);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Log_Evento obtener_id(Connection conn, Long id_evento) {
        Entidades.Log_Evento resultado = new Entidades.Log_Evento();

        try {
            String sql = "SELECT "
                    + "A.ID_EVENTO, "
                    + "A.ID_TIPO_EVENTO, "
                    + "A.ID_USUARIO, "
                    + "A.DESCRIPCION, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "LOG_EVENTO A "
                    + "WHERE "
                    + "A.ID_EVENTO=" + id_evento;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_evento(rs.getLong(1));
                resultado.setTipo_evento(new Ctrl_Tipo_Evento().obtener_id(conn, rs.getLong(2)));
                resultado.setUsuario(new Ctrl_Usuario().obtener_id(conn, rs.getLong(3)));
                resultado.setDescripcion(rs.getString(4));
                resultado.setFecha_hora(rs.getDate(5));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Log_Evento crear(Connection conn, String jsonString) {
        Entidades.Log_Evento resultado = new Entidades.Log_Evento();

        try {
            Type ObjectType = new TypeToken<Entidades.Log_Evento>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();

            Long id_evento = ctrl_base_datos.ObtenerLong("SELECT IFNULL(MAX(A.ID_EVENTO),0) + 1 MAXIMO FROM LOG_EVENTO A", conn);
            resultado.setId_evento(id_evento);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "INSERT INTO LOG_EVENTO ("
                    + "ID_EVENTO,"
                    + "ID_TIPO_EVENTO,"
                    + "ID_USUARIO,"
                    + "DESCRIPCION,"
                    + "FECHA_HORA) VALUES ("
                    + resultado.getId_evento() + ","
                    + resultado.getTipo_evento().getId_tipo_evento() + ","
                    + resultado.getUsuario().getId_usuario() + ",'"
                    + resultado.getDescripcion() + "','"
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

    public Entidades.Log_Evento modificar(Connection conn, Long id_evento, String jsonString) {
        Entidades.Log_Evento resultado = new Entidades.Log_Evento();

        try {
            Type ObjectType = new TypeToken<Entidades.Log_Evento>() {
            }.getType();
            resultado = new Gson().fromJson(jsonString, ObjectType);

            resultado.setId_evento(id_evento);

            resultado.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "UPDATE LOG_EVENTO SET "
                    + "ID_TIPO_EVENTO=" + resultado.getTipo_evento().getId_tipo_evento() + ", "
                    + "ID_USUARIO=" + resultado.getUsuario().getId_usuario() + ", "
                    + "DESCRIPCION='" + resultado.getDescripcion() + "', "
                    + "FECHA_HORA='" + db_formato_fecha_hora.format(resultado.getFecha_hora()) + "' "
                    + "WHERE "
                    + "ID_EVENTO=" + id_evento;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: modificar()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public Entidades.Log_Evento eliminar(Connection conn, Long id_evento) {
        Entidades.Log_Evento resultado = new Entidades.Log_Evento();

        try {
            String sql = "SELECT "
                    + "A.ID_EVENTO, "
                    + "A.ID_TIPO_EVENTO, "
                    + "A.ID_USUARIO, "
                    + "A.DESCRIPCION, "
                    + "A.FECHA_HORA "
                    + "FROM "
                    + "LOG_EVENTO A "
                    + "WHERE "
                    + "A.ID_EVENTO=" + id_evento;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resultado.setId_evento(rs.getLong(1));
                resultado.setTipo_evento(new Ctrl_Tipo_Evento().obtener_id(conn, rs.getLong(2)));
                resultado.setUsuario(new Ctrl_Usuario().obtener_id(conn, rs.getLong(3)));
                resultado.setDescripcion(rs.getString(4));
                resultado.setFecha_hora(rs.getDate(5));
            }
            rs.close();
            stmt.close();

            sql = "DELETE FROM LOG_EVENTO WHERE ID_EVENTO=" + id_evento;
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
