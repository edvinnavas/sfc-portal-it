package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Otros_Cargos implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Otros_Cargos() {

    }

    public Integer Carga_Otros_Cargos(Integer id_documento, String activo, Integer id_tipo_otros_cargos, String numero_identificacion, String razon_social, String descripcion, Double porcentaje_otros_cargos, Double monto_otros_cargos, Connection conn) {
        Integer resultado = 0;

        try {
            String cadenasql = "SELECT NVL(MAX(E.ID_OTROS_CARGOS),0)+1 MAXIMO FROM OTROS_CARGOS E";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_otros_cargos = 0;
            while (rs.next()) {
                maximo_id_otros_cargos = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            /* ============================================== INSERT INTO OTROS CARGOS ===================================== */
            cadenasql = "INSERT INTO OTROS_CARGOS ( "
                    + "ID_OTROS_CARGOS, "
                    + "ID_DOCUMENTO, "
                    + "ACTIVO, "
                    + "ID_TIPO_OTROS_CARGOS, "
                    + "NUMERO_IDENTIFICACION, "
                    + "RAZON_SOCIAL, "
                    + "DESCRIPCION, "
                    + "PORCENTAJE_OTROS_CARGOS, "
                    + "MONTO_OTROS_CARGOS) VALUES ("
                    + maximo_id_otros_cargos + ","
                    + id_documento + ",'"
                    + activo + "',"
                    + id_tipo_otros_cargos + ",'"
                    + numero_identificacion + "','"
                    + razon_social + "','"
                    + descripcion + "',"
                    + porcentaje_otros_cargos + ","
                    + monto_otros_cargos + ")";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_otros_cargos;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

    public String Modificar_Otros_Cargos(String ambiente, Integer id_otros_cargos, Integer id_documento, String activo, Integer id_tipo_otros_cargos, String numero_identificacion, String razon_social, String descripcion, Double porcentaje_otros_cargos, Double monto_otros_cargos) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String cadenasql = "UPDATE OTROS_CARGOS SET "
                    + "ACTIVO='" + activo + "', "
                    + "ID_TIPO_OTROS_CARGOS=" + id_tipo_otros_cargos + ", "
                    + "NUMERO_IDENTIFICACION='" + numero_identificacion + "', "
                    + "RAZON_SOCIAL='" + razon_social + "', "
                    + "DESCRIPCION='" + descripcion + "', "
                    + "PORCENTAJE_OTROS_CARGOS=" + porcentaje_otros_cargos + ", "
                    + "MONTO_OTROS_CARGOS=" + monto_otros_cargos + " "
                    + "WHERE ID_OTROS_CARGOS=" + id_otros_cargos + " AND ID_DOCUMENTO=" + id_documento;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Otros cargos modificado.";

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
