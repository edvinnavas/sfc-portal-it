package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_I_Doc implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_I_Doc() {

    }

    public Integer Carga_I_Doc(String ambiente, Integer Id_document_type, String doc, Integer id_ambiente, String contenido_tc, String doco, Connection conn, String tabla) {
        Integer resultado = -1;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            String cadenasql = "SELECT NVL(MAX(I.ID_DOC),0)+1 MAXIMO FROM ID_DOC I";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_i_doc = 0;
            while (rs.next()) {
                maximo_i_doc = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT TO_CHAR(CURRENT_DATE,'YYYY-MM-DD') || 'T' || TO_CHAR(CURRENT_DATE,'HH24:MM:SS') IdDoc_FechaEmis FROM DUAL";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String fecha_emis = "";
            while (rs.next()) {
                fecha_emis = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT A.SDPTC FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " A WHERE A.SDDOCO=" + doco;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String credito_dias = "";
            while (rs.next()) {
                credito_dias = rs.getString(1);
            }
            rs.close();
            stmt.close();

            Integer dias_credito;
            try {
                dias_credito = Integer.valueOf(credito_dias.trim());
            } catch (Exception ex) {
                dias_credito = 0;
            }

            Integer id_condicion_pago = 0;
            if (dias_credito > 1) {
                id_condicion_pago = 2;
            } else {
                id_condicion_pago = 1;
            }

            cadenasql = "INSERT INTO ID_DOC ( "
                    + "ID_DOC, "
                    + "ID_AMBIENTE, "
                    + "CONTENIDOTC, "
                    + "ID_DOCUMENT_TYPE, "
                    + "NUMERO, "
                    + "NUMERO_INTERNO, "
                    + "FECHAEMIS, "
                    + "ESTABLECIMIENTO, "
                    + "ID_CONDICION_PAGO, "
                    + "TERMINOPAGOCDG) VALUES ('"
                    + maximo_i_doc + "','"
                    + id_ambiente + "','"
                    + contenido_tc + "','"
                    + Id_document_type + "','"
                    + contenido_tc.substring(31, 41) + "','"
                    + doc + "','"
                    + fecha_emis + "','"
                    + contenido_tc.substring(21, 41) + "','"
                    + id_condicion_pago + "','"
                    + dias_credito + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_i_doc;
        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
            resultado = -1;
        }

        return resultado;
    }

}
