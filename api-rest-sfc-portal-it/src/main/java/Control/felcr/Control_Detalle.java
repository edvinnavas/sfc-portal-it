package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Detalle implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Detalle() {

    }

    public Integer Carga_Detalle(String ambiente, Integer Id_Documento, String doco, Connection conn, String tabla) {
        Integer resultado = 0;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            String cadenasql = "SELECT NVL(MAX(I.ID_DETALLE),0)+1 MAXIMO FROM DETALLE I";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_detalle = 0;
            while (rs.next()) {
                maximo_id_detalle = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT "
                    + "F.SDDOCO NO_ORDEN, "
                    + "5 TIPO_CODIGO_PRODUCTO, "
                    + "F.SDLITM VALOR_CODIGO_PRODUCTO, "
                    + "TRIM(F.SDDSC1) || ' ' || TRIM(F.SDDSC2) DESCRIPCION, "
                    + "F.SDUORG CANTIDAD, "
                    + "'UM' UNIDAD_MEDIDA, "
                    + "F.SDUOM2 UNIDAD_MEDIDA_COMERCIAL, "
                    + "ROUND(F.SDUPRC/10000.00,2) PRECIO_UNITARIO, "
                    + "0.00 MONTO_DESCUENTO, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) MONTO_TOTAL, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) SUBTOTAL, "
                    + "ROUND(F.SDUORG*F.SDUPRC/10000.00,2) + (ROUND(F.SDUORG*F.SDUPRC/10000.00,2) * DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0)),0)) MONTO_TOTAL_LINEA, "
                    + "'NATURALEZA DESCUENTO' NATURALIZA_DESCUENTO, "
                    + "DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0))*100,0) IMPUESTO, "
                    + "DECODE(TRIM(F.SDTXA1),'CRZ','SI','NO') EXENTO, "
                    + "F.SDCRCD MONEDA, "
                    + "ROUND(F.SDFUP/10000.00,2) PRECIO_UNITARIO_USD, "
                    + "ROUND(F.SDUORG*F.SDFUP/10000.00,2) MONTO_TOTAL_USD, "
                    + "ROUND(F.SDUORG*F.SDFUP/10000.00,2) SUBTOTAL_USD, "
                    + "ROUND(F.SDUORG*F.SDFUP/10000.00,2) + (ROUND(F.SDUORG*F.SDFUP/10000.00,2) * DECODE(TRIM(F.SDTAX1),'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4008@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0)),0)) MONTO_TOTAL_LINEA_USD "
                    + "FROM "
                    + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F "
                    + "WHERE "
                    + "F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984) AND "
                    + "F.SDDOCO = " + doco;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            Integer tipo_codigo_producto = 0;
            String valor_codigo_producto = "";
            String descripcion = "";
            Double cantidad = 0.00;
            String unidad_medida = "";
            String unidad_medida_comercial = "";
            Double precio_unitario = 0.00;
            Double monto_descuento = 0.00;
            Double monto_total = 0.00;
            Double subtotal = 0.00;
            Double monto_total_linea = 0.00;
            String naturaleza_descuento = "";
            Integer impuesto = 0;
            String exento = "";
            String cabys = "";
            while (rs.next()) {
                tipo_codigo_producto = rs.getInt(2);
                valor_codigo_producto = rs.getString(3);
                descripcion = rs.getString(4);
                cantidad = rs.getDouble(5);
                unidad_medida = rs.getString(6);
                unidad_medida_comercial = rs.getString(7);
                monto_descuento = rs.getDouble(9);
                String moneda = rs.getString(16).trim();
                if (moneda.equals("USD")) {
                    precio_unitario = rs.getDouble(17);
                    monto_total = rs.getDouble(18);
                    subtotal = rs.getDouble(19);
                    monto_total_linea = rs.getDouble(20);
                } else {
                    precio_unitario = rs.getDouble(8);
                    monto_total = rs.getDouble(10);
                    subtotal = rs.getDouble(11);
                    monto_total_linea = rs.getDouble(12);
                }
                naturaleza_descuento = rs.getString(13);
                impuesto = rs.getInt(14);
                exento = rs.getString(15);

                cadenasql = "SELECT F.IMDSC1 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4101D@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F WHERE TRIM(F.IMSRTX)='CABYS' AND TRIM(F.IMLITM)='" + valor_codigo_producto.trim() + "'";
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery(cadenasql);
                while (rs2.next()) {
                    cabys = rs2.getString(1);
                }
                rs2.close();
                stmt2.close();

                cadenasql = "INSERT INTO DETALLE ( "
                        + "ID_DETALLE, "
                        + "ID_DOCUMENTO, "
                        + "ID_TIPO_CODIGO_PRODUCTO, "
                        + "VLRCODIGO, "
                        + "DSCITEM, "
                        + "QTYITEM, "
                        + "UNMDITEM, "
                        + "UNIDADMEDIDACOMERCIAL, "
                        + "PRCNETOITEM, "
                        + "MNTDSCTO, "
                        + "MONTOBRUTOITEM, "
                        + "MONTONETOITEM, "
                        + "MONTOTOTALITEM, "
                        + "NROCTAPREDIAL, "
                        + "IMPUESTO, "
                        + "EXENTO, "
                        + "CABYS) VALUES ('"
                        + maximo_id_detalle + "','"
                        + Id_Documento + "','"
                        + tipo_codigo_producto + "','"
                        + valor_codigo_producto.trim() + "','"
                        + descripcion.trim() + "','"
                        + cantidad + "','"
                        + unidad_medida.trim() + "','"
                        + unidad_medida_comercial.trim() + "','"
                        + precio_unitario + "','"
                        + monto_descuento + "','"
                        + monto_total + "','"
                        + subtotal + "','"
                        + monto_total_linea + "','"
                        + naturaleza_descuento.trim() + "','"
                        + impuesto + "','"
                        + exento + "','"
                        + cabys.trim() + "')";
                Statement stmt1 = conn.createStatement();
                stmt1.executeUpdate(cadenasql);
                stmt1.close();

                maximo_id_detalle++;
            }
            rs.close();
            stmt.close();

            resultado = maximo_id_detalle;
        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

}
