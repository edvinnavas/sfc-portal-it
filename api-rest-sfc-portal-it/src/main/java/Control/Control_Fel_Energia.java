package Control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.DTE_FEL;
import Entidad.DTE_FEL_ASOCIADOS;
import Entidad.DTE_FEL_DETALLE;
import Entidad.DTE_FEL_ENCABEZADO;
import Entidad.DTE_FEL_TOTALES;
import Entidad.Dte_Lista;
import Entidad.FEL_DETALLE_DTE;
import Entidad.FEL_DTE;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Control_Fel_Energia implements Serializable {

    private static final long serialVersionUID = 1L;

    List<FEL_DETALLE_DTE> lista_detalle_fel_global = new ArrayList<>();

    public String Cargar_Doc_Fel(String ambiente, String compania, Long fecha1, Long fecha2) {
        String resultado = "";
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_fel_energia(ambiente);

            conn.setAutoCommit(false);

            String cadenasql = "SELECT "
                    + "F.AMBIENTE, "
                    + "F.USUARIO, "
                    + "F.CONTRASENA "
                    + "FROM "
                    + "FEL_AMBIENTE F "
                    + "WHERE "
                    + "F.ACTIVO = 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                ambiente = rs.getString(1);
            }
            rs.close();
            stmt.close();

            // CONVERTIR FECHAS LONG A DATE.
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("yyyyMMdd");
            Date fecha_date_1 = dateFormat_1.parse(fecha1.toString());
            Date fecha_date_2 = dateFormat_1.parse(fecha2.toString());

            SimpleDateFormat dateFormat_2 = new SimpleDateFormat("dd/MM/yyyy");

            //OBTENER FECHA1 JULIANA.
            Integer fecha_julinana_1 = 0;
            cadenasql = "SELECT TO_NUMBER(SUBSTR(TO_CHAR(TO_DATE('" + dateFormat_2.format(fecha_date_1) + "','dd/MM/yyyy'),'ccYYddd'),2,6)) FECHA_JDE FROM DUAL";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                fecha_julinana_1 = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            //OBTENER FECHA2 JULIANA.
            Integer fecha_julinana_2 = 0;
            cadenasql = "SELECT TO_NUMBER(SUBSTR(TO_CHAR(TO_DATE('" + dateFormat_2.format(fecha_date_2) + "','dd/MM/yyyy'),'ccYYddd'),2,6)) FECHA_JDE FROM DUAL";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                fecha_julinana_2 = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            //EXTRAER FACTURAS DE JDE: LAS QUE NO HAN SIDO ENVIADAS A GT-FACTURAS:=(TRIM(A.SDCRMD) IS NULL).
            List<FEL_DTE> lista_documentos = new ArrayList<>();
            cadenasql = "SELECT DISTINCT "
                    + "A.SDKCOO, "
                    + "A.SDAN8, "
                    + "A.SDSHAN, "
                    + "A.SDDOCO, "
                    + "A.SDDCTO, "
                    + "A.SDDOC, "
                    + "TO_CHAR(TO_DATE(TO_CHAR(A.SDIVD + 1900000,'9999999'),'YYYYDDD'),'yyyyMMdd') FECHA_GREGORIANA "
                    + "FROM " + ambiente + ".F42119@JDENERGIA A "
                    + "WHERE "
                    + "(TRIM(A.SDCRMD) IS NULL) AND "
                    + "(A.SDIVD  BETWEEN " + fecha_julinana_1 + " AND " + fecha_julinana_2 + ") AND "
                    + "(A.SDDOC <> 0) AND "
                    + "(A.SDLTTR NOT IN (980,900)) AND "
                    + "(A.SDDCTO IN ('S3','C3','SD')) AND "
                    + "(A.SDKCOO IN ('" + compania + "'))";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Date fecha_hora_actual = new Date();
                SimpleDateFormat dateFormat_3 = new SimpleDateFormat("yyyyMMddHHmmss");
                FEL_DTE fel_dte = new FEL_DTE(
                        1,
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        "NOMBRE CLIENTE",
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getLong(7),
                        Long.valueOf(dateFormat_3.format(fecha_hora_actual)),
                        Long.valueOf("0"),
                        "NO",
                        ambiente,
                        "-");
                lista_documentos.add(fel_dte);
            }
            rs.close();
            stmt.close();

            // OBTENER NUMERO SIGUIENTE CORRELATIVO TABLA FEL_DTE ID_DTE.
            cadenasql = "SELECT NVL(MAX(F.ID_DTE),0)+1 MAXIMO FROM FEL_DTE F";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            Integer maximo_id_dte = 0;
            while (rs.next()) {
                maximo_id_dte = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // INSERTAR REGISTROS TABLA FEL_DTE.
            for (Integer i = 0; i < lista_documentos.size(); i++) {
                Integer id_dte = maximo_id_dte + i;

                // NOMBRE DEL COMPRADOR.
                String nombre_comprador = "";
                cadenasql = "SELECT NVL(TRIM(A.WWMLNM),'-') FROM " + ambiente + ".F0111@JDENERGIA A WHERE A.WWIDLN=0 AND A.WWAN8=" + lista_documentos.get(i).getAn8_cliente_jde();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    nombre_comprador = rs.getString(1);
                }
                rs.close();
                stmt.close();

                cadenasql = "SELECT NVL(TRIM(D.ALADD1),' ') FROM " + ambiente + ".F0116@JDENERGIA D WHERE D.ALAN8=" + lista_documentos.get(i).getAn8_cliente_jde();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    nombre_comprador = nombre_comprador + " " + rs.getString(1);
                    lista_documentos.get(i).setNombre_cliente_jde(nombre_comprador);
                }
                rs.close();
                stmt.close();

                cadenasql = "INSERT INTO FEL_DTE ("
                        + "ID_DTE, "
                        + "KCOO_COMPANIA_JDE, "
                        + "AN8_CLIENTE_JDE, "
                        + "SHAN_SUCURSAL_CLIENTE_JDE, "
                        + "NOMBRE_CLIENTE_JDE, "
                        + "DOCO_NO_ORDEN_JDE, "
                        + "DCTO_TIPO_ORDEN_JDE, "
                        + "DOC_NO_DOCUMENTO_JDE, "
                        + "FECHA_DOCUMENTO, "
                        + "FECHA_HORA_CARGA, "
                        + "FECHA_HORA_ENVIO, "
                        + "ENVIADO, "
                        + "AMBIENTE, "
                        + "OBSERVACION) VALUES ("
                        + id_dte + ",'"
                        + lista_documentos.get(i).getKcoo_compania_jde() + "',"
                        + lista_documentos.get(i).getAn8_cliente_jde() + ","
                        + lista_documentos.get(i).getShan_sucursal_cliente_jde() + ",'"
                        + lista_documentos.get(i).getNombre_cliente_jde().replaceAll("'", "") + "',"
                        + lista_documentos.get(i).getDoco_no_orden_jde() + ",'"
                        + lista_documentos.get(i).getDcto_tipo_orden_jde() + "',"
                        + lista_documentos.get(i).getDoc_no_documento_jde() + ","
                        + lista_documentos.get(i).getFecha_documento() + ","
                        + lista_documentos.get(i).getFecha_hora_carga() + ","
                        + lista_documentos.get(i).getFecha_hora_envio() + ",'"
                        + lista_documentos.get(i).getEnviado() + "','"
                        + lista_documentos.get(i).getAmbiente() + "','"
                        + lista_documentos.get(i).getObservacion() + "')";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();

                // INSERTAR ENCABEZADO DE LOS DOCUMENTOS DTE.
                resultado = this.Cargar_Encabezado_Fel(
                        conn,
                        ambiente,
                        id_dte,
                        lista_documentos.get(i).getFecha_documento(),
                        lista_documentos.get(i).getDcto_tipo_orden_jde(),
                        lista_documentos.get(i).getAn8_cliente_jde(),
                        lista_documentos.get(i).getDoco_no_orden_jde(),
                        lista_documentos.get(i).getKcoo_compania_jde());
                String[] result = resultado.split("♣");
                if (result[0].equals("1")) {
                    throw new Exception(result[1]);
                }

                // INSERTAR DETALLE DE LOS DOCUMENTOS DTE.
                resultado = this.Cargar_Detalle_Fel(
                        conn,
                        ambiente,
                        id_dte,
                        lista_documentos.get(i).getDoco_no_orden_jde(),
                        lista_documentos.get(i).getDcto_tipo_orden_jde(),
                        lista_documentos.get(i).getKcoo_compania_jde(),
                        lista_documentos.get(i).getAn8_cliente_jde());
                result = resultado.split("♣");
                if (result[0].equals("1")) {
                    throw new Exception(result[1]);
                }

                // INSERTAR ASOCIADOS DE LOS DOCUMENTOS DTE.
                if (!lista_documentos.get(i).getDcto_tipo_orden_jde().equals("S3")) {
                    resultado = this.Cargar_Asociados_Fel(
                            conn,
                            ambiente,
                            id_dte,
                            lista_documentos.get(i).getDoco_no_orden_jde(),
                            lista_documentos.get(i).getDcto_tipo_orden_jde(),
                            lista_documentos.get(i).getKcoo_compania_jde(),
                            lista_documentos.get(i).getDoc_no_documento_jde());
                    result = resultado.split("♣");
                    if (result[0].equals("1")) {
                        throw new Exception(result[1]);
                    }
                }

                // INSERTAR TOTALES DE LOS DOCUMENTOS DTE.
                resultado = this.Cargar_Totales_Fel(
                        conn,
                        id_dte,
                        lista_documentos.get(i).getDcto_tipo_orden_jde());
                result = resultado.split("♣");
                if (result[0].equals("1")) {
                    throw new Exception(result[1]);
                }

                // MARCAR REGISTROS TABLA F42119@JDENERGIA:=[SDCRMD='4'].
                cadenasql = "UPDATE " + ambiente + ".F42119@JDENERGIA SET "
                        + "SDCRMD = '4' "
                        + "WHERE "
                        + "SDDOCO=" + lista_documentos.get(i).getDoco_no_orden_jde() + " AND "
                        + "SDDCTO='" + lista_documentos.get(i).getDcto_tipo_orden_jde() + "' AND "
                        + "SDKCOO='" + lista_documentos.get(i).getKcoo_compania_jde() + "'";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = "0♣DOCUMENTOS CARGADOS.";

        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);

                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Facturas_Fel MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Facturas_Fel MENSAJE: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Facturas_Fel - Rollback MENSAJE: " + ex1.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Facturas_Fel - Rollback MENSAJE: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Facturas_Fel - finally MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Facturas_Fel - finally MENSAJE: " + ex.toString();
            }
        }

        return resultado;
    }

    private String Cargar_Encabezado_Fel(Connection conn, String ambiente, Integer id_dte, Long fecha_documento, String dcto, Integer an8, Integer doco, String kcoo) {
        String resultado = "";

        try {
            Integer tipo_registro = 1;
            String enviar_correo = "N";
            Integer numero_acceso = 0;
            // TIPO DEL DOCUMENTO.
            String id_tipo_documento = "";
            String cadenasql = "SELECT NVL(TRIM(F.ID_TIPO_DOCUMENTO),'-') FROM FEL_CAT_TIPO_DOCUMENTO F WHERE F.CODIGO_JDE='" + dcto + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_tipo_documento = rs.getString(1);
            }
            rs.close();
            stmt.close();

            // NIT DEL COMPRADOR.
            String nit_comprador = "";
            cadenasql = "SELECT NVL(TRIM(T.ABTAX),'-') FROM " + ambiente + ".F0101@JDENERGIA T WHERE T.ABAN8=" + an8;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                nit_comprador = rs.getString(1);
            }
            rs.close();
            stmt.close();
            if (nit_comprador.equals("")) {
                nit_comprador = "C/F";
            }

            // MONEDA DE LA FACTURA.
            String moneda_jde = "";
            cadenasql = "SELECT DISTINCT NVL(TRIM(A.SDCRCD),'-') FROM " + ambiente + ".F42119@JDENERGIA A WHERE A.SDDOCO = " + doco + " AND A.SDDCTO='" + dcto + "' AND A.SDKCOO='" + kcoo + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                moneda_jde = rs.getString(1);
            }
            rs.close();
            stmt.close();

            Integer id_codigo_moneda = 0;
            cadenasql = "SELECT NVL(TRIM(F.ID_CODIGO_MONEDA),0) FROM FEL_CAT_CODIGO_MONEDA F WHERE F.MONEDA_JDE='" + moneda_jde + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_codigo_moneda = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // TASA DE CAMBIO.
            Double tasa_cambio = 0.00;
            cadenasql = "SELECT DISTINCT DECODE(NVL(A.SDCRR,0.00),0.00,1.00,A.SDCRR) FROM " + ambiente + ".F42119@JDENERGIA A WHERE A.SDDOCO = " + doco + " AND A.SDDCTO='" + dcto + "' AND A.SDKCOO='" + kcoo + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                tasa_cambio = rs.getDouble(1);
            }
            rs.close();
            stmt.close();

            // NUMERO EXTERNO.
            String numero_externo = "";
            cadenasql = "SELECT DISTINCT A.SDDOC FROM " + ambiente + ".F42119@JDENERGIA A WHERE A.SDDOCO = " + doco + " AND A.SDDCTO='" + dcto + "' AND A.SDKCOO='" + kcoo + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                numero_externo = rs.getString(1);
            }
            rs.close();
            stmt.close();

            // TIPO DE VENTA.
            String id_tipo_venta = "";
            cadenasql = "SELECT DISTINCT DECODE(TRIM(A.SDLNTY),'N','S','B') FROM " + ambiente + ".F42119@JDENERGIA A WHERE A.SDDOCO = " + doco + " AND A.SDDCTO='" + dcto + "' AND A.SDKCOO='" + kcoo + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_tipo_venta = rs.getString(1);
            }
            rs.close();
            stmt.close();

            // DESTINO DE VENTA.
            String contry_jde = "";
            cadenasql = "SELECT NVL(TRIM(A.ALCTR),0) FROM " + ambiente + ".F0116@JDENERGIA A WHERE A.ALAN8=" + an8;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                contry_jde = rs.getString(1);
            }
            rs.close();
            stmt.close();

            Integer id_destino_venta = 0;
            cadenasql = "SELECT NVL(TRIM(F.ID_DESTINO_VENTA),0) FROM FEL_CAT_DESTINO_VENTA F WHERE F.COUNTRY_JDE='" + contry_jde + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_destino_venta = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // NOMBRE DEL COMPRADOR.
            String nombre_comprador = "";
            cadenasql = "SELECT NVL(TRIM(A.WWMLNM),'-') FROM " + ambiente + ".F0111@JDENERGIA A WHERE A.WWIDLN=0 AND A.WWAN8=" + an8;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                nombre_comprador = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT NVL(TRIM(D.ALADD1),' ') FROM " + ambiente + ".F0116@JDENERGIA D WHERE D.ALAN8=" + an8;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                nombre_comprador = nombre_comprador + " " + rs.getString(1);
            }
            rs.close();
            stmt.close();

            // DIRECCION DEL COMPRADOR.
            String direccion = "";
            cadenasql = "SELECT NVL(TRIM(D.ALADD2),' ') || ' ' || NVL(TRIM(D.ALADD3),' ') || ' ' || NVL(TRIM(D.ALADD4),' ') FROM " + ambiente + ".F0116@JDENERGIA D WHERE D.ALAN8=" + an8;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                direccion = rs.getString(1);
            }
            rs.close();
            stmt.close();

            // SERIE Y NUMERO ADMIN
            String serie_admin = numero_externo.substring(0, 2);
            switch (serie_admin) {
                case "11": {
                    serie_admin = "A1";
                    break;
                }
                case "21": {
                    serie_admin = "C1";
                    break;
                }
                case "31": {
                    serie_admin = "D1";
                    break;
                }
            }
            Integer numero_admin = Integer.valueOf(numero_externo);

            // INCOTERM
            String incoterm = "";
            cadenasql = "SELECT NVL(TRIM(F.SHZON),'FOB') FROM " + ambiente + ".F42019@JDENERGIA F WHERE F.SHDOCO=" + doco + " AND F.SHDCTO='" + dcto + "' AND F.SHKCOO='" + kcoo + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                incoterm = rs.getString(1);
            }
            rs.close();
            stmt.close();

            // CODIGO_EXPORTADOR
            String id_tipo_receptor = "";
            cadenasql = "SELECT NVL(TRIM(G.ABTX2),4) FROM " + ambiente + ".F0101@JDENERGIA G WHERE G.ABAN8=" + an8;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_tipo_receptor = rs.getString(1);
            }
            rs.close();
            stmt.close();
            try {
                Long.valueOf(id_tipo_receptor);
                if(!(id_tipo_receptor.equals("2") || id_tipo_receptor.equals("3") || id_tipo_receptor.equals("4"))) {
                    id_tipo_receptor = "3";
                }
            } catch(Exception ex1) {
                id_tipo_receptor = "3";
            }

            // INSERTAR ENCABEZADO DEL DTE.
            cadenasql = "INSERT INTO FEL_ENCABEZADO ("
                    + "ID_DTE, "
                    + "TIPO_REGISTRO, "
                    + "FECHA_DOCUMENTO, "
                    + "ID_TIPO_DOCUMENTO, "
                    + "NIT_COMPRADOR, "
                    + "ID_CODIGO_MONEDA, "
                    + "TASA_CAMBIO, "
                    + "ORDEN_EXTERNO, "
                    + "ID_TIPO_VENTA, "
                    + "ID_DESTINO_VENTA, "
                    + "ENVIAR_CORREO, "
                    + "NOMBRE_COMPRADOR, "
                    + "DIRECCION, "
                    + "NUMERO_ACCESO, "
                    + "SERIE_ADMIN, "
                    + "NUMERO_ADMIN, "
                    + "INCOTERM, "
                    + "CODIGO_EXPORTADOR, "
                    + "ID_TIPO_RECEPTOR) VALUES ("
                    + id_dte + ","
                    + tipo_registro + ","
                    + fecha_documento + ",'"
                    + id_tipo_documento.trim() + "','"
                    + nit_comprador.trim() + "',"
                    + id_codigo_moneda + ","
                    + tasa_cambio + ",'"
                    + numero_externo.trim() + "','"
                    + id_tipo_venta.trim() + "',"
                    + id_destino_venta + ",'"
                    + enviar_correo.trim() + "','"
                    + nombre_comprador.trim().replaceAll("'", "") + "','"
                    + direccion.trim() + "',"
                    + numero_acceso + ",'"
                    + serie_admin.trim() + "',"
                    + numero_admin + ",'"
                    + incoterm + "','"
                    + nit_comprador.trim() +  "',"
                    + id_tipo_receptor + ")";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = "0♣ENCABEZADO DE DOCUMENTO CARGADO.";
        } catch (Exception ex) {
            System.out.println("ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Encabezado_Fel MENSAJE: " + ex.toString());
            resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Encabezado_Fel MENSAJE: " + ex.toString();
        }

        return resultado;
    }

    private String Cargar_Detalle_Fel(Connection conn, String ambiente, Integer id_dte, Integer doco, String dcto, String kcoo, Integer an8) {
        String resultado = "";

        try {
            Integer tipo_registro = 2;
            Integer linea_detalle = 1;
            // DESTINO DE VENTA.
            String contry_jde = "";
            String cadenasql = "SELECT NVL(TRIM(A.ALCTR),0) FROM " + ambiente + ".F0116@JDENERGIA A WHERE A.ALAN8=" + an8;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                contry_jde = rs.getString(1);
            }
            rs.close();
            stmt.close();

            Integer id_destino_venta = 0;
            cadenasql = "SELECT NVL(TRIM(F.ID_DESTINO_VENTA),0) FROM FEL_CAT_DESTINO_VENTA F WHERE F.COUNTRY_JDE='" + contry_jde + "'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_destino_venta = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            // OBTENER DATOS DEL DETALLE DE LA ORDEN.
            Double descuento = 0.00;
            cadenasql = "SELECT "
                    + "(DECODE(TRIM(F.SDCRCD),'GTQ',F.SDUPRC,'USD',F.SDFUP) / 10000.00) IMPORTE_DESCUENTO "
                    + "FROM "
                    + ambiente + ".F42119@JDENERGIA F "
                    + "WHERE "
                    + "F.SDLTTR NOT IN (980,900) AND "
                    + "F.SDITM = 28403 AND "
                    + "F.SDKCOO = '" + kcoo + "' AND "
                    + "F.SDDCTO = '" + dcto + "' AND "
                    + "F.SDDOCO = " + doco;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                descuento = rs.getDouble(1);
            }
            rs.close();
            stmt.close();

            if (descuento < 0.00) {
                descuento = descuento * (-1);
            }

            List<FEL_DETALLE_DTE> lista_detalle_fel = new ArrayList<>();
            cadenasql = "SELECT "
                    + "F.SDUORG CANTIDAD, "
                    + "(DECODE(TRIM(F.SDCRCD),'GTQ',F.SDUPRC,'USD',F.SDFUP) / 10000.00) PRECIO_PRODUCTO, "
                    + "DECODE(F.SDTAX1,'Y',NVL((SELECT I.TATXR1/1000.00/100 FROM " + ambiente + ".F4008@JDENERGIA I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=F.SDITM),(SELECT I.TATXR1/1000.00/100 FROM " + ambiente + ".F4008@JDENERGIA I WHERE I.TATXA1=F.SDTXA1 AND I.TAITM=0)),0.00) PORCENTAJE_IMPUESTO, "
                    + "DECODE(F.SDTAX1,'Y',DECODE(TRIM(F.SDTXA1),'GZ',(SELECT I.TATXR1/1000.00/100 FROM " + ambiente + ".F4008@JDENERGIA I WHERE TRIM(I.TATXA1)='GIVA' AND I.TAITM=0),0.00),0.00) PORCENTAJE_EXENTO, "
                    + "TRIM(SUBSTR(F.SDLITM,0,20)) CODIGO_PRODUCTO, "
                    + "DECODE(TRIM(TRIM(F.SDDSC1) || ' ' || TRIM(F.SDDSC2)),'.',TRIM(F.SDLITM),TRIM(TRIM(F.SDDSC1) || ' ' || TRIM(F.SDDSC2))) DESCRIPCION, "
                    + "DECODE(TRIM(F.SDLNTY),'N','S','C','S','B') TIPO_VENTA, "
                    + "TRIM(F.SDITM) ITEM, "
                    + "TRIM(F.SDKCOO) KCOO, "
                    + "F.SDDOCO DOCO, "
                    + "TRIM(F.SDDCTO) DCTO, "
                    + "TRIM(REPLACE(F.SDLNID,'.','')) LNID "
                    + "FROM "
                    + ambiente + ".F42119@JDENERGIA F "
                    + "WHERE "
                    + "F.SDLTTR NOT IN (980,900) AND "
                    + "F.SDITM <> 28403 AND "
                    + "F.SDKCOO='" + kcoo + "' AND "
                    + "F.SDDCTO='" + dcto + "' AND "
                    + "F.SDDOCO=" + doco;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                String codigo_producto_interno = rs.getString(8);

                String descripcion = rs.getString(6);

                String codigo_producto = rs.getString(5);
                String tipo_venta = rs.getString(7);

                String kcoo_detalle = rs.getString(9);
                Integer doco_detalle = rs.getInt(10);
                String dcto_detalle = rs.getString(11);
                Integer lnid_detalle = rs.getInt(12);

                Double cantidad = rs.getDouble(1);
                Double precio_producto = rs.getDouble(2);
                Double porcentaje_impuesto = rs.getDouble(3);
                Double porcentaje_exento = rs.getDouble(4);

                if (cantidad == null) {
                    cantidad = 1.00;
                } else {
                    if (cantidad == 0.00) {
                        cantidad = 1.00;
                    } else {
                        if (cantidad < 0.00) {
                            cantidad = cantidad * (-1);
                        }
                    }
                }

                if (precio_producto < 0.00) {
                    precio_producto = precio_producto * (-1);
                }
                if (porcentaje_impuesto < 0.00) {
                    porcentaje_impuesto = porcentaje_impuesto * (-1);
                }
                if (porcentaje_exento < 0.00) {
                    porcentaje_exento = porcentaje_exento * (-1);
                }

                if (codigo_producto_interno.equals("28401")) {
                    if (id_destino_venta != 1) {
                        porcentaje_exento = 0.12;
                        porcentaje_impuesto = 0.00;
                    }
                    Double precio = precio_producto + (precio_producto * porcentaje_impuesto);
                    Double importe_descuento = descuento + (descuento * porcentaje_impuesto);
                    Double porcentaje_descuento = importe_descuento / precio;
                    Double importe_bruto = cantidad * precio;
                    Double importe_neto = precio_producto - descuento;
                    Double importe_impuesto = (precio_producto - descuento) * porcentaje_impuesto;
                    Double importe_total = importe_neto + importe_impuesto;
                    Double importe_exento = 0.00;
                    if (porcentaje_exento > 0.00) {
                        importe_neto = 0.00;
                        importe_exento = precio_producto - descuento;
                    }

                    FEL_DETALLE_DTE fel_detalle_dte = new FEL_DETALLE_DTE(
                            id_dte,
                            tipo_registro,
                            linea_detalle,
                            cantidad, // CANTIDAD.
                            1, // UNIDAD MEDIDA.
                            precio, // PRECIO.
                            porcentaje_descuento, // PORCENTAJE DESCUENTO.
                            importe_descuento, // IMPORTE DESCUENTO.
                            importe_bruto, // IMPORTE BRUTO.
                            importe_exento, // IMPORTE EXENTO.
                            importe_neto, // IMPORTE NETO.
                            importe_impuesto, // IMPORTE IMPUESTO.
                            0.00, // IMPORTE OTROS.
                            importe_total, // IMPORTE TOTAL.
                            codigo_producto,
                            descripcion,
                            tipo_venta,
                            kcoo_detalle,
                            doco_detalle,
                            dcto_detalle,
                            lnid_detalle);
                    lista_detalle_fel.add(fel_detalle_dte);
                    this.lista_detalle_fel_global.add(fel_detalle_dte);
                } else {
                    if (id_destino_venta != 1) {
                        porcentaje_exento = 0.12;
                        porcentaje_impuesto = 0.00;
                    }
                    Double precio = precio_producto + (precio_producto * porcentaje_impuesto);
                    Double importe_descuento = 0.00;
                    Double porcentaje_descuento = 0.00;
                    Double importe_bruto = cantidad * precio;
                    Double importe_neto = precio_producto - 0.00;
                    Double importe_impuesto = (precio_producto - 0.00) * porcentaje_impuesto;
                    Double importe_total = importe_neto + importe_impuesto;

                    Double importe_exento = 0.00;
                    if (porcentaje_exento > 0.00) {
                        importe_neto = 0.00;
                        importe_exento = precio_producto - descuento;
                    }

                    FEL_DETALLE_DTE fel_detalle_dte = new FEL_DETALLE_DTE(
                            id_dte,
                            tipo_registro,
                            linea_detalle,
                            cantidad, // CANTIDAD.
                            1, // UNIDAD MEDIDA.
                            precio, // PRECIO.
                            porcentaje_descuento, // PORCENTAJE DESCUENTO.
                            importe_descuento, // IMPORTE DESCUENTO.
                            importe_bruto, // IMPORTE BRUTO.
                            importe_exento, // IMPORTE EXENTO.
                            importe_neto, // IMPORTE NETO.
                            importe_impuesto, // IMPORTE IMPUESTO.
                            0.00, // IMPORTE OTROS.
                            importe_total, // IMPORTE TOTAL.
                            codigo_producto,
                            descripcion,
                            tipo_venta,
                            kcoo_detalle,
                            doco_detalle,
                            dcto_detalle,
                            lnid_detalle);
                    lista_detalle_fel.add(fel_detalle_dte);
                    this.lista_detalle_fel_global.add(fel_detalle_dte);
                }
                linea_detalle++;
            }
            rs.close();
            stmt.close();

            for (Integer i = 0; i < lista_detalle_fel.size(); i++) {
                // INSERTAR DETALLE DEL DTE.
                cadenasql = "INSERT INTO FEL_DETALLE ("
                        + "ID_DTE, "
                        + "TIPO_REGISTRO, "
                        + "ID_DETALLE, "
                        + "CANTIDAD, "
                        + "ID_UNIDAD_MEDIDA, "
                        + "PRECIO, "
                        + "PORCENTAJE_DESCUENTO, "
                        + "IMPORTE_DESCUENTO, "
                        + "IMPORTE_BRUTO, "
                        + "IMPORTE_EXENTO, "
                        + "IMPORTE_NETO, "
                        + "IMPORTE_IVA, "
                        + "IMPORTE_OTROS, "
                        + "IMPORTE_TOTAL, "
                        + "PRODUCTO, "
                        + "DESCRIPCION, "
                        + "ID_TIPO_VENTA) VALUES ("
                        + lista_detalle_fel.get(i).getId_dte() + ","
                        + lista_detalle_fel.get(i).getTipo_registro() + ","
                        + lista_detalle_fel.get(i).getId_detalle() + ","
                        + lista_detalle_fel.get(i).getCantidad() + ","
                        + lista_detalle_fel.get(i).getId_unidad_medida() + ","
                        + lista_detalle_fel.get(i).getPrecio() + ","
                        + lista_detalle_fel.get(i).getPorcentaje_descuento() + ","
                        + lista_detalle_fel.get(i).getImporte_descuento() + ","
                        + lista_detalle_fel.get(i).getImporte_bruto() + ","
                        + lista_detalle_fel.get(i).getImporte_exento() + ","
                        + lista_detalle_fel.get(i).getImporte_neto() + ","
                        + lista_detalle_fel.get(i).getImporte_iva() + ","
                        + lista_detalle_fel.get(i).getImporte_otros() + ","
                        + lista_detalle_fel.get(i).getImporte_total() + ",'"
                        + lista_detalle_fel.get(i).getProducto() + "','"
                        + lista_detalle_fel.get(i).getDescripcion() + "','"
                        + lista_detalle_fel.get(i).getId_tipo_venta() + "')";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            resultado = "0♣DETALLE DE DOCUMENTO CARGADO.";
        } catch (Exception ex) {
            System.out.println("ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Detalle_Fel MENSAJE: " + ex.toString());
            resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Detalle_Fel MENSAJE: " + ex.toString();
        }

        return resultado;
    }

    private String Cargar_Asociados_Fel(Connection conn, String ambiente, Integer id_dte, Integer doco, String dcto, String kcoo, Integer doc) {
        String resultado = "";

        try {
            Integer tipo_registro = 3;
            String documento_interno = "0";
            String cadenasql = "SELECT "
                    + "TRIM(F.SDVR01) DOCUMENTO_INTERNO "
                    + "FROM "
                    + ambiente + ".F42119@JDENERGIA F "
                    + "WHERE "
                    + "F.SDLTTR NOT IN (980,900) AND "
                    + "F.SDKCOO = '" + kcoo + "' AND "
                    + "F.SDDCTO = '" + dcto + "' AND "
                    + "F.SDDOCO = " + doco;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                documento_interno = rs.getString(1);
            }
            rs.close();
            stmt.close();

            try {
                Long.valueOf(documento_interno);
            } catch (Exception ex) {
                documento_interno = "0";
            }

            Integer id_dte_asociado = 0;
            Long fecha_documento_asociado = Long.valueOf(0);
            cadenasql = "SELECT "
                    + "F.ID_DTE, "
                    + "F.FECHA_DOCUMENTO "
                    + "FROM "
                    + "FEL_DTE F "
                    + "WHERE "
                    + "F.DCTO_TIPO_ORDEN_JDE='S3' AND "
                    + "F.KCOO_COMPANIA_JDE='" + kcoo + "' AND "
                    + "F.DOC_NO_DOCUMENTO_JDE=" + documento_interno;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                id_dte_asociado = rs.getInt(1);
                fecha_documento_asociado = rs.getLong(2);
            }
            rs.close();
            stmt.close();

            String serie_auth_asoc = "";
            Long numero_auth_asoc = Long.valueOf(0);
            cadenasql = "SELECT F.SERIE, F.PREIMPRESO FROM FEL_GENERADO F WHERE F.ID_DTE=" + id_dte_asociado;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                serie_auth_asoc = rs.getString(1);
                numero_auth_asoc = rs.getLong(2);
            }
            rs.close();
            stmt.close();

            if (!serie_auth_asoc.equals("") && numero_auth_asoc > Long.valueOf(0)) {
                // INSERTAR ASOCIADOS DEL DTE.
                cadenasql = "INSERT INTO FEL_ASOCIADOS ("
                        + "ID_DTE, "
                        + "TIPO_REGISTRO, "
                        + "ID_TIPO_DOCUMENTO, "
                        + "SERIE, "
                        + "NUMERO, "
                        + "FECHA_DOCUMENTO) VALUES ("
                        + id_dte + ","
                        + tipo_registro + ",'"
                        + "FACT" + "','"
                        + serie_auth_asoc + "',"
                        + numero_auth_asoc + ","
                        + fecha_documento_asociado + ")";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            resultado = "0♣ASOCIADOS DE DOCUMENTO CARGADO.";
        } catch (Exception ex) {
            System.out.println("ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Asociados_Fel MENSAJE: " + ex.toString());
            resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Asociados_Fel MENSAJE: " + ex.toString();
        }

        return resultado;
    }

    private String Cargar_Totales_Fel(Connection conn, Integer id_dte, String dcto) {
        String resultado = "";

        try {
            Integer tipo_registro = 4;
            Double importe_bruto = 0.00;
            Double importe_descuento = 0.00;
            Double importe_exento = 0.00;
            Double importe_neto = 0.00;
            Double importe_iva = 0.00;
            Double importe_otros = 0.00;
            Double importe_total = 0.00;
            Double porcentaje_isr = 0.00;
            Double importe_isr = 0.00;
            Integer registros_detalle = 0;
            Integer documento_asociado = 0;

            String cadenasql = "SELECT "
                    + "SUM(F.IMPORTE_BRUTO) IMPORTE_BRUTO, "
                    + "SUM(F.IMPORTE_DESCUENTO) IMPORTE_DESCUENTO, "
                    + "SUM(F.IMPORTE_EXENTO) IMPORTE_EXENTO, "
                    + "SUM(F.IMPORTE_NETO) IMPORTE_NETO, "
                    + "SUM(F.IMPORTE_IVA) IMPORTE_IVA, "
                    + "SUM(F.IMPORTE_OTROS) IMPORTE_OTROS, "
                    + "SUM(F.IMPORTE_TOTAL) IMPORTE_TOTAL, "
                    + "SUM(0.00) PORCENTAJE_ISR, "
                    + "SUM(0.00) IMPORTE_ISR, "
                    + "COUNT(*) REGISTROS_DETALLE, "
                    + "F.ID_DTE ID_DTE, "
                    + "0 DOCUMENTOS_ASOCIADOS "
                    + "FROM "
                    + "FEL_DETALLE F "
                    + "WHERE "
                    + "F.ID_DTE=" + id_dte + " "
                    + "GROUP BY  "
                    + "F.ID_DTE, "
                    + "0";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                importe_bruto = rs.getDouble(1);
                importe_descuento = rs.getDouble(2);
                importe_exento = rs.getDouble(3);
                importe_neto = rs.getDouble(4);
                importe_iva = rs.getDouble(5);
                importe_otros = rs.getDouble(6);
                importe_total = rs.getDouble(7);
                porcentaje_isr = rs.getDouble(8);
                importe_isr = rs.getDouble(9);
                registros_detalle = rs.getInt(10);
                documento_asociado = rs.getInt(12);
            }
            rs.close();
            stmt.close();

            if (importe_bruto < 0.00) {
                importe_bruto = importe_bruto * (-1);
            }
            if (importe_descuento < 0.00) {
                importe_descuento = importe_descuento * (-1);
            }
            if (importe_exento < 0.00) {
                importe_exento = importe_exento * (-1);
            }
            if (importe_neto < 0.00) {
                importe_neto = importe_neto * (-1);
            }
            if (importe_iva < 0.00) {
                importe_iva = importe_iva * (-1);
            }
            if (importe_otros < 0.00) {
                importe_otros = importe_otros * (-1);
            }
            if (importe_total < 0.00) {
                importe_total = importe_total * (-1);
            }
            if (porcentaje_isr < 0.00) {
                porcentaje_isr = porcentaje_isr * (-1);
            }
            if (importe_isr < 0.00) {
                importe_isr = importe_isr * (-1);
            }
            if (registros_detalle < 0.00) {
                registros_detalle = registros_detalle * (-1);
            }
            if (documento_asociado < 0.00) {
                documento_asociado = documento_asociado * (-1);
            }
            if (!dcto.equals("S3")) {
                documento_asociado = 1;
            }

            // INSERTAR TOTALES DEL DTE.
            cadenasql = "INSERT INTO FEL_TOTALES ("
                    + "ID_DTE, "
                    + "TIPO_REGISTRO, "
                    + "IMPORTE_BRUTO, "
                    + "IMPORTE_DESCUENTO, "
                    + "IMPORTE_EXENTO, "
                    + "IMPORTE_NETO, "
                    + "IMPORTE_IVA, "
                    + "IMPORTE_OTROS, "
                    + "IMPORTE_TOTAL, "
                    + "PORCENTAJE_ISR, "
                    + "IMPORTE_ISR, "
                    + "REGISTROS_DETALLE, "
                    + "DOCUMENTOS_ASOCIADOS) VALUES ("
                    + id_dte + ","
                    + tipo_registro + ","
                    + importe_bruto + ","
                    + importe_descuento + ","
                    + importe_exento + ","
                    + importe_neto + ","
                    + importe_iva + ","
                    + importe_otros + ","
                    + importe_total + ","
                    + porcentaje_isr + ","
                    + importe_isr + ","
                    + registros_detalle + ","
                    + documento_asociado + ")";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = "0♣TOTALES DE DOCUMENTO CARGADO.";
        } catch (Exception ex) {
            System.out.println("ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Totales_Fel_Fel MENSAJE: " + ex.toString());
            resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: Cargar_Totales_Fel MENSAJE: " + ex.toString();
        }

        return resultado;
    }

    public String desmarcar_fel(String ambiente, Long id_dte) {
        String resultado = "";
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_fel_energia(ambiente);

            conn.setAutoCommit(false);

            // OBTENER KCOO, DOCO, DCTO.
            String kcoo = "";
            Integer doco = 0;
            String dcto = "";
            String ambiente_jde = "";
            String cadenasql = "SELECT "
                    + "F.KCOO_COMPANIA_JDE, "
                    + "F.DOCO_NO_ORDEN_JDE, "
                    + "F.DCTO_TIPO_ORDEN_JDE, "
                    + "F.AMBIENTE "
                    + "FROM "
                    + "FEL_DTE F "
                    + "WHERE "
                    + "F.ID_DTE=" + id_dte;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                kcoo = rs.getString(1);
                doco = rs.getInt(2);
                dcto = rs.getString(3);
                ambiente_jde = rs.getString(4);
            }
            rs.close();
            stmt.close();

            //ELEMINAR TABLA FEL_GENERADO.
            cadenasql = "DELETE FROM FEL_GENERADO WHERE ID_DTE=" + id_dte;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            //ELEMINAR TABLA FEL_TOTALES.
            cadenasql = "DELETE FROM FEL_TOTALES WHERE ID_DTE=" + id_dte;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            //ELEMINAR TABLA FEL_ASOCIADOS.
            cadenasql = "DELETE FROM FEL_ASOCIADOS WHERE ID_DTE=" + id_dte;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            //ELEMINAR TABLA FEL_DETALLE.
            cadenasql = "DELETE FROM FEL_DETALLE WHERE ID_DTE=" + id_dte;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            //ELEMINAR TABLA FEL_ENCABEZADO.
            cadenasql = "DELETE FROM FEL_ENCABEZADO WHERE ID_DTE=" + id_dte;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            //ELEMINAR TABLA FEL_DTE.
            cadenasql = "DELETE FROM FEL_DTE WHERE ID_DTE=" + id_dte;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            // DESMARCAR REGISTROS TABLA F42119@JDENERGIA:=[SDCRMD=NULL].
            cadenasql = "UPDATE " + ambiente_jde + ".F42119@JDENERGIA SET "
                    + "SDCRMD = NULL "
                    + "WHERE "
                    + "SDKCOO='" + kcoo + "' AND "
                    + "SDDOCO=" + doco + " AND "
                    + "SDDCTO='" + dcto + "'";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            cadenasql = "UPDATE " + ambiente_jde + ".F4211@JDENERGIA SET "
                    + "SDCRMD = NULL "
                    + "WHERE "
                    + "SDKCOO='" + kcoo + "' AND "
                    + "SDDOCO=" + doco + " AND "
                    + "SDDCTO='" + dcto + "'";
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = "0♣DOCUMENTO DESMARCADO, PUEDE VOLVER A CARGAR ESTE DOCUMENTOS.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);

                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: desmarcar_fel MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: desmarcar_fel MENSAJE: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: desmarcar_fel - Rollback MENSAJE: " + ex1.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: desmarcar_fel - Rollback MENSAJE: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: desmarcar_fel - finally MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: desmarcar_fel - finally MENSAJE: " + ex.toString();
            }
        }

        return resultado;
    }

    public String generar_archivo(String ambiente, String jsonSring) {
        String resultado;
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Dte_Lista>>() {
            }.getType();
            List<Dte_Lista> lista_documentos = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_fel_energia(ambiente);

            conn.setAutoCommit(false);

            // LISTADO FEL DEL ARCHIVO CARGA MASIVA.
            List<DTE_FEL> lista_dte_fel = new ArrayList<>();

            for (Integer i = 0; i < lista_documentos.size(); i++) {
                String cadenasql = "SELECT "
                        + "F.ID_DTE, "
                        + "F.TIPO_REGISTRO, "
                        + "F.FECHA_DOCUMENTO, "
                        + "F.ID_TIPO_DOCUMENTO, "
                        + "F.NIT_COMPRADOR, "
                        + "F.ID_CODIGO_MONEDA, "
                        + "F.TASA_CAMBIO, "
                        + "F.ORDEN_EXTERNO, "
                        + "F.ID_TIPO_VENTA, "
                        + "F.ID_DESTINO_VENTA, "
                        + "F.ENVIAR_CORREO, "
                        + "F.NOMBRE_COMPRADOR, "
                        + "F.DIRECCION, "
                        + "F.NUMERO_ACCESO, "
                        + "F.SERIE_ADMIN, "
                        + "F.NUMERO_ADMIN, "
                        + "D.AN8_CLIENTE_JDE, "
                        + "F.INCOTERM, "
                        + "F.CODIGO_EXPORTADOR, "
                        + "F.ID_TIPO_RECEPTOR "
                        + "FROM "
                        + "FEL_ENCABEZADO F "
                        + "LEFT JOIN FEL_DTE D ON (F.ID_DTE = D.ID_DTE) "
                        + "WHERE "
                        + "F.ID_DTE = " + lista_documentos.get(i).getId_dte();
                DTE_FEL_ENCABEZADO dte_fel_encabezado = null;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    dte_fel_encabezado = new DTE_FEL_ENCABEZADO(
                            rs.getLong(1),
                            rs.getInt(2),
                            rs.getLong(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getInt(6),
                            rs.getDouble(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getInt(10),
                            rs.getString(11),
                            rs.getString(12),
                            rs.getString(13),
                            rs.getLong(14),
                            rs.getString(15),
                            rs.getLong(16),
                            rs.getLong(17),
                            rs.getString(18),
                            rs.getString(19),
                            rs.getLong(20)
                    );
                }
                rs.close();
                stmt.close();

                cadenasql = "SELECT "
                        + "F.ID_DTE, "
                        + "F.TIPO_REGISTRO, "
                        + "F.ID_DETALLE, "
                        + "F.CANTIDAD, "
                        + "F.ID_UNIDAD_MEDIDA, "
                        + "F.PRECIO, "
                        + "F.PORCENTAJE_DESCUENTO, "
                        + "F.IMPORTE_DESCUENTO, "
                        + "F.IMPORTE_BRUTO, "
                        + "F.IMPORTE_EXENTO, "
                        + "F.IMPORTE_NETO, "
                        + "F.IMPORTE_IVA, "
                        + "F.IMPORTE_OTROS, "
                        + "F.IMPORTE_TOTAL, "
                        + "F.PRODUCTO, "
                        + "F.DESCRIPCION, "
                        + "F.ID_TIPO_VENTA "
                        + "FROM "
                        + "FEL_DETALLE F "
                        + "WHERE "
                        + "F.ID_DTE = " + lista_documentos.get(i).getId_dte();
                List<DTE_FEL_DETALLE> lista_dte_fel_detalle = new ArrayList<>();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    DTE_FEL_DETALLE dte_fel_detalle = new DTE_FEL_DETALLE(
                            rs.getLong(1),
                            rs.getInt(2),
                            rs.getLong(3),
                            rs.getDouble(4),
                            rs.getInt(5),
                            rs.getDouble(6),
                            rs.getDouble(7),
                            rs.getDouble(8),
                            rs.getDouble(9),
                            rs.getDouble(10),
                            rs.getDouble(11),
                            rs.getDouble(12),
                            rs.getDouble(13),
                            rs.getDouble(14),
                            rs.getString(15),
                            rs.getString(16),
                            rs.getString(17));
                    lista_dte_fel_detalle.add(dte_fel_detalle);
                }
                rs.close();
                stmt.close();

                cadenasql = "SELECT "
                        + "F.ID_DTE, "
                        + "F.TIPO_REGISTRO, "
                        + "F.ID_TIPO_DOCUMENTO, "
                        + "F.SERIE, "
                        + "F.NUMERO, "
                        + "F.FECHA_DOCUMENTO "
                        + "FROM "
                        + "FEL_ASOCIADOS F "
                        + "WHERE "
                        + "F.ID_DTE = " + lista_documentos.get(i).getId_dte();
                DTE_FEL_ASOCIADOS dte_fel_asociados = null;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    dte_fel_asociados = new DTE_FEL_ASOCIADOS(
                            rs.getLong(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getLong(5),
                            rs.getLong(6));
                }
                rs.close();
                stmt.close();

                cadenasql = "SELECT "
                        + "F.ID_DTE, "
                        + "F.TIPO_REGISTRO, "
                        + "F.IMPORTE_BRUTO, "
                        + "F.IMPORTE_DESCUENTO, "
                        + "F.IMPORTE_EXENTO, "
                        + "F.IMPORTE_NETO, "
                        + "F.IMPORTE_IVA, "
                        + "F.IMPORTE_OTROS, "
                        + "F.IMPORTE_TOTAL, "
                        + "F.PORCENTAJE_ISR, "
                        + "F.IMPORTE_ISR, "
                        + "F.REGISTROS_DETALLE, "
                        + "F.DOCUMENTOS_ASOCIADOS "
                        + "FROM "
                        + "FEL_TOTALES F "
                        + "WHERE "
                        + "F.ID_DTE = " + lista_documentos.get(i).getId_dte();
                DTE_FEL_TOTALES dte_fel_totales = null;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    dte_fel_totales = new DTE_FEL_TOTALES(
                            rs.getLong(1),
                            rs.getInt(2),
                            rs.getDouble(3),
                            rs.getDouble(4),
                            rs.getDouble(5),
                            rs.getDouble(6),
                            rs.getDouble(7),
                            rs.getDouble(8),
                            rs.getDouble(9),
                            rs.getDouble(10),
                            rs.getDouble(11),
                            rs.getInt(12),
                            rs.getInt(13));
                }
                rs.close();
                stmt.close();

                DTE_FEL dte_fel = new DTE_FEL(dte_fel_encabezado, lista_dte_fel_detalle, dte_fel_asociados, dte_fel_totales);
                lista_dte_fel.add(dte_fel);

                cadenasql = "UPDATE FEL_DTE SET ENVIADO='SI' WHERE ID_DTE = " + lista_documentos.get(i).getId_dte();
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = new Gson().toJson(lista_dte_fel);
        } catch (Exception ex) {
            try {
                conn.rollback();

                System.out.println("CLASE: " + this.getClass().getName() + " METODO: generar_archivo ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: generar_archivo ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: generar_archivo - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: generar_archivo - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: generar_archivo - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: generar_archivo - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String actualizar_descripcion_producto(String ambiente_app) {
        String resultado;
        String ambiente = "";
        String user = "";
        String pass = "";
        String db_user;
        String db_pass;
        
        Connection conn_jdenergia;
        Connection conn_gtfacturas;
        
        if(ambiente_app.equals("PY")) {
            db_user = "gtfacturas_demo";
            db_pass = "demo";
        } else {
            db_user = "gtfacturas";
            db_pass = "gtfactura5";
        }
        
        try {
            // REGISTRAR LAS OBSERVACIONES PARA LOS PRODUCTOS EN EL ESQUEMA DE GTFACTURAS.
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn_gtfacturas = DriverManager.getConnection("jdbc:oracle:thin:@//10.252.7.204:1521/unopetrol", db_user, db_pass);
            String cadenasql = "SELECT "
                    + "F.AMBIENTE, "
                    + "F.USUARIO, "
                    + "F.CONTRASENA "
                    + "FROM "
                    + "FEL_AMBIENTE F "
                    + "WHERE "
                    + "F.ACTIVO = 1";
            Statement stmt = conn_gtfacturas.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                ambiente = rs.getString(1);
                user = rs.getString(2);
                pass = rs.getString(3);
            }
            rs.close();
            stmt.close();
            if (conn_gtfacturas != null) {
                conn_gtfacturas.close();
            }

            // EXTRAER LAS OBSERVACIONES DE JDE.
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn_jdenergia = DriverManager.getConnection("jdbc:oracle:thin:@//10.252.9.1:1521/jdepdee1", user, pass);
            for (Integer i = 0; i < this.lista_detalle_fel_global.size(); i++) {
                cadenasql = "SELECT "
                        + "REPLACE(REGEXP_REPLACE(REPLACE(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(D.GDTXFT,DBMS_LOB.GETLENGTH(D.GDTXFT),1)),CHR(0),''), '<.+?>|(' || '&' || 'nbsp;)'), CHR(13) || CHR(10) ) DESCRIPCION "
                        + "FROM "
                        + ambiente + ".F00165 D "
                        + "WHERE "
                        + "TRIM(D.GDOBNM) = 'GT4211A' AND "
                        + "REPLACE(TRIM(D.GDTXKY),'.','') = '" + this.lista_detalle_fel_global.get(i).getDoco() + "|" + this.lista_detalle_fel_global.get(i).getDcto() + "|" + this.lista_detalle_fel_global.get(i).getKcoo() + "|" + this.lista_detalle_fel_global.get(i).getLnid() + "'";
                stmt = conn_jdenergia.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    String descripcion = rs.getString(1);
                    descripcion = descripcion.replaceAll(Character.toString('\0'), "");
                    descripcion = descripcion.replaceAll("<p style=\"\">", "");
                    descripcion = descripcion.replaceAll("<br/>", "");
                    descripcion = descripcion.replaceAll("<p>", "");
                    descripcion = descripcion.replaceAll("</p>", "");
                    descripcion = descripcion.replaceAll("\n", "");
                    descripcion = descripcion.replaceAll("\r", "");
                    descripcion = descripcion.replaceAll("\u00a0", "");
                    descripcion = descripcion.replaceAll("&" + "nbsp;", " ");
                    descripcion = descripcion.replaceAll(String.valueOf((char) 160), " ");
                    this.lista_detalle_fel_global.get(i).setDescripcion(descripcion.trim());
                }
                rs.close();
                stmt.close();
            }
            if (conn_jdenergia != null) {
                conn_jdenergia.close();
            }

            // REGISTRAR LAS OBSERVACIONES PARA LOS PRODUCTOS EN EL ESQUEMA DE GTFACTURAS.
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            conn_gtfacturas = DriverManager.getConnection("jdbc:oracle:thin:@//10.252.7.204:1521/unopetrol", db_user, db_pass);
            for (Integer i = 0; i < this.lista_detalle_fel_global.size(); i++) {
                cadenasql = "UPDATE "
                        + "FEL_DETALLE SET "
                        + "DESCRIPCION = '" + this.lista_detalle_fel_global.get(i).getDescripcion() + "' "
                        + "WHERE "
                        + "ID_DTE = " + this.lista_detalle_fel_global.get(i).getId_dte() + " AND "
                        + "TIPO_REGISTRO = " + this.lista_detalle_fel_global.get(i).getTipo_registro() + " AND "
                        + "ID_DETALLE = " + this.lista_detalle_fel_global.get(i).getId_detalle();
                stmt = conn_gtfacturas.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }
            if (conn_gtfacturas != null) {
                conn_gtfacturas.close();
            }

            resultado = "0♣DOCUMENTOS CARGADOS.";

        } catch (Exception ex) {
            System.out.println("1♣CLASE: " + this.getClass().getName() + " METODO: actualizar_descripcion_producto ERROR: " + ex.toString());
            resultado = "1♣CLASE: " + this.getClass().getName() + " METODO: actualizar_descripcion_producto ERROR: " + ex.toString();
        }

        return resultado;
    }

    public String fel_modificar_ambiente(String ambiente, Long id_ambiente, Integer valor) {
        String resultado = "";
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_fel_energia(ambiente);

            conn.setAutoCommit(false);

            String cadenasql = "UPDATE FEL_AMBIENTE SET ACTIVO=" + valor + " WHERE ID_AMBIENTE=" + id_ambiente;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            if (valor == 1) {
                valor = 0;
            } else {
                valor = 1;
            }

            if (id_ambiente == 1) {
                id_ambiente = Long.valueOf(2);
            } else {
                id_ambiente = Long.valueOf(1);
            }

            cadenasql = "UPDATE FEL_AMBIENTE SET ACTIVO=" + valor + " WHERE ID_AMBIENTE=" + id_ambiente;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = "0♣AMBIENTE JDE MODIFICADO.";

        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);

                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: fel_modificar_ambiente MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: fel_modificar_ambiente MENSAJE: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: fel_modificar_ambiente - Rollback MENSAJE: " + ex1.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: fel_modificar_ambiente - Rollback MENSAJE: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: fel_modificar_ambiente - finally MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: fel_modificar_ambiente - finally MENSAJE: " + ex.toString();
            }
        }

        return resultado;
    }

    public String documento_autorizar(String ambiente_conn, String path, String ambiente, String kcoo_param) {
        String resultado = "";
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_fel_energia(ambiente_conn);

            conn.setAutoCommit(false);

            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] linea_autorizacion = strLine.split("\\|");
                String tipo_documento = linea_autorizacion[0];
                String fecha_documento = linea_autorizacion[1];
                String serie_auth = linea_autorizacion[2];
                String preimpreso_auth = linea_autorizacion[3];
                String nit_comprador = linea_autorizacion[4];
                String importe_total = linea_autorizacion[5];
                String importe_iva = linea_autorizacion[6];
                String numero_autorizacion = linea_autorizacion[7];
                String orden_externo = linea_autorizacion[8];
                String nombre_cliente = linea_autorizacion[9];
                String direccion_cliente = linea_autorizacion[10];

                Long id_dte = null;
                String kcoo = "";
                String dcto = "";
                Long doco = null;
                String ambiente_jde = "";
                Integer an8 = 0;

                String cadenasql = "SELECT "
                        + "F.ID_DTE, "
                        + "F.KCOO_COMPANIA_JDE, "
                        + "F.DCTO_TIPO_ORDEN_JDE, "
                        + "F.DOCO_NO_ORDEN_JDE, "
                        + "F.AMBIENTE, "
                        + "F.AN8_CLIENTE_JDE "
                        + "FROM "
                        + "FEL_DTE F "
                        + "WHERE "
                        + "F.AMBIENTE='" + ambiente + "' AND "
                        + "F.KCOO_COMPANIA_JDE='" + kcoo_param + "' AND "
                        + "F.DOC_NO_DOCUMENTO_JDE = " + orden_externo;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    id_dte = rs.getLong(1);
                    kcoo = rs.getString(2);
                    dcto = rs.getString(3);
                    doco = rs.getLong(4);
                    ambiente_jde = rs.getString(5);
                    an8 = rs.getInt(6);
                }
                rs.close();
                stmt.close();

                cadenasql = "INSERT INTO FEL_GENERADO ("
                        + "ID_DTE, "
                        + "ID_TIPO_DOCUMENTO, "
                        + "FECHA_DOCUMENTO, "
                        + "SERIE, "
                        + "PREIMPRESO, "
                        + "NIT_COMPRADOR, "
                        + "IMPORTE_TOTAL, "
                        + "IMPORTE_IVA, "
                        + "NUMERO_AUTORIZACION, "
                        + "ORDEN_EXTERNO, "
                        + "NOMBRE, "
                        + "DIRECCION, "
                        + "ARCHIVO_GENERADO) VALUES ("
                        + id_dte + ",'"
                        + tipo_documento + "',"
                        + fecha_documento + ",'"
                        + serie_auth + "',"
                        + preimpreso_auth + ",'"
                        + nit_comprador + "',"
                        + importe_total + ","
                        + importe_iva + ",'"
                        + numero_autorizacion + "','"
                        + orden_externo + "','"
                        + nombre_cliente.replaceAll("'", "") + "','"
                        + direccion_cliente + "','"
                        + path + "')";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();

                cadenasql = "UPDATE FEL_DTE SET AUTORIZADO='SI' WHERE ID_DTE=" + id_dte;
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();

                Integer ivd = 0;
                cadenasql = "SELECT TO_NUMBER(SUBSTR(TO_CHAR(TO_DATE('" + fecha_documento + "','yyyyMMdd'),'ccYYddd'),2,6)) FROM DUAL";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    ivd = rs.getInt(1);
                }
                rs.close();
                stmt.close();

                Integer upmj = 0;
                cadenasql = "SELECT TO_NUMBER(SUBSTR(TO_CHAR(CURRENT_DATE,'ccYYddd'),2,6)) FROM DUAL";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    upmj = rs.getInt(1);
                }
                rs.close();
                stmt.close();

                cadenasql = "INSERT INTO " + ambiente_jde + ".F5542101@JDENERGIA ("
                        + "FEDOCO, "
                        + "FEDCTO, "
                        + "FEKCOO, "
                        + "FEDOC, "
                        + "FEAN8, "
                        + "FEIVD, "
                        + "FEAWDDOC, "
                        + "FER74108P, "
                        + "FEASMTGUID, "
                        + "FETORG, "
                        + "FEUSER, "
                        + "FEUPMJ) VALUES ('"
                        + doco + "','"
                        + dcto + "','"
                        + kcoo + "','"
                        + orden_externo + "','"
                        + an8 + "','"
                        + ivd + "','"
                        + serie_auth + "','"
                        + preimpreso_auth + "','"
                        + numero_autorizacion + "','"
                        + "ITAPP" + "','"
                        + "ITAPP" + "','"
                        + upmj + "')";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }
            br.close();
            fstream.close();

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = "0♣DOCUMENTOS AUTORIZADOS.";

        } catch (Exception ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);

                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: documento_autorizar MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: documento_autorizar MENSAJE: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: documento_autorizar - Rollback MENSAJE: " + ex1.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: documento_autorizar - Rollback MENSAJE: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("1♣ERROR CLASE: " + this.getClass().toString() + " METODO: documento_autorizar - finally MENSAJE: " + ex.toString());
                resultado = "1♣ERROR CLASE: " + this.getClass().toString() + " METODO: documento_autorizar - finally MENSAJE: " + ex.toString();
            }
        }

        return resultado;
    }

}
