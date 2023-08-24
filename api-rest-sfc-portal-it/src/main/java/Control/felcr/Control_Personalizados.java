package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Personalizados implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Personalizados() {

    }

    public Integer Carga_Personalizados(String ambiente, Integer id_dte, Integer id_documento, String numero_interno, String an8, String doco, Connection conn, String tabla) {
        Integer resultado = 0;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            String cadenasql = "SELECT NVL(MAX(I.ID_PERSONALIZADOS),0)+1 MAXIMO FROM PERSONALIZADOS I";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_personalizados = 0;
            while (rs.next()) {
                maximo_id_personalizados = rs.getInt(1);
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

            //MONTO FACTURA.
            cadenasql = "SELECT "
                    + "TABLA.NO_ORDEN, "
                    + "SUM(TABLA.PRECIO_EXTENDIDO) + SUM(TABLA.TOTAL_EXENTO) TOTAL_VENTA "
                    + "FROM "
                    + "(SELECT "
                    + "F.SDDOCO NO_ORDEN, "
                    + "F.SDDOC NO_DOCUMENTO, "
                    + "F.SDUORG CANTIDAD, "
                    + "F.SDUOM2 UNIDAD_MEDIDA, "
                    + "F.SDITM CODIGO_PRODUCTO, "
                    + "DECODE(TRIM(F.SDLNTY),'S','MERCANCIA','SERVICIO') TIPO_PRODUCTO, "
                    + "F.SDLITM NOMBRE_PRODUCTO, "
                    + "TRIM(F.SDDSC1) || ' ' || TRIM(F.SDDSC2) DESCRIPCION, "
                    + "ROUND(F.SDUPRC/10000.00,2) PRECIO_UNITARIO, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) PRECIO_EXTENDIDO, "
                    + "0.00 TOTAL_DESCUENTO, "
                    + "0.00 TOTAL_EXENTO, "
                    + "F.SDTXA1 TIPO_IMPUESTO, "
                    + "NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM), (SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0)) PORCENTAJE_IMPUESTO, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) * NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM), (SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0)) TOTAL_IMPUESTO, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) + (ROUND(F.SDUORG*F.SDUPRC/10000.00,2) * NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM), (SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))) TOTAL_VENTA "
                    + "FROM "
                    + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F "
                    + "WHERE "
                    + "F.SDDOCO = " + doco + " AND F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) TABLA "
                    + "GROUP BY "
                    + "TABLA.NO_ORDEN";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            Double vlrpagar = 0.00;
            while (rs.next()) {
                vlrpagar = rs.getDouble(2);
            }
            rs.close();
            stmt.close();

            String monto_factura_letras = vlrpagar.toString();

            cadenasql = "INSERT INTO PERSONALIZADOS ( "
                    + "ID_PERSONALIZADOS, "
                    + "ID_DTE, "
                    + "ID_DOCUMENTO, "
                    + "SISTEMAEMISOR, "
                    + "NUMEROINTERNO, "
                    + "DIRECCIONRECEPTOR, "
                    + "CORREOSRECEPTOR, "
                    + "IMPRIMIR, "
                    + "DISTRIBUIR, "
                    + "VLRPALABRAS, "
                    + "OBSERVACIONES) VALUES ('"
                    + maximo_id_personalizados + "','"
                    + id_dte + "','"
                    + id_documento + "','"
                    + "JDE" + "','"
                    + numero_interno.trim().replaceAll("'", "") + "','"
                    + direccion_receptor.trim().replaceAll("'", "") + "','"
                    + mail_receptor.trim().replaceAll("'", "") + "','"
                    + "1" + "','"
                    + "1" + "','"
                    + monto_factura_letras.trim().replaceAll("'", "") + "','"
                    + "-" + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_personalizados;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

}
