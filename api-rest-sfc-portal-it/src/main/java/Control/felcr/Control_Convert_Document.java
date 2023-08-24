package Control.felcr;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Control_Convert_Document implements Serializable {

    private static final long serialVersionUID = 1L;

    public Control_Convert_Document() {

    }
    
    public String Cargar_Convert_Document(String ambiente, Integer anio, Integer mes, Integer dia, String tabla) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String anio_letra = "";
            if (anio < 10) {
                anio_letra = "0" + anio.toString();
            } else {
                anio_letra = anio.toString();
            }

            String mes_letra = "";
            if (mes < 10) {
                mes_letra = "0" + mes.toString();
            } else {
                mes_letra = mes.toString();
            }

            String dia_letra = "";
            if (dia < 10) {
                dia_letra = "0" + dia.toString();
            } else {
                dia_letra = dia.toString();
            }
            String fecha = dia_letra + "/" + mes_letra + "/" + anio_letra;

            String cadenasql = "SELECT TO_NUMBER(SUBSTR(TO_CHAR(TO_DATE('" + fecha + "','dd/MM/yyyy'),'ccYYddd'),2,6)) FECHA_JDE FROM DUAL";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            String fecha_jde = "";
            while (rs.next()) {
                fecha_jde = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT NVL(MAX(C.ID_CONVERT_DOCUMENT),0)+1 MAXIMO FROM CONVERT_DOCUMENT C";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            Integer maximo_convert_document = 0;
            while (rs.next()) {
                maximo_convert_document = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT DISTINCT A.SDDOCO, A.SDDCTO, A.SDKCOO, 1 SDLNID, A.SDIVD, A.SDDOC, A.SDSHAN "
                    + "FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " A "
                    + "WHERE "
                    + "(TRIM(A.SDCRMD) IS NULL) AND "
                    + "(A.SDIVD = " + fecha_jde + ") AND "
                    + "(A.SDDOC <> 0) AND "
                    + "(A.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) AND "
                    + "(A.SDDCTO IN ('S3','C3','SD')) AND "
                    + "(A.SDKCOO IN ('00850','00851','00852','00853','00854','00855','00856','00857')) AND "
                    + "(A.SDSHAN NOT IN (SELECT E.AN8_ESTACIONES_COCO FROM ESTACIONES_COCO E))";
            System.out.println("EXTRACCION DOCUMENTOS: " + cadenasql);

            List<Entidad.felcr.Convert_Document> lst_convert_document = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Integer id_document_type = 0;
                if (rs.getString(2).trim().equals("S3")) {
                    id_document_type = 1;
                }
                if (rs.getString(2).trim().equals("C3")) {
                    id_document_type = 3;
                }
                if (rs.getString(2).trim().equals("SD")) {
                    id_document_type = 2;
                }

                String semento_negocio = ctrl_base_datos.ObtenerString("SELECT T.ABAC05 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0101@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.ABAN8=" + rs.getString(7), conn);
                Integer casa_matriz = 0;
                Integer terminal = 0;
                if (semento_negocio.trim().toUpperCase().equals("LUB")) {
                    casa_matriz = 2;
                    terminal = 2;
                } else {
                    casa_matriz = 1;
                    terminal = 1;
                }

                String sql_contenido_tc = "SELECT "
                        + "E.CODIGOPAIS || TO_CHAR(CURRENT_DATE,'ddMMyy') || LPAD(E.IDEMISOR,12,0) || CM.COD_CASA_MATRIZ || T.COD_TERMINAL || D.COD_DOCUMENT_TYPE || LPAD(C.CORRELATIVO,10,0) || '1' || E.CODIGO_SEGURIDAD_PY CONTENIDO_TC "
                        + "FROM "
                        + "EMISOR E, "
                        + "CORRELATIVOS C "
                        + "LEFT JOIN TERMINAL T ON (C.ID_CASA_MATRIZ=T.ID_CASA_MATRIZ AND C.ID_TERMINAL=T.ID_TERMINAL) "
                        + "LEFT JOIN CASA_MATRIZ CM ON (T.ID_CASA_MATRIZ=CM.ID_CASA_MATRIZ) "
                        + "LEFT JOIN DOCUMENT_TYPE D ON (C.ID_DOCUMENT_TYPE=D.ID_DOCUMENT_TYPE) "
                        + "WHERE "
                        + "C.ID_DOCUMENT_TYPE=" + id_document_type + " AND C.ID_TERMINAL=" + terminal + " AND C.ID_CASA_MATRIZ=" + casa_matriz;

                Entidad.felcr.Convert_Document convert_document = new Entidad.felcr.Convert_Document(
                        maximo_convert_document,
                        "secret",
                        "secret",
                        id_document_type,
                        1, // ESTA VALOR SE ACTUALIZA AL FINAL DEL PROCESO.
                        1, // ESTA VALOR SE ACTUALIZA AL FINAL DEL PROCESO.
                        ctrl_base_datos.ObtenerString(sql_contenido_tc, conn),
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        anio_letra + mes_letra + dia_letra + "000000",
                        rs.getString(6),
                        rs.getString(7),
                        ctrl_base_datos.ObtenerString("SELECT T.ABTAX FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0101@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.ABAN8=" + rs.getString(7), conn),
                        ctrl_base_datos.ObtenerString("SELECT N.WWMLNM FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0111@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " N WHERE N.WWIDLN=0 AND N.WWAN8=" + rs.getString(7), conn),
                        anio_letra + mes_letra + dia_letra + "000000",
                        "DOCUMENTO PENDIENTE DE ENVIAR A GOSOCKET.",
                        "NO");

                ctrl_base_datos.ExecuteStatementDB("UPDATE CORRELATIVOS SET CORRELATIVO=CORRELATIVO+1 WHERE ID_DOCUMENT_TYPE=" + id_document_type + " AND ID_TERMINAL=" + terminal + " AND ID_CASA_MATRIZ=" + casa_matriz, conn);

                lst_convert_document.add(convert_document);
                maximo_convert_document++;   // INCREMENTA CONTADOR.
            }
            rs.close();
            stmt.close();

            for (Integer i = 0; i < lst_convert_document.size(); i++) {
                // INSTANCIA ID_DOC
                Control_I_Doc i_doc = new Control_I_Doc();
                Integer id_doc = i_doc.Carga_I_Doc(
                        ambiente,
                        lst_convert_document.get(i).getId_document_type(),
                        lst_convert_document.get(i).getNo_factura(),
                        1,
                        lst_convert_document.get(i).getContenidotc(),
                        lst_convert_document.get(i).getNo_orden_e1(),
                        conn,
                        tabla);
                System.out.println("ID_DOC: " + id_doc);

                // INSTANCIA ID_DOC_MEDIO_PAGO
                Control_Id_Doc_Medio_Pago id_doc_medio_pago = new Control_Id_Doc_Medio_Pago();
                id_doc_medio_pago.Carga_Id_Doc_Medio_Pago(id_doc, 4, conn);

                // INSTANCIA RECEPTOR
                Control_Receptor control_receptor = new Control_Receptor();
                Integer id_receptor = control_receptor.Carga_Receptor(
                        ambiente,
                        lst_convert_document.get(i).getTax_id_receptor(),
                        lst_convert_document.get(i).getAban8_e1(),
                        conn);
                System.out.println("ID_RECEPTOR: " + id_receptor);

                // INSTANCIA TOTALES
                Control_Totales control_totales = new Control_Totales();
                Integer id_totales = control_totales.Carga_Totales(
                        ambiente,
                        lst_convert_document.get(i).getNo_orden_e1(),
                        lst_convert_document.get(i).getAban8_e1(),
                        conn,
                        tabla);
                System.out.println("ID_TOTALES: " + id_totales);

                // INSTANCIA ENCABEZADO
                Control_Encabezado control_encabezado = new Control_Encabezado();
                Integer id_encabezado = control_encabezado.Carga_Encabezado(id_doc, 1, id_receptor, id_totales, conn);
                System.out.println("ID_ENCABEZADO: " + id_encabezado);

                // INSTANCIA DOCUMENTO
                Control_Documento control_documento = new Control_Documento();
                Integer id_documento = control_documento.Carga_Documento(id_encabezado, 1, conn);
                System.out.println("ID_TOTALES: " + id_documento);

                // INSTANCIA DTE
                Control_Dte control_dte = new Control_Dte();
                Integer id_dte = control_dte.Carga_Dte(id_documento, conn);
                System.out.println("ID_DTE: " + id_dte);

                // INSTANCIA DETALLE
                Control_Detalle control_detalle = new Control_Detalle();
                Integer id_detalle = control_detalle.Carga_Detalle(
                        ambiente,
                        id_documento,
                        lst_convert_document.get(i).getNo_orden_e1(),
                        conn,
                        tabla);
                System.out.println("ID_DETALLE: " + id_detalle);

                // INSTANCIA PERSONALIZADOS
                Control_Personalizados control_personalizados = new Control_Personalizados();
                Integer id_personalizados = control_personalizados.Carga_Personalizados(
                        ambiente,
                        id_dte,
                        id_documento,
                        lst_convert_document.get(i).getNo_factura(),
                        lst_convert_document.get(i).getAban8_e1(),
                        lst_convert_document.get(i).getNo_orden_e1(),
                        conn,
                        tabla);
                System.out.println("ID_PERSONALIZADOS: " + id_personalizados);

                // INSTANCIA REFERENCIA
                Control_Referencia control_Referencia = new Control_Referencia();
                Integer id_referencia = control_Referencia.Carga_Referencia(
                        ambiente, 
                        id_documento,
                        lst_convert_document.get(i).getNo_orden_e1(),
                        lst_convert_document.get(i).getTipo_orden_e1(),
                        lst_convert_document.get(i).getNo_compania(),
                        lst_convert_document.get(i).getAban8_e1(),
                        conn,
                        tabla);
                System.out.println("ID_REFERENCIA: " + id_referencia);

                // INSTANCIA EXONERACION
                String fecha_emision = anio.toString() + mes.toString() + dia.toString() + "000000";
                Control_Exoneracion control_Exoneracion = new Control_Exoneracion();
                Integer id_exoneracion = control_Exoneracion.Carga_Exoneracion(
                        id_documento,
                        "NO",
                        8,
                        "-",
                        fecha_emision,
                        lst_convert_document.get(i).getNombre_receptor(),
                        0.00,
                        0.00,
                        conn);
                System.out.println("ID_EXONERACION: " + id_exoneracion);

                // INSTANCIA OTROS CARGOS
                Control_Otros_Cargos control_Otros_Cargos = new Control_Otros_Cargos();
                Integer id_otros_cargos = control_Otros_Cargos.Carga_Otros_Cargos(
                        id_documento,
                        "NO",
                        8,
                        lst_convert_document.get(i).getTax_id_receptor(),
                        lst_convert_document.get(i).getNombre_receptor(),
                        "-",
                        0.00,
                        0.00,
                        conn);
                System.out.println("ID_OTROS_CARGOS: " + id_otros_cargos);

                cadenasql = "INSERT INTO CONVERT_DOCUMENT ( "
                        + "ID_CONVERT_DOCUMENT, "
                        + "SYNC_POINT, "
                        + "PASSWORDS, "
                        + "ID_DOCUMENT_TYPE, "
                        + "ID_DTE, "
                        + "ID_DOCUMENTO, "
                        + "CONTENIDOTC, "
                        + "NO_ORDEN_E1, "
                        + "TIPO_ORDEN_E1, "
                        + "NO_COMPANIA, "
                        + "FECHA_DOCUMENTO, "
                        + "NO_FACTURA, "
                        + "ABAN8_E1, "
                        + "TAX_ID_RECEPTOR, "
                        + "NOMBRE_RECEPTOR, "
                        + "FECHA_ENVIO, "
                        + "PROCESS_RESULT, "
                        + "PROCESADO) VALUES ('"
                        + lst_convert_document.get(i).getId_convert_document() + "','"
                        + lst_convert_document.get(i).getSync_point() + "','"
                        + lst_convert_document.get(i).getPasswords() + "','"
                        + lst_convert_document.get(i).getId_document_type() + "','"
                        + id_dte + "','"
                        + id_documento + "','"
                        + lst_convert_document.get(i).getContenidotc() + "','"
                        + lst_convert_document.get(i).getNo_orden_e1() + "','"
                        + lst_convert_document.get(i).getTipo_orden_e1() + "','"
                        + lst_convert_document.get(i).getNo_compania() + "','"
                        + lst_convert_document.get(i).getFecha_documento() + "','"
                        + lst_convert_document.get(i).getNo_factura() + "','"
                        + lst_convert_document.get(i).getAban8_e1() + "','"
                        + lst_convert_document.get(i).getTax_id_receptor() + "','"
                        + lst_convert_document.get(i).getNombre_receptor() + "','"
                        + lst_convert_document.get(i).getFecha_envio() + "','"
                        + lst_convert_document.get(i).getProcess_result() + "','"
                        + lst_convert_document.get(i).getProcesado() + "')";
                stmt = conn.createStatement();
                System.out.println("INSERT CONVERT_DOCUMENT: " + cadenasql);
                stmt.executeUpdate(cadenasql);
                stmt.close();

                // String no_orden_e1 = driver.getStringDB("SELECT C.NO_ORDEN_E1 FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document);
                System.out.println("UPDATE F42119: " + "UPDATE " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " SET SDCRMD='4' WHERE SDDOCO=" + lst_convert_document.get(i).getNo_orden_e1());
                ctrl_base_datos.ExecuteStatementDB("UPDATE " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " SET SDCRMD='4' WHERE SDDOCO=" + lst_convert_document.get(i).getNo_orden_e1(), conn);
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Documentos fiscales cargados.";

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
    
    public String Anular_documento(String ambiente, String usuario, Integer id_convert_document, String refacturacion) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String kcoo = "0";
            String doco = "0";
            String dcto = "0";
            String doc = "0";
            String fecha_documento = "00000000000000";
            String cadenasql = "SELECT "
                    + "C.NO_COMPANIA KCOO, "
                    + "C.NO_ORDEN_E1 DOCO, "
                    + "C.TIPO_ORDEN_E1 DCTO, "
                    + "C.NO_FACTURA DOC, "
                    + "C.FECHA_DOCUMENTO "
                    + "FROM "
                    + "CONVERT_DOCUMENT C "
                    + "WHERE "
                    + "C.ID_CONVERT_DOCUMENT=" + id_convert_document;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                kcoo = rs.getString(1);
                doco = rs.getString(2);
                dcto = rs.getString(3);
                doc = rs.getString(4);
                fecha_documento = rs.getString(5);
            }
            rs.close();
            stmt.close();

            String nueva_fecha_documento = "2017" + fecha_documento.substring(4, fecha_documento.length());
            cadenasql = "UPDATE CONVERT_DOCUMENT SET "
                    + "FECHA_DOCUMENTO='" + nueva_fecha_documento + "', PROCESADO='NO' "
                    + "WHERE ID_CONVERT_DOCUMENT=" + id_convert_document;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            if (refacturacion.equals("NO")) {
                cadenasql = "UPDATE " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F4211@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " "
                        + "SET SDCRMD=NULL "
                        + "WHERE "
                        + "SDKCOO='" + kcoo + "' AND "
                        + "SDDOCO=" + doco + " AND "
                        + "SDDCTO='" + dcto + "' AND "
                        + "SDDOC=" + doc;
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();

                cadenasql = "UPDATE " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F42119@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " "
                        + "SET SDCRMD=NULL "
                        + "WHERE "
                        + "SDKCOO='" + kcoo + "' AND "
                        + "SDDOCO=" + doco + " AND "
                        + "SDDCTO='" + dcto + "' AND "
                        + "SDDOC=" + doc;
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Factura anulada.";

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

    public String re_facturar(String ambiente, String usuario, Integer id_convert_document, String tabla) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            conn.setAutoCommit(false);

            String cadenasql = "SELECT C.NO_ORDEN_E1, C.FECHA_DOCUMENTO, C.FECHA_ENVIO FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer doco = 0;
            String fecha_documento = "";
            String fecha_envio = "";
            while (rs.next()) {
                doco = rs.getInt(1);
                fecha_documento = rs.getString(2);
                fecha_envio = rs.getString(3);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT NVL(MAX(C.ID_CONVERT_DOCUMENT),0)+1 MAXIMO FROM CONVERT_DOCUMENT C";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            Integer maximo_convert_document = 0;
            while (rs.next()) {
                maximo_convert_document = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "SELECT DISTINCT A.SDDOCO, A.SDDCTO, A.SDKCOO, 1 SDLNID, A.SDIVD, A.SDDOC, A.SDSHAN "
                    + "FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " A "
                    + "WHERE "
                    + "(A.SDDOCO = " + doco + ") AND "
                    + "(A.SDDOC <> 0) AND "
                    + "(A.SDLTTR NOT IN (980, 900, 902, 904, 909, 984)) AND "
                    + "(A.SDDCTO IN ('S3','C3','SD')) AND "
                    + "(A.SDKCOO IN ('00850','00851','00852','00853','00854','00855','00856','00857')) AND "
                    + "(A.SDSHAN NOT IN (SELECT E.AN8_ESTACIONES_COCO FROM ESTACIONES_COCO E))";
            System.out.println("EXTRACCION DOCUMENTOS: " + cadenasql);

            List<Entidad.felcr.Convert_Document> lst_convert_document = new ArrayList<>();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Integer id_document_type = 0;
                if (rs.getString(2).trim().equals("S3")) {
                    id_document_type = 1;
                }
                if (rs.getString(2).trim().equals("C3")) {
                    id_document_type = 3;
                }
                if (rs.getString(2).trim().equals("SD")) {
                    id_document_type = 2;
                }

                String semento_negocio = ctrl_base_datos.ObtenerString("SELECT T.ABAC05 FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0101@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.ABAN8=" + rs.getString(7), conn);
                Integer casa_matriz = 0;
                Integer terminal = 0;
                if (semento_negocio.trim().toUpperCase().equals("LUB")) {
                    casa_matriz = 2;
                    terminal = 2;
                } else {
                    casa_matriz = 1;
                    terminal = 1;
                }

                String sql_contenido_tc = "SELECT "
                        + "E.CODIGOPAIS || TO_CHAR(CURRENT_DATE,'ddMMyy') || LPAD(E.IDEMISOR,12,0) || CM.COD_CASA_MATRIZ || T.COD_TERMINAL || D.COD_DOCUMENT_TYPE || LPAD(C.CORRELATIVO,10,0) || '1' || E.CODIGO_SEGURIDAD_PY CONTENIDO_TC "
                        + "FROM "
                        + "EMISOR E, "
                        + "CORRELATIVOS C "
                        + "LEFT JOIN TERMINAL T ON (C.ID_CASA_MATRIZ=T.ID_CASA_MATRIZ AND C.ID_TERMINAL=T.ID_TERMINAL) "
                        + "LEFT JOIN CASA_MATRIZ CM ON (T.ID_CASA_MATRIZ=CM.ID_CASA_MATRIZ) "
                        + "LEFT JOIN DOCUMENT_TYPE D ON (C.ID_DOCUMENT_TYPE=D.ID_DOCUMENT_TYPE) "
                        + "WHERE "
                        + "C.ID_DOCUMENT_TYPE=" + id_document_type + " AND C.ID_TERMINAL=" + terminal + " AND C.ID_CASA_MATRIZ=" + casa_matriz;

                Entidad.felcr.Convert_Document convert_document = new Entidad.felcr.Convert_Document(
                        maximo_convert_document,
                        "secret",
                        "secret",
                        id_document_type,
                        1, // ESTA VALOR SE ACTUALIZA AL FINAL DEL PROCESO.
                        1, // ESTA VALOR SE ACTUALIZA AL FINAL DEL PROCESO.
                        ctrl_base_datos.ObtenerString(sql_contenido_tc, conn),
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        fecha_documento,
                        rs.getString(6),
                        rs.getString(7),
                        ctrl_base_datos.ObtenerString("SELECT T.ABTAX FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0101@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " T WHERE T.ABAN8=" + rs.getString(7), conn),
                        ctrl_base_datos.ObtenerString("SELECT N.WWMLNM FROM " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + ".F0111@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " N WHERE N.WWIDLN=0 AND N.WWAN8=" + rs.getString(7), conn),
                        fecha_envio,
                        "DOCUMENTO PENDIENTE DE ENVIAR A GOSOCKET.",
                        "NO");

                ctrl_base_datos.ExecuteStatementDB("UPDATE CORRELATIVOS SET CORRELATIVO=CORRELATIVO+1 WHERE ID_DOCUMENT_TYPE=" + id_document_type + " AND ID_TERMINAL=" + terminal + " AND ID_CASA_MATRIZ=" + casa_matriz, conn);

                lst_convert_document.add(convert_document);
                maximo_convert_document++;   // INCREMENTA CONTADOR.
            }
            rs.close();
            stmt.close();

            for (Integer i = 0; i < lst_convert_document.size(); i++) {
                // INSTANCIA ID_DOC
                Control_I_Doc i_doc = new Control_I_Doc();
                Integer id_doc = i_doc.Carga_I_Doc(
                        ambiente,
                        lst_convert_document.get(i).getId_document_type(),
                        lst_convert_document.get(i).getNo_factura(),
                        1,
                        lst_convert_document.get(i).getContenidotc(),
                        lst_convert_document.get(i).getNo_orden_e1(),
                        conn,
                        tabla);
                System.out.println("ID_DOC: " + id_doc);

                // INSTANCIA ID_DOC_MEDIO_PAGO
                Control_Id_Doc_Medio_Pago id_doc_medio_pago = new Control_Id_Doc_Medio_Pago();
                id_doc_medio_pago.Carga_Id_Doc_Medio_Pago(id_doc, 4, conn);

                // INSTANCIA RECEPTOR
                Control_Receptor control_receptor = new Control_Receptor();
                Integer id_receptor = control_receptor.Carga_Receptor(
                        ambiente,
                        lst_convert_document.get(i).getTax_id_receptor(),
                        lst_convert_document.get(i).getAban8_e1(),
                        conn);
                System.out.println("ID_RECEPTOR: " + id_receptor);

                // INSTANCIA TOTALES
                Control_Totales control_totales = new Control_Totales();
                Integer id_totales = control_totales.Carga_Totales(
                        ambiente,
                        lst_convert_document.get(i).getNo_orden_e1(),
                        lst_convert_document.get(i).getAban8_e1(),
                        conn,
                        tabla);
                System.out.println("ID_TOTALES: " + id_totales);

                // INSTANCIA ENCABEZADO
                Control_Encabezado control_encabezado = new Control_Encabezado();
                Integer id_encabezado = control_encabezado.Carga_Encabezado(id_doc, 1, id_receptor, id_totales, conn);
                System.out.println("ID_ENCABEZADO: " + id_encabezado);

                // INSTANCIA DOCUMENTO
                Control_Documento control_documento = new Control_Documento();
                Integer id_documento = control_documento.Carga_Documento(id_encabezado, 1, conn);
                System.out.println("ID_TOTALES: " + id_documento);

                // INSTANCIA DTE
                Control_Dte control_dte = new Control_Dte();
                Integer id_dte = control_dte.Carga_Dte(id_documento, conn);
                System.out.println("ID_DTE: " + id_dte);

                // INSTANCIA DETALLE
                Control_Detalle control_detalle = new Control_Detalle();
                Integer id_detalle = control_detalle.Carga_Detalle(
                        ambiente,
                        id_documento,
                        lst_convert_document.get(i).getNo_orden_e1(),
                        conn,
                        tabla);
                System.out.println("ID_DETALLE: " + id_detalle);

                // INSTANCIA PERSONALIZADOS
                Control_Personalizados control_personalizados = new Control_Personalizados();
                Integer id_personalizados = control_personalizados.Carga_Personalizados(
                        ambiente,
                        id_dte,
                        id_documento,
                        lst_convert_document.get(i).getNo_factura(),
                        lst_convert_document.get(i).getAban8_e1(),
                        lst_convert_document.get(i).getNo_orden_e1(),
                        conn,
                        tabla);
                System.out.println("ID_PERSONALIZADOS: " + id_personalizados);

                // INSTANCIA REFERENCIA
                Control_Referencia control_Referencia = new Control_Referencia();
                Integer id_referencia = control_Referencia.Carga_Referencia_Refacturacion(
                        ambiente,
                        id_documento,
                        lst_convert_document.get(i).getNo_orden_e1(),
                        lst_convert_document.get(i).getTipo_orden_e1(),
                        lst_convert_document.get(i).getNo_compania(),
                        lst_convert_document.get(i).getAban8_e1(),
                        conn,
                        tabla,
                        id_convert_document);
                System.out.println("ID_REFERENCIA: " + id_referencia);

                // INSTANCIA EXONERACION
                Control_Exoneracion control_Exoneracion = new Control_Exoneracion();
                Integer id_exoneracion = control_Exoneracion.Carga_Exoneracion(
                        id_documento,
                        "NO",
                        8,
                        "-",
                        fecha_envio,
                        lst_convert_document.get(i).getNombre_receptor(),
                        0.00,
                        0.00,
                        conn);
                System.out.println("ID_EXONERACION: " + id_exoneracion);

                // INSTANCIA OTROS CARGOS
                Control_Otros_Cargos control_Otros_Cargos = new Control_Otros_Cargos();
                Integer id_otros_cargos = control_Otros_Cargos.Carga_Otros_Cargos(
                        id_documento,
                        "NO",
                        8,
                        lst_convert_document.get(i).getTax_id_receptor(),
                        lst_convert_document.get(i).getNombre_receptor(),
                        "-",
                        0.00,
                        0.00,
                        conn);
                System.out.println("ID_OTROS_CARGOS: " + id_otros_cargos);

                cadenasql = "INSERT INTO CONVERT_DOCUMENT ( "
                        + "ID_CONVERT_DOCUMENT, "
                        + "SYNC_POINT, "
                        + "PASSWORDS, "
                        + "ID_DOCUMENT_TYPE, "
                        + "ID_DTE, "
                        + "ID_DOCUMENTO, "
                        + "CONTENIDOTC, "
                        + "NO_ORDEN_E1, "
                        + "TIPO_ORDEN_E1, "
                        + "NO_COMPANIA, "
                        + "FECHA_DOCUMENTO, "
                        + "NO_FACTURA, "
                        + "ABAN8_E1, "
                        + "TAX_ID_RECEPTOR, "
                        + "NOMBRE_RECEPTOR, "
                        + "FECHA_ENVIO, "
                        + "PROCESS_RESULT, "
                        + "PROCESADO) VALUES ('"
                        + lst_convert_document.get(i).getId_convert_document() + "','"
                        + lst_convert_document.get(i).getSync_point() + "','"
                        + lst_convert_document.get(i).getPasswords() + "','"
                        + lst_convert_document.get(i).getId_document_type() + "','"
                        + id_dte + "','"
                        + id_documento + "','"
                        + lst_convert_document.get(i).getContenidotc() + "','"
                        + lst_convert_document.get(i).getNo_orden_e1() + "','"
                        + lst_convert_document.get(i).getTipo_orden_e1() + "','"
                        + lst_convert_document.get(i).getNo_compania() + "','"
                        + lst_convert_document.get(i).getFecha_documento() + "','"
                        + lst_convert_document.get(i).getNo_factura() + "','"
                        + lst_convert_document.get(i).getAban8_e1() + "','"
                        + lst_convert_document.get(i).getTax_id_receptor() + "','"
                        + lst_convert_document.get(i).getNombre_receptor() + "','"
                        + lst_convert_document.get(i).getFecha_envio() + "','"
                        + lst_convert_document.get(i).getProcess_result() + "','"
                        + lst_convert_document.get(i).getProcesado() + "')";
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();

                // String no_orden_e1 = driver.getStringDB("SELECT C.NO_ORDEN_E1 FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document);
                ctrl_base_datos.ExecuteStatementDB("UPDATE " + ctrl_base_datos.AmbienteEsquemaJde(ambiente) + "." + tabla + "@" + ctrl_base_datos.AmbienteDBLinkJde(ambiente) + " SET SDCRMD='4' WHERE SDDOCO=" + lst_convert_document.get(i).getNo_orden_e1(), conn);
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

            resultado = "0,Re-facturación habilitada.";

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
