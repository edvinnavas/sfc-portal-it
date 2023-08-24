package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;

public class Control_Id_Doc_Medio_Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Id_Doc_Medio_Pago() {

    }

    public void Carga_Id_Doc_Medio_Pago(Integer Id_Doc, Integer Id_Medio_Pago, Connection conn) {
        try {
            String cadenasql = "INSERT INTO ID_DOC_MEDIO_PAGO ( "
                    + "ID_DOC, "
                    + "ID_MEDIO_PAGO) VALUES ('"
                    + Id_Doc + "','"
                    + Id_Medio_Pago + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();
        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }
    }

}
