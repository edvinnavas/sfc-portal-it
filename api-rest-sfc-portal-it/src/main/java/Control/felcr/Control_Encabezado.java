package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Encabezado implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Encabezado() {

    }

    public Integer Carga_Encabezado(Integer Id_Doc, Integer Id_Emisor, Integer Id_Receptor, Integer Id_Totales, Connection conn) {
        Integer resultado = 0;

        try {
            String cadenasql = "SELECT NVL(MAX(I.ID_ENCABEZADO),0)+1 MAXIMO FROM ENCABEZADO I";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_encabezado = 0;
            while (rs.next()) {
                maximo_id_encabezado = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "INSERT INTO ENCABEZADO ( "
                    + "ID_ENCABEZADO, "
                    + "ID_DOC, "
                    + "ID_EMISOR, "
                    + "ID_RECEPTOR, "
                    + "ID_TOTALES) VALUES ('"
                    + maximo_id_encabezado + "','"
                    + Id_Doc + "','"
                    + Id_Emisor + "','"
                    + Id_Receptor + "','"
                    + Id_Totales + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_encabezado;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

}
