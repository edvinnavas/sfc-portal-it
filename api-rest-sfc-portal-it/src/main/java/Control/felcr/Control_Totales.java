package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Control_Totales implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Totales() {

    }

    public Integer Carga_Totales(String ambiente, String doco, String an8, Connection conn, String tabla) {
        Integer resultado = 0;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            String cadenasql = "SELECT NVL(MAX(T.ID_TOTALES),0)+1 MAXIMO FROM TOTALES T";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_totales = 0;
            while (rs.next()) {
                maximo_id_totales = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // cadenasql = "SELECT T.AICRCD FROM " + driver.AmbienteEsquemaJde() + ".F03012@" + driver.AmbienteDBLinkJde() + " T WHERE T.AIAN8=" + an8;
            cadenasql = "SELECT DISTINCT F.SDCRCD FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F WHERE F.SDDOCO=" + doco;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String moneda = "";
            while (rs.next()) {
                moneda = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT "
                    + "TABLA.NO_ORDEN, "
                    + "SUM(DECODE(TRIM(TABLA.MONEDA),'CRC',TABLA.PRECIO_EXTENDIDO,TABLA.PRECIO_EXTENDIDO_USD)) TOTAL_GRAVADO, "
                    + "SUM(TABLA.TOTAL_DESCUENTO) TOTAL_DESCUENTO, "
                    + "SUM(DECODE(TRIM(TABLA.MONEDA),'CRC',TABLA.PRECIO_EXTENDIDO,TABLA.PRECIO_EXTENDIDO_USD)) - SUM(TABLA.TOTAL_DESCUENTO) TOTAL_VENTA_NETA, "
                    + "SUM(TABLA.TOTAL_EXENTO) TOTAL_EXENTO, "
                    + "SUM(DECODE(TRIM(TABLA.MONEDA),'CRC',TABLA.TOTAL_IMPUESTO,TABLA.TOTAL_IMPUESTO_USD)) TOTAL_IMPUESTO, "
                    + "SUM(DECODE(TRIM(TABLA.MONEDA),'CRC',TABLA.PRECIO_EXTENDIDO,TABLA.PRECIO_EXTENDIDO_USD)) + SUM(TABLA.TOTAL_EXENTO) TOTAL_VENTA, "
                    + "SUM(DECODE(TRIM(TABLA.MONEDA),'CRC',TABLA.PRECIO_EXTENDIDO,TABLA.PRECIO_EXTENDIDO_USD)) - SUM(TABLA.TOTAL_DESCUENTO) + DECODE(TRIM(TABLA.MONEDA),'CRC',SUM(TABLA.TOTAL_IMPUESTO),SUM(TABLA.TOTAL_IMPUESTO_USD)) TOTAL_COMPROBANTE, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'SERVICIO',TABLA.PRECIO_EXTENDIDO,0.00)) TOTAL_SERVICIOS_GRAVADOS, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'SERVICIO',TABLA.TOTAL_EXENTO,0.00)) TOTAL_SERVICIOS_EXENTOS, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'MERCANCIA',TABLA.PRECIO_EXTENDIDO,0.00)) TOTAL_MERCANCIAS_GRAVADOS, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'MERCANCIA',TABLA.TOTAL_EXENTO,0.00)) TOTAL_MERCANCIAS_EXENTOS, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'SERVICIO',TABLA.PRECIO_EXTENDIDO_USD,0.00)) TOTAL_SERVICIOS_GRAVADOS_USD, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'SERVICIO',TABLA.TOTAL_EXENTO,0.00)) TOTAL_SERVICIOS_EXENTOS_USD, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'MERCANCIA',TABLA.PRECIO_EXTENDIDO_USD,0.00)) TOTAL_MERCANCIAS_GRAVADOS_USD, "
                    + "SUM(DECODE(TABLA.TIPO_PRODUCTO,'MERCANCIA',TABLA.TOTAL_EXENTO,0.00)) TOTAL_MERCANCIAS_EXENTOS_USD, "
                    + "TRIM(TABLA.MONEDA) MONEDA "
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
                    + "DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))*100,0) PORCENTAJE_IMPUESTO, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) * DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))*100,0) TOTAL_IMPUESTO, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) + ROUND(F.SDUORG*F.SDUPRC/10000.00,2) * DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))*100,0) TOTAL_VENTA, "
                    + "F.SDCRCD MONEDA, "
                    + "ROUND(F.SDFUP/10000.00,2) PRECIO_UNITARIO_USD, "
                    + "ROUND(F.SDUORG*F.SDFUP/10000.00,2) PRECIO_EXTENDIDO_USD, "
                    + "ROUND(F.SDUORG*F.SDFUP/10000.00,2) * DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))*100,0) TOTAL_IMPUESTO_USD, "
                    + "ROUND(F.SDUORG*F.SDFUP/10000.00,2) + ROUND(F.SDUORG*F.SDFUP/10000.00,2) * DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))*100,0) TOTAL_VENTA_USD "
                    + "FROM "
                    + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F "
                    + "WHERE "
                    + "F.SDDOCO = " + doco + " AND F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) TABLA "
                    + "GROUP BY "
                    + "TABLA.NO_ORDEN, TRIM(TABLA.MONEDA)";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            Double subtotal = 0.00;
            Double mntdcto = 0.00;
            Double mntbase = 0.00;
            Double mntexe = 0.00;
            Double mntimp = 0.00;
            Double saldoanterior = 0.00;
            Double vlrpagar = 0.00;
            Double montoconcepto1 = 0.00;
            Double montoconcepto2 = 0.00;
            Double montoconcepto3 = 0.00;
            Double montoconcepto4 = 0.00;
            while (rs.next()) {
                subtotal = rs.getDouble(2);
                mntdcto = rs.getDouble(3);
                mntbase = rs.getDouble(4);
                mntexe = rs.getDouble(5);
                mntimp = rs.getDouble(6);
                saldoanterior = rs.getDouble(7);
                vlrpagar = rs.getDouble(8);
                if (rs.getString(17).equals("USD")) {
                    montoconcepto1 = rs.getDouble(13);
                    montoconcepto2 = rs.getDouble(14);
                    montoconcepto3 = rs.getDouble(15);
                    montoconcepto4 = rs.getDouble(16);
                } else {
                    montoconcepto1 = rs.getDouble(9);
                    montoconcepto2 = rs.getDouble(10);
                    montoconcepto3 = rs.getDouble(11);
                    montoconcepto4 = rs.getDouble(12);
                }
            }
            rs.close();
            stmt.close();

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            cadenasql = "INSERT INTO TOTALES ( "
                    + "ID_TOTALES, "
                    + "MONEDA, "
                    + "FCTCONV, "
                    + "SUBTOTAL, "
                    + "MNTDCTO, "
                    + "MNTBASE, "
                    + "MNTEXE, "
                    + "MNTIMP, "
                    + "SALDOANTERIOR, "
                    + "VLRPAGAR, "
                    + "MONTOCONCEPTO1, "
                    + "MONTOCONCEPTO2, "
                    + "MONTOCONCEPTO3, "
                    + "MONTOCONCEPTO4) VALUES ('"
                    + maximo_id_totales + "','"
                    + moneda + "','"
                    + "1.00" + "','"
                    + decimalFormat.format(subtotal) + "','"
                    + decimalFormat.format(mntdcto) + "','"
                    + decimalFormat.format(mntbase) + "','"
                    + decimalFormat.format(mntexe) + "','"
                    + decimalFormat.format(mntimp) + "','"
                    + decimalFormat.format(saldoanterior) + "','"
                    + decimalFormat.format(vlrpagar) + "','"
                    + decimalFormat.format(montoconcepto1) + "','"
                    + decimalFormat.format(montoconcepto2) + "','"
                    + decimalFormat.format(montoconcepto3) + "','"
                    + decimalFormat.format(montoconcepto4) + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_totales;
        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

}
