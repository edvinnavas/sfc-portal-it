package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Dte implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Dte() {

    }

    public Integer Carga_Dte(Integer Id_Documento, Connection conn) {
        Integer resultado = 0;

        try {
            String cadenasql = "SELECT NVL(MAX(I.ID_DTE),0)+1 MAXIMO FROM DTE I";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_dte = 0;
            while (rs.next()) {
                maximo_id_dte = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "INSERT INTO DTE ( "
                    + "ID_DTE, "
                    + "ID_DOCUMENTO) VALUES ('"
                    + maximo_id_dte + "','"
                    + Id_Documento + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_dte;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

}
