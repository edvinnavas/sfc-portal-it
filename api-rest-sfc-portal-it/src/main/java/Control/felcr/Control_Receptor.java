package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Receptor implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Receptor() {

    }

    public Integer Carga_Receptor(String ambiente, String id_tax, String an8, Connection conn) {
        Integer resultado = 0;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            id_tax = id_tax.trim().replaceAll("-", "");
            String cadenasql = "SELECT R.ID_RECEPTOR FROM RECEPTOR R WHERE R.NRODOCRECEP='" + id_tax + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer id_receptor = 0;
            while (rs.next()) {
                id_receptor = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            Integer id_tipo_contribuyente = 0;
            if (id_tax.length() == 10) {
                id_tipo_contribuyente = 2;
            } else {
                id_tipo_contribuyente = 1;
            }

            if (id_receptor > 0) {
                //NOMBRE DE RECEPTOR.
                cadenasql = "SELECT N.WWMLNM FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0111@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " N WHERE N.WWIDLN=0 AND N.WWAN8=" + an8;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String nombre_receptor = "";
                while (rs.next()) {
                    nombre_receptor = rs.getString(1);
                }
                rs.close();
                stmt.close();

                //CORREO DE RECEPTOR.
                cadenasql = "SELECT T.EAEMAL FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F01151@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.EAETP='E' AND T.EAAN8=" + an8 + " AND "
                        + "T.EAIDLN=(SELECT N.WWIDLN FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0111@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " N "
                        + "WHERE TRIM(N.WWMLNM)='Facturacion Electronica CR' AND N.WWAN8=" + an8 + ")";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String mail_receptor = "-";
                while (rs.next()) {
                    mail_receptor = rs.getString(1);
                }
                rs.close();
                stmt.close();

                //CODIGO PAIS Y TELEFONO DE RECEPTOR.
                cadenasql = "SELECT T.WPAR1, T.WPPH1 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0115@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.WPAN8=" + an8;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String codigo_pais = "-";
                String telefono = "-";
                while (rs.next()) {
                    codigo_pais = rs.getString(1);
                    telefono = rs.getString(2);
                }
                rs.close();
                stmt.close();

                if (codigo_pais.trim() == null) {
                    codigo_pais = "506";
                } else {
                    if (codigo_pais.trim().equals("")) {
                        codigo_pais = "506";
                    }
                }

                //DIRECCION DE RECEPTOR.
                cadenasql = "SELECT TRIM(D.ALADD1) || ' ' || TRIM(D.ALADD2) || ' ' || TRIM(D.ALADD3) || ' ' || TRIM(D.ALADD4) "
                        + "FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0116@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " D "
                        + "WHERE D.ALAN8=" + an8;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String direccion_receptor = "-";
                while (rs.next()) {
                    direccion_receptor = rs.getString(1);
                }
                rs.close();
                stmt.close();

                cadenasql = "UPDATE RECEPTOR SET "
                        + "ID_TIPO_CONTRIBUYENTE='" + id_tipo_contribuyente + "', "
                        + "NRODOCRECEP='" + id_tax.trim() + "', "
                        + "NMBRECEP='" + nombre_receptor.trim().replaceAll("'", "") + "', "
                        + "PRIMERNOMBRE='" + nombre_receptor.trim().replaceAll("'", "") + "', "
                        + "CDGINTRECEP='" + "-" + "', "
                        + "CALLE='" + direccion_receptor.trim().replaceAll("'", "") + "', "
                        + "DEPARTAMENTO='" + "0" + "', "
                        + "DISTRITO='" + "00" + "', "
                        + "CIUDAD='" + "00" + "', "
                        + "MUNICIPIO='" + "00" + "', "
                        + "EMAIL='" + mail_receptor.trim().replaceAll("'", "") + "', "
                        + "CODIGOPAIS='" + codigo_pais.trim().replaceAll("'", "") + "', "
                        + "TELEFONO='" + telefono.trim().replaceAll("'", "") + "', "
                        + "EXTENSION='" + "000" + "', "
                        + "FAX='" + "0000-0000" + "' "
                        + "WHERE ID_RECEPTOR=" + id_receptor;
                stmt = conn.createStatement();
                System.out.println("UPDATE RECEPTOR: " + cadenasql);
                stmt.executeUpdate(cadenasql);
                stmt.close();

                resultado = id_receptor;
            } else {
                //SIGUIENTE ID_RECEPTOR.
                cadenasql = "SELECT NVL(MAX(R.ID_RECEPTOR),0)+1 MAXIMO "
                        + "FROM RECEPTOR R";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                Integer maximo_receptor = 0;
                while (rs.next()) {
                    maximo_receptor = rs.getInt(1);
                }
                rs.close();
                stmt.close();

                //NOMBRE DE RECEPTOR.
                cadenasql = "SELECT N.WWMLNM FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0111@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " N WHERE N.WWIDLN=0 AND N.WWAN8=" + an8;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String nombre_receptor = "";
                while (rs.next()) {
                    nombre_receptor = rs.getString(1);
                }
                rs.close();
                stmt.close();

                //CORREO DE RECEPTOR.
                cadenasql = "SELECT T.EAEMAL FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F01151@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.EAETP='E' AND T.EAAN8=" + an8 + " AND "
                        + "T.EAIDLN=(SELECT N.WWIDLN FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0111@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " N "
                        + "WHERE TRIM(N.WWMLNM)='Facturacion Electronica CR' AND N.WWAN8=" + an8 + ")";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String mail_receptor = "-";
                while (rs.next()) {
                    mail_receptor = rs.getString(1);
                }
                rs.close();
                stmt.close();

                //CODIGO PAIS Y TELEFONO DE RECEPTOR.
                cadenasql = "SELECT T.WPAR1, T.WPPH1 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0115@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.WPAN8=" + an8;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String codigo_pais = "-";
                String telefono = "-";
                while (rs.next()) {
                    codigo_pais = rs.getString(1);
                    telefono = rs.getString(2);
                }
                rs.close();
                stmt.close();

                if (codigo_pais.trim() == null) {
                    codigo_pais = "506";
                } else {
                    if (codigo_pais.trim().equals("")) {
                        codigo_pais = "506";
                    }
                }

                //DIRECCION DE RECEPTOR.
                cadenasql = "SELECT TRIM(D.ALADD1) || ' ' || TRIM(D.ALADD2) || ' ' || TRIM(D.ALADD3) || ' ' || TRIM(D.ALADD4) "
                        + "FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0116@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " D "
                        + "WHERE D.ALAN8=" + an8;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                String direccion_receptor = "-";
                while (rs.next()) {
                    direccion_receptor = rs.getString(1);
                }
                rs.close();
                stmt.close();

                cadenasql = "INSERT INTO RECEPTOR ( "
                        + "ID_RECEPTOR, "
                        + "ID_TIPO_CONTRIBUYENTE, "
                        + "NRODOCRECEP, "
                        + "NMBRECEP, "
                        + "PRIMERNOMBRE, "
                        + "CDGINTRECEP, "
                        + "CALLE, "
                        + "DEPARTAMENTO, "
                        + "DISTRITO, "
                        + "CIUDAD, "
                        + "MUNICIPIO, "
                        + "EMAIL, "
                        + "CODIGOPAIS, "
                        + "TELEFONO, "
                        + "EXTENSION, "
                        + "FAX) VALUES ('"
                        + maximo_receptor + "','"
                        + id_tipo_contribuyente + "','"
                        + id_tax.trim() + "','"
                        + nombre_receptor.trim().replaceAll("'", "") + "','"
                        + nombre_receptor.trim().replaceAll("'", "") + "','"
                        + "-" + "','"
                        + direccion_receptor.trim().replaceAll("'", "") + "','"
                        + "0" + "','"
                        + "00" + "','"
                        + "00" + "','"
                        + "00" + "','"
                        + mail_receptor.trim().replaceAll("'", "") + "','"
                        + codigo_pais.trim().replaceAll("'", "") + "','"
                        + telefono.trim().replaceAll("'", "") + "','"
                        + "000" + "','"
                        + "0000-0000" + "')";
                stmt = conn.createStatement();
                System.out.println("INSERT RECEPTOR: " + cadenasql);
                stmt.executeUpdate(cadenasql);
                stmt.close();

                resultado = maximo_receptor;
            }

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

    public String Modificar_Receptor(String ambiente, Integer id_receptor, Integer id_tipo_contribuyente, String tax_id, String nombre_receptor, String direccion, String correo, String codigo_area) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String cadenasql = "UPDATE RECEPTOR SET "
                    + "ID_TIPO_CONTRIBUYENTE=" + id_tipo_contribuyente + ", "
                    + "NRODOCRECEP='" + tax_id + "', "
                    + "NMBRECEP='" + nombre_receptor + "', "
                    + "PRIMERNOMBRE='" + nombre_receptor + "', "
                    + "CALLE='" + direccion + "', "
                    + "EMAIL='" + correo + "', "
                    + "CODIGOPAIS='" + codigo_area + "' "
                    + "WHERE ID_RECEPTOR=" + id_receptor;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Receptor modificado.";

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
