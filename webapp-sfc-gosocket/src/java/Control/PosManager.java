package Control;

import com.sisint.apifactmiscelanea.dao.Dao;
import com.sisint.apifactmiscelanea.daoImpl.DaoImpl;
import com.sisint.apifactmiscelanea.obj.DetalleGen;
import com.sisint.apifactmiscelanea.obj.FacturaGen;
import com.sisint.apifactmiscelanea.obj.MediosPagoGen;
import com.sisint.apifactmiscelanea.util.ConstantsApiFactMiscelanea;
import com.sisint.apijson.ConstantsJsonApi;
import com.sisint.apifactmiscelanea.obj.UnoSbExoneracion;
import com.sisint.apifactmiscelanea.obj.UnoSbOtherCharges;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosManager implements Serializable {

    private static final long serialVersionUID = 1L;

    public String saludo() {
        String resultado = "HOLA LOCO!!!";

        return resultado;
    }

    public String gosocket(String ambiente, Integer id_convert_document) {
        Control.Ctrl_Base_Datos ctrl_base_datos = new Control.Ctrl_Base_Datos();
        Connection conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);
        String resultado = "";

        try {
            System.out.println("******************** INICIO SERVICIO ENVIAR GOSOCKET ********************");
            resultado = "******************** INICIO SERVICIO ENVIAR GOSOCKET ********************\n";
            conn.setAutoCommit(false);

            // Dao d = new DaoImpl("http://facturacion.uno-terra.com/RestUnoFacturacionElec/", ConstantsJsonApi.contenTypeJSON);
            Dao d = new DaoImpl("http://10.253.7.250:9001/RestUnoFacturacionElec/services/", ConstantsJsonApi.contenTypeJSON);

            FacturaGen facturar = new FacturaGen();

            /* ============================================= CASA MATRIZ Y TERMINAL ========================================= */
            String casa_matriz = ctrl_base_datos.ObtenerString("SELECT "
                    + "(SELECT CAS.ID_CASA_MATRIZ FROM CASA_MATRIZ CAS WHERE CAS.COD_CASA_MATRIZ = SUBSTR(IDO.ESTABLECIMIENTO,0,3)) ID_CASA_MATRIZ "
                    + "FROM ID_DOC IDO WHERE IDO.ID_DOC IN ( "
                    + "SELECT ENC.ID_DOC FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);

            String terminal = ctrl_base_datos.ObtenerString("SELECT "
                    + "(SELECT TER.ID_TERMINAL FROM TERMINAL TER WHERE TER.COD_TERMINAL = SUBSTR(IDO.ESTABLECIMIENTO,4,5) AND TER.ID_CASA_MATRIZ = (SELECT CAS.ID_CASA_MATRIZ FROM CASA_MATRIZ CAS WHERE CAS.COD_CASA_MATRIZ = SUBSTR(IDO.ESTABLECIMIENTO,0,3))) ID_TERMINAL "
                    + "FROM ID_DOC IDO WHERE IDO.ID_DOC IN ( "
                    + "SELECT ENC.ID_DOC FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);

            String casa_matriz_final = "";
            String terminal_final = "";
            if (casa_matriz.trim().equals("1") && terminal.trim().equals("1")) {
                casa_matriz_final = "-2";
                terminal_final = "-2";
            }
            if (casa_matriz.trim().equals("2") && terminal.trim().equals("2")) {
                casa_matriz_final = "-3";
                terminal_final = "-3";
            }

            // Casa matriz -2 Central
            // Casa Matriz -2 Costa rica
            System.out.println("***** CASA MATRIZ: " + casa_matriz_final.trim());
            resultado = resultado + "***** CASA MATRIZ: " + casa_matriz_final.trim() + "\n";
            facturar.setCasaMatriz(casa_matriz_final.trim());

            // Casa matriz -3 Central
            // Casa Matriz -3 Costa rica
            System.out.println("***** TERMINAL: " + terminal_final.trim());
            resultado = resultado + "***** TERMINAL: " + terminal_final.trim() + "\n";
            facturar.setTerminal(terminal_final.trim());

            /* ============================================= TIPO DESPACHO ================================================== */
            Integer tipo_despacho = ctrl_base_datos.ObtenerEntero("SELECT R.TIPO_DESPACHO "
                    + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                    + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            // Tipo Despacho 1 gasolina
            // Tipo Despacho 2 lubricantes y tienda
            if (tipo_despacho == 3) {
                System.out.println("***** TIPO DESPACHO: " + 2);
                resultado = resultado + "***** TIPO DESPACHO: " + 2 + "\n";
                facturar.setTipoDespacho(BigInteger.valueOf(2));
            } else {
                System.out.println("***** TIPO DESPACHO: " + tipo_despacho);
                resultado = resultado + "***** TIPO DESPACHO: " + tipo_despacho + "\n";
                facturar.setTipoDespacho(BigInteger.valueOf(tipo_despacho));
            }

            /* ========================================== TIPO DOCUMENTO FISCAL ============================================= */
            String tipo_factura = ctrl_base_datos.ObtenerString("SELECT D.COD_DOCUMENT_TYPE CODIGO FROM CONVERT_DOCUMENT C LEFT JOIN DOCUMENT_TYPE D ON (C.ID_DOCUMENT_TYPE = D.ID_DOCUMENT_TYPE) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            // Tipo Despacho 01 Factura electrónica.
            // Tipo Despacho 02 Nota de débito electrónica.
            // Tipo Despacho 03 Nota de crébito electrónica.
            // Tipo Despacho 04 Tiquete electrónico.
            String codigo_pais = ctrl_base_datos.ObtenerString("SELECT "
                    + "REC.CODIGOPAIS CORREO "
                    + "FROM RECEPTOR REC WHERE REC.ID_RECEPTOR IN ( "
                    + "SELECT ENC.ID_RECEPTOR FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);
            if (!codigo_pais.trim().equals("506")) {
                tipo_factura = "09";
            }
            System.out.println("***** TIPO FACTURA: " + tipo_factura.trim());
            resultado = resultado + "***** TIPO FACTURA: " + tipo_factura.trim() + "\n";
            facturar.setTipoFactura(tipo_factura.trim());

            /* =========================================== FECHA Y HORA FACTURA ============================================= */
            String fecha_factura = ctrl_base_datos.ObtenerString("SELECT SUBSTR(C.FECHA_DOCUMENTO,0,4) || '/' || SUBSTR(C.FECHA_DOCUMENTO,5,2) || '/' || SUBSTR(C.FECHA_DOCUMENTO,7,2) FECHA_FACTURA FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date fecha_factura_d = dateFormat.parse(fecha_factura);
            fecha_factura_d.setHours(0);
            fecha_factura_d.setMinutes(0);
            fecha_factura_d.setSeconds(0);
            System.out.println("***** FECHA FACTURA: " + fecha_factura_d);
            resultado = resultado + "***** FECHA FACTURA: " + fecha_factura_d + "\n";
            facturar.setFechaFactura(fecha_factura_d);

            /* =========================================== CONDICION PAGO FACTURA =========================================== */
            String condicion_pago = ctrl_base_datos.ObtenerString("SELECT "
                    + "(SELECT C.COD_CONDICION_PAGO FROM CONDICION_PAGO C WHERE C.ID_CONDICION_PAGO IN (IDO.ID_CONDICION_PAGO)) CONDICION_PAGO "
                    + "FROM ID_DOC IDO WHERE IDO.ID_DOC IN ( "
                    + "SELECT ENC.ID_DOC FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);
            //Condicion de pago 01 Contado
            System.out.println("***** CONDICION_PAGO: " + condicion_pago.trim());
            resultado = resultado + "***** CONDICION_PAGO: " + condicion_pago.trim() + "\n";
            facturar.setCondicionPago(condicion_pago.trim());

            /* =========================================== DIAS CREDITO ===================================================== */
            String dias_credito = ctrl_base_datos.ObtenerString("SELECT "
                    + "IDO.TERMINOPAGOCDG DIAS_CREDITO "
                    + "FROM ID_DOC IDO WHERE IDO.ID_DOC IN ( "
                    + "SELECT ENC.ID_DOC FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);
            try {
                Integer.valueOf(dias_credito.trim());
            } catch (Exception ex) {
                dias_credito = "1";
            }
            System.out.println("***** DIASCREDITO: " + dias_credito);
            resultado = resultado + "***** DIASCREDITO: " + dias_credito + "\n";
            facturar.setDiasCredito(dias_credito);

            /* ============================================= TAX NUMBER FACTURA ============================================= */
            String tax_number_factura = ctrl_base_datos.ObtenerString("SELECT REPLACE(C.TAX_ID_RECEPTOR,'-','') TAX_NUMBER FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            System.out.println("***** TAX ID: " + tax_number_factura.trim());
            resultado = resultado + "***** TAX ID: " + tax_number_factura.trim() + "\n";
            facturar.setTaxNumber(tax_number_factura.trim());

            /* =========================================== NOMBRE RECEPTOR FACTURA ========================================== */
            String nombre_cliente_factura = ctrl_base_datos.ObtenerString("SELECT C.NOMBRE_RECEPTOR NOMBRE_CLIENTE FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            System.out.println("***** NOMBRE RECEPTOR: " + nombre_cliente_factura.trim());
            resultado = resultado + "***** NOMBRE RECEPTOR: " + nombre_cliente_factura.trim() + "\n";
            facturar.setNombreFiscal(nombre_cliente_factura.trim());

            /* =========================================== CORREO RECEPTOR FACTURA ========================================== */
            String correo_receptor = ctrl_base_datos.ObtenerString("SELECT "
                    + "REC.EMAIL CORREO "
                    + "FROM RECEPTOR REC WHERE REC.ID_RECEPTOR IN ( "
                    + "SELECT ENC.ID_RECEPTOR FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);
            //Correo Electronico **Opcional
            correo_receptor = correo_receptor.trim();
            if (correo_receptor.equals("-")) {
                correo_receptor = null;
            }
            System.out.println("***** CORREO RECEPTOR: " + correo_receptor);
            resultado = resultado + "***** CORREO RECEPTOR: " + correo_receptor + "\n";
            facturar.setCorreoElectronico(correo_receptor);

            /* =============================================== MONEDA FACTURA =============================================== */
            String moneda_factura = ctrl_base_datos.ObtenerString("SELECT "
                    + "TRIM(TOT.MONEDA) MONEDA "
                    + "FROM TOTALES TOT WHERE TOT.ID_TOTALES IN ( "
                    + "SELECT ENC.ID_TOTALES FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                    + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                    + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                    + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);
            System.out.println("***** MONEDA: " + moneda_factura.trim());
            resultado = resultado + "***** MONEDA: " + moneda_factura.trim() + "\n";
            facturar.setMoneda(moneda_factura.trim());

            /* =============================================== NUMERO DOCUMENTO INTERNO FACTURA ============================= */
            Integer no_interno_factura = ctrl_base_datos.ObtenerEntero("SELECT C.NO_FACTURA FROM CONVERT_DOCUMENT C WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            System.out.println("***** DOCUMENTO INTERNO: " + no_interno_factura);
            resultado = resultado + "***** DOCUMENTO INTERNO: " + no_interno_factura + "\n";
            facturar.setNumeroInterno(no_interno_factura);

            /* ============================================== DOCUMENTO DE REFERENCIA NC Y ND =============================== */
            String factura_referencia = ctrl_base_datos.ObtenerString("SELECT R.ID_BATCH "
                    + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                    + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            if (factura_referencia.equals("-")) {
                factura_referencia = ctrl_base_datos.ObtenerString("SELECT R.NO_DOCUMENTO_REF "
                        + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                        + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            }
            if (factura_referencia.equals("") || factura_referencia.equals(" ") || factura_referencia.equals("0")) {
                factura_referencia = null;
            }
            String refacturacion = ctrl_base_datos.ObtenerString("SELECT R.ID_DOCUMENT_TYPE_REF "
                    + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                    + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            // Tipo Despacho 01 Factura electrónica.
            // Tipo Despacho 02 Nota de débito electrónica.
            // Tipo Despacho 03 Nota de crébito electrónica.
            // Tipo Despacho 04 Tiquete electrónico.
            if (tipo_factura.trim().equals("01") || tipo_factura.trim().equals("09")) {
                if (refacturacion.trim().equals("10")) {
                    System.out.println("***** FACTURA ANULAR: " + factura_referencia.trim());
                    resultado = resultado + "***** FACTURA ANULAR: " + factura_referencia.trim() + "\n";
                    facturar.setFacturaAnular(factura_referencia);
                } else {
                    System.out.println("***** FACTURA ANULAR: " + null);
                    resultado = resultado + "***** FACTURA ANULAR: " + "null" + "\n";
                    facturar.setFacturaAnular(null);
                }
            } else {
                System.out.println("***** FACTURA ANULAR: " + factura_referencia.trim());
                resultado = resultado + "***** FACTURA ANULAR: " + factura_referencia.trim() + "\n";
                facturar.setFacturaAnular(factura_referencia);
            }

            /* ============================================== TEXTO DE OBSERVACION ADJUNTA ================================== */
            String comentarios = ctrl_base_datos.ObtenerString("SELECT R.COMENTARIO_ADJUNTO "
                    + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                    + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            if (comentarios.equals("-")) {
                comentarios = null;
                if (tipo_factura.equals("03") || tipo_factura.equals("02")) {
                    comentarios = "Anula documento " + no_interno_factura + ".";
                }
            }
            System.out.println("***** FACTURA COMENTARIOS: " + comentarios);
            resultado = resultado + "***** FACTURA COMENTARIOS: " + comentarios + "\n";
            facturar.setComentarios(comentarios);

            /* =========================================== TIPO CLIENTE ===================================================== */
            if (tipo_factura.equals("09")) {
                System.out.println("***** TIPO CLIENTE: " + ConstantsApiFactMiscelanea.TIPO_CLIENTE.EXTRANJERO.getCode());
                resultado = resultado + "***** TIPO CLIENTE: " + ConstantsApiFactMiscelanea.TIPO_CLIENTE.EXTRANJERO.getCode() + "\n";
                facturar.setTipoCliente(ConstantsApiFactMiscelanea.TIPO_CLIENTE.EXTRANJERO.getCode());
            } else {
                System.out.println("***** TIPO CLIENTE: " + ConstantsApiFactMiscelanea.TIPO_CLIENTE.CREDITO.getCode());
                resultado = resultado + "***** TIPO CLIENTE: " + ConstantsApiFactMiscelanea.TIPO_CLIENTE.CREDITO.getCode() + "\n";
                facturar.setTipoCliente(ConstantsApiFactMiscelanea.TIPO_CLIENTE.CREDITO.getCode());
            }

            /* =========================================== DIRECCION EXTRANJERO ============================================= */
            if (tipo_factura.equals("09")) {
                String direccion_extranjero = ctrl_base_datos.ObtenerString("SELECT "
                        + "REC.CALLE CALLE "
                        + "FROM RECEPTOR REC WHERE REC.ID_RECEPTOR IN ( "
                        + "SELECT ENC.ID_RECEPTOR FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                        + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                        + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                        + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + "))))", conn);

                System.out.println("***** DIRECCION EXTRANJERO: " + direccion_extranjero);
                resultado = resultado + "***** DIRECCION EXTRANJERO: " + direccion_extranjero + "\n";
                facturar.setDireccionExtranjero(direccion_extranjero);
            } else {
                System.out.println("***** DIRECCION EXTRANJERO: " + null);
                resultado = resultado + "***** DIRECCION EXTRANJERO: " + null + "\n";
                facturar.setDireccionExtranjero(null);
            }

            /* =========================================== CODIGO DE ACTIVIDAD ============================================== */
            String partida_arrancelaria = null;
            switch (tipo_despacho) {
                case 1: {
                    System.out.println("***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_COMBUS);
                    resultado = resultado + "***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_COMBUS + "\n";
                    facturar.setCodigoActividad(ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_COMBUS);
                    partida_arrancelaria = null;
                    break;
                }
                case 2: {
                    System.out.println("***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_LUBS);
                    resultado = resultado + "***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_LUBS + "\n";
                    facturar.setCodigoActividad(ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_LUBS);
                    if (tipo_factura.equals("09")) {
                        partida_arrancelaria = "271019910011"; // 2710.19.9111
                    } else {
                        partida_arrancelaria = null;
                    }
                    break;
                }
                case 3: {
                    System.out.println("***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_ARRENDA);
                    resultado = resultado + "***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_ARRENDA + "\n";
                    facturar.setCodigoActividad(ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_ARRENDA);
                    partida_arrancelaria = null;
                    break;
                }
                default: {
                    System.out.println("***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_LUBS);
                    resultado = resultado + "***** CODIGO DE ACTIVIDAD: " + ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_LUBS + "\n";
                    facturar.setCodigoActividad(ConstantsApiFactMiscelanea.FACEIDDOCODACTIVIDAD_LUBS);
                    if (tipo_factura.equals("09")) {
                        partida_arrancelaria = "271019910011"; // 2710.19.9111
                    } else {
                        partida_arrancelaria = null;
                    }
                    break;
                }
            }

            /* =========================================== TIPO DOC REF ===================================================== */
            Integer document_type_ref = ctrl_base_datos.ObtenerEntero("SELECT R.ID_DOCUMENT_TYPE_REF "
                    + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                    + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            String tipo_cod_ref = null;
            if (document_type_ref != 10) {
                tipo_cod_ref = null;
            } else {
                Integer codigo_ref = ctrl_base_datos.ObtenerEntero("SELECT R.ID_CODIGO_REF "
                        + "FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) "
                        + "WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                switch (codigo_ref) {
                    case 7: {
                        tipo_cod_ref = null;
                        break;
                    }
                    case 6: {
                        tipo_cod_ref = ConstantsApiFactMiscelanea.TIPO_DOC_REF.SUSTITUYE_FACTURA_RECHAZADA_POR_MINISTERIO_DE_HACIENDA.getCode();
                        break;
                    }
                    case 8: {
                        tipo_cod_ref = ConstantsApiFactMiscelanea.TIPO_DOC_REF.SUSTITUYE_FACTURA_RECHAZADA_POR_EL_RECEPTOR_DEL_COMPROBANTE.getCode();
                        break;
                    }
                    case 9: {
                        tipo_cod_ref = ConstantsApiFactMiscelanea.TIPO_DOC_REF.SUSTITUYE_FACTURA_DE_EXPORTACION.getCode();
                        break;
                    }
                    default: {
                        tipo_cod_ref = null;
                        break;
                    }
                }
            }
            System.out.println("***** TIPO DOC REF: " + tipo_cod_ref);
            resultado = resultado + "***** TIPO DOC REF: " + tipo_cod_ref + "\n";
            facturar.setTipoDocRef(tipo_cod_ref);

            /* =========================================== MEDIO DE PAGO FACTURA ============================================ */
            // Medios de Pago.: 01 Efectivo, 02 Tarjeta, 03 Cheque, 04 Transferencia ó Depósito Bancario, 05 Recaudado por Terceros ó 99 Otros.
            List<MediosPagoGen> mediosPago = new ArrayList();
            mediosPago.add(new MediosPagoGen("04"));
            System.out.println("***** MEDIOS PAGO GEN: 4");
            resultado = resultado + "***** MEDIOS PAGO GEN: 4" + "\n";
            facturar.setMediosPago(mediosPago);

            /* ============================================== DETALLE FACTURA =============================================== */
            // Detalles de factura.
            List<DetalleGen> detalle = new ArrayList();

            String cadenasql = "SELECT "
                    + "D.DSCITEM, "
                    + "D.VLRCODIGO, "
                    + "D.QTYITEM, "
                    + "D.PRCNETOITEM, "
                    + "D.IMPUESTO, "
                    + "D.EXENTO, "
                    + "D.CABYS "
                    + "FROM "
                    + "DETALLE D "
                    + "WHERE "
                    + "D.ID_DOCUMENTO IN (SELECT CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_convert_document + ")";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Integer linea = 1;
            while (rs.next()) {
                DetalleGen det = new DetalleGen();
                /* ============================================== NO. LINEA DETALLE FACTURA ================================= */
                System.out.println("********** LINEA: " + BigInteger.valueOf(linea));
                resultado = resultado + "********** LINEA: " + BigInteger.valueOf(linea) + "\n";
                det.setNoLinea(BigInteger.valueOf(linea));

                /* ============================================== DESCRIPCION PRODUCTO DETALLE FACTURA ====================== */
                System.out.println("********** DESCRIPCION: " + rs.getString(1).trim());
                resultado = resultado + "********** DESCRIPCION: " + rs.getString(1).trim() + "\n";
                det.setDescripcion(rs.getString(1).trim());

                /* ============================================== CODIGO PRODUCTO DETALLE FACTURA =========================== */
                System.out.println("********** CODIGO: " + rs.getString(2).trim());
                resultado = resultado + "********** CODIGO: " + rs.getString(2).trim() + "\n";
                det.setCodigo(rs.getString(2).trim());

                /* ============================================== DEPARTAMENTO PRODUCTO DETALLE FACTURA ===================== */
                if (tipo_despacho == 2) {
                    System.out.println("********** DEPARTAMENTO: " + "LUBRICANTES");
                    resultado = resultado + "********** DEPARTAMENTO: " + "LUBRICANTES" + "\n";
                    det.setDepartamento("LUBRICANTES");
                } else {
                    System.out.println("********** DEPARTAMENTO: " + "COMBUSTIBLES");
                    resultado = resultado + "********** DEPARTAMENTO: " + "COMBUSTIBLES" + "\n";
                    det.setDepartamento("COMBUSTIBLES");
                }

                /* ============================================== CANTIDAD PRODUCTO DETALLE FACTURA ========================= */
                Integer cantidad_numero = Integer.valueOf(rs.getString(3));
                if (cantidad_numero < 0) {
                    cantidad_numero = cantidad_numero * (-1);
                }
                System.out.println("********** CANTIDAD: " + BigDecimal.valueOf(cantidad_numero));
                resultado = resultado + "********** CANTIDAD: " + BigDecimal.valueOf(cantidad_numero) + "\n";
                det.setCantidad(BigDecimal.valueOf(cantidad_numero));

                /* ============================================== DESCUENTO PRODUCTO DETALLE FACTURA ======================== */
                System.out.println("********** DESCUENTO: " + BigDecimal.ZERO);
                resultado = resultado + "********** DESCUENTO: " + BigDecimal.ZERO + "\n";
                det.setDescuento(BigDecimal.ZERO);

                /* ============================================== UNIDAD MEDIDA PRODUCTO DETALLE FACTURA ==================== */
                System.out.println("********** UNIDAD MEDIDA: " + "Unid");
                resultado = resultado + "********** UNIDAD MEDIDA: " + "Unid" + "\n";
                det.setUnidadMedida("Unid");

                /* ============================================== TIPO PRODUCTO DETALLE FACTURA ============================= */
                // 13 si es lubricante - 0  si es petroleo
                System.out.println("********** TASA IMPUESTO: " + BigDecimal.valueOf(rs.getInt(5)));
                resultado = resultado + "********** TASA IMPUESTO: " + BigDecimal.valueOf(rs.getInt(5)) + "\n";
                det.setTasaimpuesto(BigDecimal.valueOf(rs.getInt(5)));

                /* ============================================== RECEPTOR EXENTO SI/NO ===================================== */
                System.out.println("********** EXENTO: " + rs.getString(6));
                resultado = resultado + "********** EXENTO: " + rs.getString(6) + "\n";
                // det.setExento(rs.getString(6));

                /* ============================================== PRECIO PRODUCTO DETALLE FACTURA =========================== */
                System.out.println("********** PRECIO: " + BigDecimal.valueOf(Double.parseDouble(rs.getString(4))));
                resultado = resultado + "********** PRECIO: " + BigDecimal.valueOf(Double.parseDouble(rs.getString(4))) + "\n";
                det.setPrecioNetoProducto(BigDecimal.valueOf(Double.parseDouble(rs.getString(4))));

                /* =========================================== PARTIDA ARANCELARIA ========================================== */
                System.out.println("********** PARTIDA ARANCELARIA: " + partida_arrancelaria);
                resultado = resultado + "********** PARTIDA ARANCELARIA: " + partida_arrancelaria + "\n";
                det.setPartidaArancelaria(partida_arrancelaria);

                /* =========================================== TIPO CODIGO PROCESO ========================================== */
                System.out.println("********** TIPO CODIGO PRODUCTO: " + ConstantsApiFactMiscelanea.TIPO_COD_PROD.CODIGO_USO_INTERNO.getCode());
                resultado = resultado + "********** TIPO CODIGO PRODUCTO: " + ConstantsApiFactMiscelanea.TIPO_COD_PROD.CODIGO_USO_INTERNO.getCode() + "\n";
                det.setTipoCodProducto(ConstantsApiFactMiscelanea.TIPO_COD_PROD.CODIGO_USO_INTERNO.getCode());

                /* =================================================== CABYS ================================================ */
                if (rs.getString(7).trim().length() == 13 && !rs.getString(7).trim().equals("0000000000000")) {
                    System.out.println("********** CABYS: " + rs.getString(7));
                    resultado = resultado + "********** CABYS: " + rs.getString(7) + "\n";
                    det.setNroCtaPredial(rs.getString(7));
                } else {
                    throw new Exception("Error valor CABYS.");
                }

                /* ================================== AGREGAMOS UNA O VARIAS LINEAS DE DETALLE ============================== */
                detalle.add(det);

                linea++;
            }
            rs.close();
            stmt.close();

            // ZETEO DETALLE
            facturar.setDetalle(detalle);

            String activo_exoneracion = ctrl_base_datos.ObtenerString("SELECT E.ACTIVO FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            System.out.println("********** ACTIVO_EXONERACION: " + activo_exoneracion);
            resultado = resultado + "********** ACTIVO_EXONERACION: " + activo_exoneracion + "\n";
            if (activo_exoneracion.equals("SI")) {
                UnoSbExoneracion exoneracion = new UnoSbExoneracion();

                /* =========================================== TIPO EXONERACION ===================================================== */
                Integer id_tipo_exoneracion = ctrl_base_datos.ObtenerEntero("SELECT E.ID_TIPO_EXONERACION FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                String tipo_exoneracion = "";
                switch (id_tipo_exoneracion) {
                    case 1: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.AUTORIZADO_POR_LA_LEY_ESPECIAL.getCode();
                        break;
                    }
                    case 2: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.COMPRAS_AUTORIZADAS.getCode();
                        break;
                    }
                    case 3: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.EXENCIONES_DIRECCION_ESPECIAL_HACIENDA.getCode();
                        break;
                    }
                    case 4: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.TRANSITORIO_IX.getCode();
                        break;
                    }
                    case 5: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.TRANSITORIO_V.getCode();
                        break;
                    }
                    case 6: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.TRANSITORIO_XVII.getCode();
                        break;
                    }
                    case 7: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.VENTAS_EXENTAS_A_DIPLOMATICOS.getCode();
                        break;
                    }
                    case 8: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.OTROS.getCode();
                        break;
                    }
                    default: {
                        tipo_exoneracion = ConstantsApiFactMiscelanea.TIPO_EXONERACION.OTROS.getCode();
                        break;
                    }
                }
                System.out.println("***** TIPO-EXONERACION: " + tipo_exoneracion);
                resultado = resultado + "***** TIPO-EXONERACION: " + tipo_exoneracion + "\n";
                exoneracion.setTipodoc(tipo_exoneracion);

                /* =========================================== NUMDOC =============================================================== */
                String num_doc_exoneracion = ctrl_base_datos.ObtenerString("SELECT E.NUM_DOC FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** NUMDOC: " + num_doc_exoneracion);
                resultado = resultado + "***** NUMDOC: " + num_doc_exoneracion + "\n";
                exoneracion.setNumdoc(num_doc_exoneracion);

                /* =========================================== FECHA_EMISION ======================================================== */
                String fecha_emision_exoneracion = ctrl_base_datos.ObtenerString("SELECT E.FECHA_EMISION FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                System.out.println("***** FECHA_EMISION: " + dataFormat.parse(fecha_emision_exoneracion));
                resultado = resultado + "***** FECHA_EMISION: " + dataFormat.parse(fecha_emision_exoneracion) + "\n";
                exoneracion.setFechaemision(dataFormat.parse(fecha_emision_exoneracion));

                /* =========================================== NOMBRE_INSTITUCION =================================================== */
                String nombre_institucion_exoneracion = ctrl_base_datos.ObtenerString("SELECT E.NOMBRE_INSTITUCION FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** NOMBRE_INSTITUCION: " + nombre_institucion_exoneracion);
                resultado = resultado + "***** NOMBRE_INSTITUCION: " + nombre_institucion_exoneracion + "\n";
                exoneracion.setNombreinstitucion(nombre_institucion_exoneracion);

                /* =========================================== PORCENTAJE_EXONERACION =============================================== */
                String porcentaje_exoneracion = ctrl_base_datos.ObtenerString("SELECT E.PORCENTAJE_EXONERACION FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** PORCENTAJE_EXONERACION: " + Short.valueOf(porcentaje_exoneracion));
                resultado = resultado + "***** PORCENTAJE_EXONERACION: " + Short.valueOf(porcentaje_exoneracion) + "\n";
                exoneracion.setPorcentajeexoneracion(Short.valueOf(porcentaje_exoneracion));

                /* =========================================== MONTO_EXONERACION =============================================== */
                String monto_exoneracion = ctrl_base_datos.ObtenerString("SELECT E.MONTO_EXONERACION FROM EXONERACION E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** MONTO_EXONERACION: " + BigDecimal.valueOf(Double.parseDouble(monto_exoneracion)));
                resultado = resultado + "***** MONTO_EXONERACION: " + BigDecimal.valueOf(Double.parseDouble(monto_exoneracion)) + "\n";
                exoneracion.setMontoexoneracion(BigDecimal.valueOf(Double.parseDouble(monto_exoneracion)));

                // ZETEO EXONERACION
                facturar.setUnoSbExoneracion(exoneracion);
            }

            String activo_otros_cargos = ctrl_base_datos.ObtenerString("SELECT E.ACTIVO FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
            System.out.println("********** ACTIVO_OTROS_CARGOS: " + activo_otros_cargos);
            resultado = resultado + "********** ACTIVO_OTROS_CARGOS: " + activo_otros_cargos + "\n";
            if (activo_otros_cargos.equals("SI")) {
                UnoSbOtherCharges otros_cargos = new UnoSbOtherCharges();
                /* =========================================== TIPO-OTROS-CARGOS ==================================================== */
                Integer id_tipo_otros_cargos = ctrl_base_datos.ObtenerEntero("SELECT E.ID_TIPO_OTROS_CARGOS FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                String tipo_otros_cargos = "";
                switch (id_tipo_otros_cargos) {
                    case 1: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.COBRO_DE_UN_TERCERO.getCode();
                        break;
                    }
                    case 2: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.CONTRIBUCION_PARAFISCAL.getCode();
                        break;
                    }
                    case 3: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.COSTOS_DE_EXPORTACION.getCode();
                        break;
                    }
                    case 4: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.IMPUESTO_DE_SERVICIO_10_PORCIENTO.getCode();
                        break;
                    }
                    case 5: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.TIMBRE_BOMBEROS.getCode();
                        break;
                    }
                    case 6: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.TIMBRE_CRUZ_ROJA.getCode();
                        break;
                    }
                    case 7: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.TIMBRE_DE_COLEGIOS_PROFESIONALES.getCode();
                        break;
                    }
                    case 8: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.OTROS.getCode();
                        break;
                    }
                    default: {
                        tipo_otros_cargos = ConstantsApiFactMiscelanea.TIPO_OTROS_CARGOS.OTROS.getCode();
                        break;
                    }
                }
                System.out.println("***** TIPO-OTROS-CARGOS: " + tipo_otros_cargos);
                resultado = resultado + "***** TIPO-OTROS-CARGOS: " + tipo_otros_cargos + "\n";
                otros_cargos.setDocumenttype(tipo_otros_cargos);

                /* =========================================== NUMERO_IDENTIFICACION ================================================ */
                String numero_identificacion_otros_cargos = ctrl_base_datos.ObtenerString("SELECT REPLACE(E.NUMERO_IDENTIFICACION,'-','') FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** NUMERO_IDENTIFICACION: " + numero_identificacion_otros_cargos);
                resultado = resultado + "***** NUMERO_IDENTIFICACION: " + numero_identificacion_otros_cargos + "\n";
                otros_cargos.setIdentitythird(numero_identificacion_otros_cargos);

                /* =========================================== RAZON_SOCIAL ========================================================= */
                String razon_social_otros_cargos = ctrl_base_datos.ObtenerString("SELECT E.RAZON_SOCIAL FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** RAZON_SOCIAL: " + razon_social_otros_cargos);
                resultado = resultado + "***** RAZON_SOCIAL: " + razon_social_otros_cargos + "\n";
                otros_cargos.setNamethird(razon_social_otros_cargos);

                /* =========================================== DESCRIPCION ========================================================== */
                String descripcion_otros_cargos = ctrl_base_datos.ObtenerString("SELECT E.DESCRIPCION FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** DESCRIPCION: " + descripcion_otros_cargos);
                resultado = resultado + "***** DESCRIPCION: " + descripcion_otros_cargos + "\n";
                otros_cargos.setDetail(descripcion_otros_cargos);

                /* =========================================== PORCENTAJE_OTROS_CARGOS ============================================== */
                String porcentaje_otros_cargos = ctrl_base_datos.ObtenerString("SELECT E.PORCENTAJE_OTROS_CARGOS FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** PORCENTAJE_OTROS_CARGOS: " + BigDecimal.valueOf(Double.parseDouble(porcentaje_otros_cargos)));
                resultado = resultado + "***** PORCENTAJE_OTROS_CARGOS: " + BigDecimal.valueOf(Double.parseDouble(porcentaje_otros_cargos)) + "\n";
                otros_cargos.setPercentage(BigDecimal.valueOf(Double.parseDouble(porcentaje_otros_cargos)));

                /* =========================================== MONTO_OTROS_CARGOS =================================================== */
                String monto_otros_cargos = ctrl_base_datos.ObtenerString("SELECT E.MONTO_OTROS_CARGOS FROM OTROS_CARGOS E LEFT JOIN CONVERT_DOCUMENT C ON (E.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                System.out.println("***** MONTO_OTROS_CARGOS: " + BigDecimal.valueOf(Double.parseDouble(monto_otros_cargos)));
                resultado = resultado + "***** MONTO_OTROS_CARGOS: " + BigDecimal.valueOf(Double.parseDouble(monto_otros_cargos)) + "\n";
                otros_cargos.setAmountcharge(BigDecimal.valueOf(Double.parseDouble(monto_otros_cargos)));

                // ZETEO OTROS_CARGOS
                List<UnoSbOtherCharges> lst_otros_cargos = new ArrayList<>();
                lst_otros_cargos.add(otros_cargos);
                facturar.setDscrcgglobal(lst_otros_cargos);
            }

            // ENVIA FACTURA GOSOCKET.
            if (tipo_factura.trim().equals("03")) {
                String tipo_nota_credito = ctrl_base_datos.ObtenerString("SELECT R.TIPO_NOTA_CREDITO FROM REFERENCIA R LEFT JOIN CONVERT_DOCUMENT C ON (R.ID_DOCUMENTO=C.ID_DOCUMENTO) WHERE C.ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                if (tipo_nota_credito.equals("TOTAL")) {
                    System.out.println("ENVIA NOTA DE CREDITO TOTAL.");
                    facturar = d.facturarMiscelanea(facturar);
                } else {
                    System.out.println("ENVIA NOTA DE CREDITO PARCIAL.");
                    facturar = d.notaCreditoParcial(facturar);
                }
            } else {
                System.out.println("ENVIA FACTURA NORMAL.");
                facturar = d.facturarMiscelanea(facturar);
            }

            System.out.println("********** TEST FACTURA: " + facturar);
            if (facturar != null) {
                System.out.println("********** TEST GET_FACTURA: " + facturar.getFactura());
                if (facturar.getFactura() != null) {
                    ctrl_base_datos.ExecuteStatementDB("UPDATE CONVERT_DOCUMENT SET PROCESADO='SI', PROCESS_RESULT='" + facturar.getFactura().trim() + "' WHERE ID_CONVERT_DOCUMENT=" + id_convert_document, conn);
                    resultado = "0,Documento fiscal procesado GOSOCKET: " + facturar.getFactura();
                } else {
                    resultado = "1," + facturar.getMsgError();
                }
            } else {
                resultado = "1,Documento fiscal no procesado GOSOCKET.";
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();

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
