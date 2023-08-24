package Control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ctrl_FelCr implements Serializable {

    private static final long serialVersionUID = 1L;

    public String lista_dtes(String ambiente, String fecha) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);

            conn.setAutoCommit(false);

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            Date fecha_facturacion = dateFormat1.parse(fecha);

            Integer anio = fecha_facturacion.getYear() + 1900;
            Integer mes = fecha_facturacion.getMonth() + 1;
            Integer dia = fecha_facturacion.getDate();

            String anio_letra;
            if (anio < 10) {
                anio_letra = "0" + anio.toString();
            } else {
                anio_letra = anio.toString();
            }

            String mes_letra;
            if (mes < 10) {
                mes_letra = "0" + mes.toString();
            } else {
                mes_letra = mes.toString();
            }

            String dia_letra;
            if (dia < 10) {
                dia_letra = "0" + dia.toString();
            } else {
                dia_letra = dia.toString();
            }

            List<Entidad.RegTblDteCr> lista_reg_tbl_dte_cr = new ArrayList<>();
            String cadenasql = "SELECT "
                    + "C.ID_CONVERT_DOCUMENT, "
                    + "C.SYNC_POINT, "
                    + "C.PASSWORDS, "
                    + "C.ID_DOCUMENT_TYPE, "
                    + "C.ID_DTE, "
                    + "C.ID_DOCUMENTO, "
                    + "C.CONTENIDOTC, "
                    + "C.NO_ORDEN_E1, "
                    + "C.TIPO_ORDEN_E1, "
                    + "C.NO_COMPANIA, "
                    + "SUBSTR(C.FECHA_DOCUMENTO,7,2) || '/' || SUBSTR(C.FECHA_DOCUMENTO,5,2) || '/' || SUBSTR(C.FECHA_DOCUMENTO,0,4) || ' ' || SUBSTR(C.FECHA_DOCUMENTO,9,2) || ':' || SUBSTR(C.FECHA_DOCUMENTO,11,2) || ':' || SUBSTR(C.FECHA_DOCUMENTO,13,2) FECHA_DOCUMENTO, "
                    + "C.NO_FACTURA, "
                    + "C.TAX_ID_RECEPTOR, "
                    + "C.NOMBRE_RECEPTOR, "
                    + "SUBSTR(C.FECHA_ENVIO,7,2) || '/' || SUBSTR(C.FECHA_ENVIO,5,2) || '/' || SUBSTR(C.FECHA_ENVIO,0,4) || ' ' || SUBSTR(C.FECHA_ENVIO,9,2) || ':' || SUBSTR(C.FECHA_ENVIO,11,2) || ':' || SUBSTR(C.FECHA_ENVIO,13,2) FECHA_ENVIO, "
                    + "C.PROCESS_RESULT, "
                    + "C.PROCESADO, "
                    + "CASE WHEN R.ID_DOCUMENT_TYPE_REF=10 THEN 'SI' ELSE 'NO' END REFACTURACION "
                    + "FROM "
                    + "CONVERT_DOCUMENT C "
                    + "LEFT JOIN REFERENCIA R ON (C.ID_DOCUMENTO=R.ID_DOCUMENTO) "
                    + "WHERE "
                    + "SUBSTR(C.FECHA_DOCUMENTO,0,4) = '" + anio_letra + "' AND "
                    + "SUBSTR(C.FECHA_DOCUMENTO,5,2) = '" + mes_letra + "' AND "
                    + "SUBSTR(C.FECHA_DOCUMENTO,7,2) = '" + dia_letra + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Entidad.RegTblDteCr reg_tbl_dte_cr = new Entidad.RegTblDteCr();
                reg_tbl_dte_cr.setId_convert_document(rs.getInt(1));
                reg_tbl_dte_cr.setSync_point(rs.getString(2));
                reg_tbl_dte_cr.setPasswords(rs.getString(3));
                reg_tbl_dte_cr.setDocument_type(rs.getString(4));
                reg_tbl_dte_cr.setId_tde(rs.getInt(5));
                reg_tbl_dte_cr.setId_documento(rs.getInt(6));
                reg_tbl_dte_cr.setContenidotc(rs.getString(7));
                reg_tbl_dte_cr.setNo_orden_e1(rs.getString(8));
                reg_tbl_dte_cr.setTipo_orden_e1(rs.getString(9));
                reg_tbl_dte_cr.setNo_compania(rs.getString(10));
                reg_tbl_dte_cr.setFecha_documento(rs.getString(11));
                reg_tbl_dte_cr.setNo_factura(rs.getString(12));
                reg_tbl_dte_cr.setTax_id_receptor(rs.getString(13));
                reg_tbl_dte_cr.setNombre_receptor(rs.getString(14));
                reg_tbl_dte_cr.setFecha_envio(rs.getString(15));
                reg_tbl_dte_cr.setProcess_result(rs.getString(16));
                reg_tbl_dte_cr.setProcesado(rs.getString(17));
                reg_tbl_dte_cr.setRefacturacion(rs.getString(18));
                lista_reg_tbl_dte_cr.add(reg_tbl_dte_cr);
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(lista_reg_tbl_dte_cr);
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: lista_dtes(), ERRROR: " + ex.toString();
                }
            } catch (Exception ex1) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-lista_dtes(), ERRROR: " + ex.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-lista_dtes(), ERRROR: " + ex.toString();
            }
        }

        return resultado;
    }
    
    public String cargar_docs(String ambiente, String usuario, Integer anio, Integer mes, Integer dia, String tabla) {
        String resultado;

        try {
            Control.felcr.Control_Convert_Document controllerConverDocument = new Control.felcr.Control_Convert_Document();
            resultado = controllerConverDocument.Cargar_Convert_Document(ambiente, anio, mes, dia, tabla);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

    public String modificar_referencia(String ambiente, String usuario, Integer id_referencia, String no_documento_ref, String id_batch, String comentario_adjunto, Integer id_codigo_ref, String tipo_nota_credito) {
        String resultado;

        try {
            Control.felcr.Control_Referencia controllerReferencia = new Control.felcr.Control_Referencia();
            resultado = controllerReferencia.Modificar_Referencia(ambiente, id_referencia, no_documento_ref, id_batch, comentario_adjunto, id_codigo_ref, tipo_nota_credito);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

    public String anular_documento(String ambiente, String usuario, Integer id_convert_document, String refacturacion) {
        String resultado;

        try {
            Control.felcr.Control_Convert_Document controllerConverDocument = new Control.felcr.Control_Convert_Document();
            resultado = controllerConverDocument.Anular_documento(ambiente, usuario, id_convert_document, refacturacion);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

    public String re_facturar(String ambiente, String usuario, Integer id_convert_document, String tabla) {
        String resultado;

        try {
            Control.felcr.Control_Convert_Document controllerConverDocument = new Control.felcr.Control_Convert_Document();
            resultado = controllerConverDocument.re_facturar(ambiente, usuario, id_convert_document, tabla);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

    public String modificar_receptor(String ambiente, String usuario, Integer id_receptor, Integer id_tipo_contribuyente, String tax_id, String nombre_cliente, String direccion, String correo, String codigo_area) {
        String resultado;

        try {
            Control.felcr.Control_Receptor controllerReceptor = new Control.felcr.Control_Receptor();
            resultado = controllerReceptor.Modificar_Receptor(ambiente, id_receptor, id_tipo_contribuyente, tax_id, nombre_cliente, direccion, correo, codigo_area);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

    public String modificar_exoneracion(String ambiente, Integer id_exoneracion, Integer id_documento, String activo_exoneracion, Integer id_tipo_exoneracion, String num_doc, String fecha_emision, String nombre_institucion, Double porcentaje_exoneracion, Double monto_exoneracion) {
        String resultado;

        try {
            Control.felcr.Control_Exoneracion controllerExoneracion = new Control.felcr.Control_Exoneracion();
            resultado = controllerExoneracion.Modificar_Exoneracion(ambiente, id_exoneracion, id_documento, activo_exoneracion, id_tipo_exoneracion, num_doc, fecha_emision, nombre_institucion, porcentaje_exoneracion, monto_exoneracion);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

    public String modificar_otros_cargos(String ambiente, Integer id_otros_cargos, Integer id_documento, String activo_otros_cargos, Integer id_tipo_otros_cargos, String numero_identificacion, String razon_social, String descripcion, Double porcentaje_otros_cargos, Double monto_otros_cargos) {
        String resultado;

        try {
            Control.felcr.Control_Otros_Cargos controllerOtrosCargos = new Control.felcr.Control_Otros_Cargos();
            resultado = controllerOtrosCargos.Modificar_Otros_Cargos(ambiente, id_otros_cargos, id_documento, activo_otros_cargos, id_tipo_otros_cargos, numero_identificacion, razon_social, descripcion, porcentaje_otros_cargos, monto_otros_cargos);
        } catch (Exception ex) {
            resultado = "1," + ex.toString();
        }

        return resultado;
    }

}
