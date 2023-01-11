package Control;

import Entidad.Evento;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class Control_Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    public String buscar_eventos() {
        String resultado = "";
        List<Evento> lista_evento = new ArrayList<>();
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "select "
                    + "a.id_tipo_evento, "
                    + "a.id_usuario, "
                    + "a.fecha_hora, "
                    + "a.descripcion "
                    + "from "
                    + "evento a";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Evento evento = new Evento(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getString(4));
                lista_evento.add(evento);
            }
            rs.close();
            stmt.close();

            conn.commit();

            resultado = new Gson().toJson(lista_evento);
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_eventos ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_eventos ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String buscar_evento(Long id_tipo_evento, Long id_usuario, Long fecha_hora) {
        String resultado = "";
        List<Evento> lista_evento = new ArrayList<>();
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "select "
                    + "a.id_tipo_evento, "
                    + "a.id_usuario, "
                    + "a.fecha_hora, "
                    + "a.descripcion "
                    + "from "
                    + "evento a "
                    + "where "
                    + "a.id_tipo_evento=" + id_tipo_evento + " and "
                    + "a.id_usuario=" + id_usuario + " and "
                    + "a.fecha_hora=" + fecha_hora;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Evento evento = new Evento(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getLong(3),
                        rs.getString(4));
                lista_evento.add(evento);
            }
            rs.close();
            stmt.close();

            conn.commit();

            resultado = new Gson().toJson(lista_evento);
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_eventos ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_eventos ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: buscar_eventos - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String crear_evento(String jsonSring) {
        String resultado;
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Evento>>() {
            }.getType();
            List<Evento> lista_evento = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            for (Integer i = 0; i < lista_evento.size(); i++) {
                String cadenasql = "insert into evento ("
                        + "id_tipo_evento, "
                        + "id_usuario, "
                        + "fecha_hora, "
                        + "descripcion) values ("
                        + lista_evento.get(i).getId_tipo_evento() + ","
                        + lista_evento.get(i).getId_usuario() + ","
                        + lista_evento.get(i).getFecha_hora() + ",'"
                        + lista_evento.get(i).getDescripcion() + "')";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            conn.commit();

            resultado = "Evento creado en la aplicación.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: crear_evento ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: crear_evento ERROR: " + ex.toString();
            } catch (SQLException ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: crear_evento - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: crear_evento - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: crear_evento - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: crear_evento - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String modificar_evento(Long id_tipo_evento, Long id_usuario, Long fecha_hora, String jsonSring) {
        String resultado;
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Evento>>() {
            }.getType();
            List<Evento> lista_evento = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            for (Integer i = 0; i < lista_evento.size(); i++) {
                String cadenasql = "update evento set "
                        + "descripcion='" + lista_evento.get(i).getDescripcion() + "' "
                        + "where "
                        + "id_tipo_evento=" + id_tipo_evento + " and "
                        + "id_usuario=" + id_usuario + " and "
                        + "fecha_hora=" + fecha_hora;
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            conn.commit();

            resultado = "Evento modificado en la aplicación.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar_evento ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: modificar_evento ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar_evento - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: modificar_evento - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar_evento - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: modificar_evento - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String eliminar_evento(Long id_tipo_evento, Long id_usuario, Long fecha_hora) {
        String resultado;
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "delete from evento where id_tipo_evento=" + id_tipo_evento + " and id_usuario=" + id_usuario + " and fecha_hora=" + fecha_hora;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();

            resultado = "Evento eliminado en la aplicación.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_evento ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: eliminar_evento ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_evento - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: eliminar_evento - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_evento - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: eliminar_evento - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

}
