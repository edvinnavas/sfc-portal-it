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

public class Ctrl_Tipo_Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Tipo_Evento() {

    }

    public String obtener_lista() {
        String resultado = null;

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            List<Entidades.Tipo_Evento> lista_tipo_evento = new ArrayList<>();

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

                lista_tipo_evento.add(tipo_evento);
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(lista_tipo_evento);
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

    public String obtener_id(Long id_tipo_evento) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            Entidades.Tipo_Evento tipo_evento = new Entidades.Tipo_Evento();

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
                tipo_evento.setId_tipo_evento(rs.getLong(1));
                tipo_evento.setNombre(rs.getString(2));
                tipo_evento.setActivo(rs.getInt(3));
                tipo_evento.setDescripcion(rs.getString(4));
                tipo_evento.setUsuario_m(rs.getString(5));
                tipo_evento.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_evento);
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
            Type ObjectType = new TypeToken<Entidades.Tipo_Evento>() {
            }.getType();
            Entidades.Tipo_Evento tipo_evento = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            Long id_tipo_evento = ctrl_base_datos.ObtenerLong("SELECT IFNULL(MAX(A.ID_TIPO_EVENTO),0) + 1 MAXIMO FROM TIPO_EVENTO A", conn);
            tipo_evento.setId_tipo_evento(id_tipo_evento);

            tipo_evento.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "INSERT INTO TIPO_EVENTO ("
                    + "ID_TIPO_EVENTO,"
                    + "NOMBRE,"
                    + "ACTIVO,"
                    + "DESCRIPCION,"
                    + "USUARIO_M,"
                    + "FECHA_HORA) VALUES ("
                    + tipo_evento.getId_tipo_evento() + ",'"
                    + tipo_evento.getNombre() + "',"
                    + tipo_evento.getActivo() + ",'"
                    + tipo_evento.getDescripcion() + "','"
                    + tipo_evento.getUsuario_m() + "','"
                    + db_formato_fecha_hora.format(tipo_evento.getFecha_hora()) + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_evento);
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

    public String modificar(Long id_tipo_evento, String jsonString) {
        String resultado = "";

        Connection conn = null;

        try {
            Type ObjectType = new TypeToken<Entidades.Tipo_Evento>() {
            }.getType();
            Entidades.Tipo_Evento tipo_evento = new Gson().fromJson(jsonString, ObjectType);

            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            tipo_evento.setId_tipo_evento(id_tipo_evento);

            tipo_evento.setFecha_hora(new Date());

            SimpleDateFormat db_formato_fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "UPDATE TIPO_EVENTO SET "
                    + "NOMBRE='" + tipo_evento.getNombre() + "', "
                    + "ACTIVO=" + tipo_evento.getActivo() + ", "
                    + "DESCRIPCION='" + tipo_evento.getDescripcion() + "', "
                    + "USUARIO_M='" + tipo_evento.getUsuario_m() + "', "
                    + "FECHA_HORA='" + db_formato_fecha_hora.format(tipo_evento.getFecha_hora()) + "' "
                    + "WHERE "
                    + "ID_TIPO_EVENTO=" + id_tipo_evento;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_evento);
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

    public String eliminar(Long id_tipo_evento) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            Entidades.Tipo_Evento tipo_evento = new Entidades.Tipo_Evento();

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
                tipo_evento.setId_tipo_evento(rs.getLong(1));
                tipo_evento.setNombre(rs.getString(2));
                tipo_evento.setActivo(rs.getInt(3));
                tipo_evento.setDescripcion(rs.getString(4));
                tipo_evento.setUsuario_m(rs.getString(5));
                tipo_evento.setFecha_hora(rs.getDate(6));
            }
            rs.close();
            stmt.close();

            sql = "DELETE FROM TIPO_EVENTO WHERE ID_TIPO_EVENTO=" + id_tipo_evento;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(tipo_evento);
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
