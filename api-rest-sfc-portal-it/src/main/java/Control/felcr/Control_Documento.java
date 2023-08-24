package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Documento() {

    }

    public Integer Carga_Documento(Integer Id_Encabezado, Integer Id_Cae, Connection conn) {
        Integer resultado = 0;

        try {
            String cadenasql = "SELECT NVL(MAX(I.ID_DOCUMENTO),0)+1 MAXIMO FROM DOCUMENTO I";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_documento = 0;
            while (rs.next()) {
                maximo_id_documento = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "INSERT INTO DOCUMENTO ( "
                    + "ID_DOCUMENTO, "
                    + "ID_ENCABEZADO, "
                    + "ID_CAE) VALUES ('"
                    + maximo_id_documento + "','"
                    + Id_Encabezado + "','"
                    + Id_Cae + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_documento;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

}
