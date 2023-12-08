package Control.felcr;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Control_Referencia implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Referencia() {

    }

    public Integer Carga_Referencia(String ambiente, Integer id_documento, String doco, String tipo_orden, String no_compania, String an8_e1, Connection conn, String tabla) {
        Integer resultado = 0;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            String cadenasql = "SELECT NVL(MAX(R.ID_REFERENCIA),0)+1 MAXIMO FROM REFERENCIA R";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_referencia = 0;
            while (rs.next()) {
                maximo_id_referencia = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            /* ============================================== DOCUMENTO DE REFERENCIA NC Y ND =============================== */
            cadenasql = "SELECT TRIM(F.SDVR01) FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F WHERE (F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) AND (F.SDDOCO=" + doco + ")";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String documento_interno = "";
            while (rs.next()) {
                documento_interno = rs.getString(1);
            }
            rs.close();
            stmt.close();
            try {
                Integer.valueOf(documento_interno);
            } catch (Exception ex) {
                documento_interno = "0";
            }

            cadenasql = "SELECT TRIM(C.PROCESS_RESULT) FROM CONVERT_DOCUMENT C WHERE C.PROCESADO='SI' AND C.NO_FACTURA=" + documento_interno;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String factura_referencia = "";
            while (rs.next()) {
                factura_referencia = rs.getString(1);
            }
            rs.close();
            stmt.close();
            if (factura_referencia.equals("") || factura_referencia.equals(" ")) {
                factura_referencia = "-";
            }

            cadenasql = "SELECT DISTINCT TO_CHAR(TO_DATE(TO_CHAR(F.SDIVD + 1900000,'9999999'),'YYYYDDD'),'yyyyMMdd') || '000000' FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F WHERE (F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) AND (F.SDDOCO=" + doco + ")";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String fecha_factura_ref = "";
            while (rs.next()) {
                fecha_factura_ref = rs.getString(1);
            }
            rs.close();
            stmt.close();
            if (fecha_factura_ref.equals("") || fecha_factura_ref.equals(" ")) {
                fecha_factura_ref = "-";
            }

            /* ============================================== TEXTO DE OBSERVACION ADJUNTA =============================== */
            String id_comentarios = doco + "|" + tipo_orden + "|" + no_compania;
            Connection conn_jde = ctrl_base_datos.obtener_conexion_jde_petroleos(ambiente);
            String comentarios = ctrl_base_datos.ObtenerString("SELECT NVL(REPLACE(SUBSTR(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(A.GDTXFT,32000,1)),CHR(0),''),160,(LENGTH(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(A.GDTXFT,32000,1)),CHR(0),''))-166)-2),'''',''),' ') TEXTO_1 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F00165 A WHERE A.GDTXKY = '" + id_comentarios + "'", conn_jde);
            if (conn_jde != null) {
                conn_jde.close();
            }
            if (comentarios != null) {
                comentarios = comentarios.replaceAll("\\\\par", "\n");

                comentarios = comentarios.replaceAll("\\\\e1", "á");
                comentarios = comentarios.replaceAll("\\\\e9", "é");
                comentarios = comentarios.replaceAll("\\\\ed", "í");
                comentarios = comentarios.replaceAll("\\\\f3", "ó");
                comentarios = comentarios.replaceAll("\\\\fa", "ú");

                comentarios = comentarios.replaceAll("\\\\c1", "Á");
                comentarios = comentarios.replaceAll("\\\\c9", "É");
                comentarios = comentarios.replaceAll("\\\\cd", "Í");
                comentarios = comentarios.replaceAll("\\\\d3", "Ó");
                comentarios = comentarios.replaceAll("\\\\da", "Ú");

                comentarios = comentarios.replaceAll("\n", "");
                comentarios = comentarios.replaceAll("\r", "");

                comentarios = comentarios.trim();
                if (comentarios.equals("") || comentarios.equals(" ")) {
                    comentarios = "-";
                }
            } else {
                comentarios = "-";
            }

            /* ============================================= TIPO DESPACHO =============================================== */
            cadenasql = "SELECT TRIM(F.SDSRP1) FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F WHERE (F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) AND (F.SDDOCO=" + doco + ")";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String segmento = "";
            while (rs.next()) {
                segmento = rs.getString(1);
            }
            rs.close();
            stmt.close();

            Integer tipo_despacho = 3;
            if (segmento == null) {
                if (an8_e1.trim().equals("850368") || an8_e1.trim().equals("850366")) {
                    tipo_despacho = 1;
                } else {
                    tipo_despacho = 3;
                }
                /*segmento = driver.getStringDB("SELECT TRIM(F.ABAC05) FROM " + driver.AmbienteEsquemaJde() + ".F0101@" + driver.AmbienteDBLinkJde() + " F WHERE F.ABAN8=" + an8_e1);
                if (segmento.equals("RET")) {
                    tipo_despacho = 1;
                } else {
                    tipo_despacho = 2;
                }*/
            } else {
                switch (segmento.trim()) {
                    case "015":
                        tipo_despacho = 1;
                        break;
                    case "014":
                        tipo_despacho = 2;
                        break;
                }
            }

            /* ============================================== INSERT INTO REFERENCIA ===================================== */
            cadenasql = "INSERT INTO REFERENCIA ( "
                    + "ID_REFERENCIA, "
                    + "ID_DOCUMENTO, "
                    + "ID_DOCUMENT_TYPE_REF, "
                    + "NO_DOCUMENTO_REF, "
                    + "ID_BATCH, "
                    + "FECHA_DOCUMENTO_REF, "
                    + "RAZONREF, "
                    + "ID_CODIGO_REF, "
                    + "COMENTARIO_ADJUNTO, "
                    + "TIPO_DESPACHO) VALUES ('"
                    + maximo_id_referencia + "','"
                    + id_documento + "','"
                    + "1" + "','"
                    + documento_interno + "','"
                    + factura_referencia + "','"
                    + fecha_factura_ref + "','"
                    + "Ref. Nota Crédito, Nota Dédito" + "','"
                    + "1" + "','"
                    + comentarios + "','"
                    + tipo_despacho + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_referencia;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }

    public String Modificar_Referencia(String ambiente, Integer id_referencia, String no_documento_ref, String id_batch, String comentario_adjunto, Integer id_codigo_ref, String tipo_nota_credito) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String cadenasql = "UPDATE REFERENCIA SET "
                    + "NO_DOCUMENTO_REF='" + no_documento_ref + "', "
                    + "ID_BATCH='" + id_batch + "', "
                    + "COMENTARIO_ADJUNTO='" + comentario_adjunto + "', "
                    + "ID_CODIGO_REF=" + id_codigo_ref + ", "
                    + "TIPO_NOTA_CREDITO='" + tipo_nota_credito + "' "
                    + "WHERE ID_REFERENCIA=" + id_referencia;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Referencia modificada.";

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

    public Integer Carga_Referencia_Refacturacion(String ambiente, Integer id_documento, String doco, String tipo_orden, String no_compania, String an8_e1, Connection conn, String tabla, Integer id_convert_document) {
        Integer resultado = 0;
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();

        try {
            String cadenasql = "SELECT NVL(MAX(R.ID_REFERENCIA),0)+1 MAXIMO FROM REFERENCIA R";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_referencia = 0;
            while (rs.next()) {
                maximo_id_referencia = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            /* ============================================== DOCUMENTO DE REFERENCIA NC Y ND =============================== */
            cadenasql = "SELECT C.NO_FACTURA FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String documento_interno = "";
            while (rs.next()) {
                documento_interno = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT C.PROCESS_RESULT FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String factura_referencia = "";
            while (rs.next()) {
                factura_referencia = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT C.FECHA_DOCUMENTO FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String fecha_factura_ref = "";
            while (rs.next()) {
                fecha_factura_ref = rs.getString(1);
            }
            rs.close();
            stmt.close();

            if (fecha_factura_ref.equals("") || fecha_factura_ref.equals(" ")) {
                fecha_factura_ref = "-";
            }

            /* ============================================== TEXTO DE OBSERVACION ADJUNTA =============================== */
            String id_comentarios = doco + "|" + tipo_orden + "|" + no_compania;
            Connection conn_jde = ctrl_base_datos.obtener_conexion_jde_petroleos(ambiente);
            String comentarios = ctrl_base_datos.ObtenerString("SELECT NVL(REPLACE(SUBSTR(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(A.GDTXFT,32000,1)),CHR(0),''),160,(LENGTH(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(A.GDTXFT,32000,1)),CHR(0),''))-166)-2),'''',''),' ') TEXTO_1 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F00165 A WHERE A.GDTXKY = '" + id_comentarios + "'", conn_jde);
            if (conn_jde != null) {
                conn_jde.close();
            }
            if (comentarios != null) {
                comentarios = comentarios.replaceAll("\\\\par", "\n");

                comentarios = comentarios.replaceAll("\\\\e1", "á");
                comentarios = comentarios.replaceAll("\\\\e9", "é");
                comentarios = comentarios.replaceAll("\\\\ed", "í");
                comentarios = comentarios.replaceAll("\\\\f3", "ó");
                comentarios = comentarios.replaceAll("\\\\fa", "ú");

                comentarios = comentarios.replaceAll("\\\\c1", "Á");
                comentarios = comentarios.replaceAll("\\\\c9", "É");
                comentarios = comentarios.replaceAll("\\\\cd", "Í");
                comentarios = comentarios.replaceAll("\\\\d3", "Ó");
                comentarios = comentarios.replaceAll("\\\\da", "Ú");

                comentarios = comentarios.replaceAll("\n", "");
                comentarios = comentarios.replaceAll("\r", "");

                comentarios = comentarios.trim();
                if (comentarios.equals("") || comentarios.equals(" ")) {
                    comentarios = "-";
                }
            } else {
                comentarios = "-";
            }

            /* ============================================= TIPO DESPACHO =============================================== */
            cadenasql = "SELECT TRIM(F.SDSRP1) FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " F WHERE (F.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) AND (F.SDDOCO=" + doco + ")";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            String segmento = "";
            while (rs.next()) {
                segmento = rs.getString(1);
            }
            rs.close();
            stmt.close();

            Integer tipo_despacho = 0;
            if (segmento == null) {
                if (an8_e1.trim().equals("850368") || an8_e1.trim().equals("850366")) {
                    tipo_despacho = 1;
                } else {
                    tipo_despacho = 3;
                }
                /*segmento = driver.getStringDB("SELECT TRIM(F.ABAC05) FROM " + driver.AmbienteEsquemaJde() + ".F0101@" + driver.AmbienteDBLinkJde() + " F WHERE F.ABAN8=" + an8_e1);
                if (segmento.equals("RET")) {
                    tipo_despacho = 1;
                } else {
                    tipo_despacho = 2;
                }*/
            } else {
                switch (segmento.trim()) {
                    case "015":
                        tipo_despacho = 1;
                        break;
                    case "014":
                        tipo_despacho = 2;
                        break;
                }
            }

            /* ============================================== INSERT INTO REFERENCIA ===================================== */
            cadenasql = "INSERT INTO REFERENCIA ( "
                    + "ID_REFERENCIA, "
                    + "ID_DOCUMENTO, "
                    + "ID_DOCUMENT_TYPE_REF, "
                    + "NO_DOCUMENTO_REF, "
                    + "ID_BATCH, "
                    + "FECHA_DOCUMENTO_REF, "
                    + "RAZONREF, "
                    + "ID_CODIGO_REF, "
                    + "COMENTARIO_ADJUNTO, "
                    + "TIPO_DESPACHO) VALUES ('"
                    + maximo_id_referencia + "','"
                    + id_documento + "','"
                    + "10" + "','"
                    + documento_interno + "','"
                    + factura_referencia + "','"
                    + fecha_factura_ref + "','"
                    + "Refacturación" + "','"
                    + "7" + "','"
                    + comentarios + "','"
                    + tipo_despacho + "')";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = maximo_id_referencia;

        } catch (Exception ex) {
            System.out.println("ERROR(CLASE: " + this.getClass().getName() + ") = " + ex.toString());
        }

        return resultado;
    }
    
}
