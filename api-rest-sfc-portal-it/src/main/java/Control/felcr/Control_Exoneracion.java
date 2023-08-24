package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Exoneracion implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Exoneracion() {

    }

    public Integer Carga_Exoneracion(Integer id_documento, String activo, Integer id_tipo_exoneracion, String num_doc, String fecha_emision, String nombre_institucion, Double porcentaje_exoneracion, Double monto_exoneracion, Connection conn) {
        Integer resultado = 0;

        try {
            String cadenasql = "SELECT NVL(MAX(E.ID_EXONERACION),0)+1 MAXIMO FROM EXONERACION E";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_exoneracion = 0;
            while (rs.next()) {
                maximo_id_exoneracion = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            /* ============================================== INSERT INTO EXONERACION ===================================== */
            cadenasql = "INSERT INTO EXONERACION ( "
                    + "ID_EXONERACION, "
                    + "ID_DOCUMENTO, "
                    + "ACTIVO, "
                    + "ID_TIPO_EXONERACION, "
                    + "NUM_DOC, "
                    + "FECHA_EMISION, "
                    + "NOMBRE_INSTITUCION, "
                    + "PORCENTAJE_EXONERACION, "
                    + "MONTO_EXONERACION) VALUES ("
                    + maximo_id_exoneracion + ","
                    + id_documento + ",'"
                    + activo + "',"
                    + id_tipo_exoneracion + ",'"
                    + num_doc + "',"
                    + fecha_emision + ",'"
                    + nombre_institucion + "',"
                    + porcentaje_exoneracion + ","
                    + monto_exoneracion + ")";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_exoneracion;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

    public String Modificar_Exoneracion(String ambiente, Integer id_exoneracion, Integer id_documento, String activo, Integer id_tipo_exoneracion, String num_doc, String fecha_emision, String nombre_institucion, Double porcentaje_exoneracion, Double monto_exoneracion) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String cadenasql = "UPDATE EXONERACION SET "
                    + "ACTIVO='" + activo + "', "
                    + "ID_TIPO_EXONERACION=" + id_tipo_exoneracion + ", "
                    + "NUM_DOC='" + num_doc + "', "
                    + "FECHA_EMISION=" + fecha_emision + ", "
                    + "NOMBRE_INSTITUCION='" + nombre_institucion + "', "
                    + "PORCENTAJE_EXONERACION=" + porcentaje_exoneracion + ", "
                    + "MONTO_EXONERACION=" + monto_exoneracion + " "
                    + "WHERE ID_EXONERACION=" + id_exoneracion + " AND ID_DOCUMENTO=" + id_documento;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Exoneración modificada.";

        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "1," + ex.toString();
                }
            } catch (Exception ex1) {
                System.out.println(ex1.toString());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = ex.toString();
            }
        }

        return resultado;
    }

}
