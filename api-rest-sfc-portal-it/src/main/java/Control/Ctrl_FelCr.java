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

            List<Entidad.felcr.RegTblDteCr> lista_reg_tbl_dte_cr = new ArrayList<>();
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
                Entidad.felcr.RegTblDteCr reg_tbl_dte_cr = new Entidad.felcr.RegTblDteCr();
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
    
    public String obtener_document(String ambiente, Long id_convert_document) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);

            conn.setAutoCommit(false);

            Entidad.felcr.RegDglDocument reg_dgl_document = new Entidad.felcr.RegDglDocument();

            String sql = "SELECT "
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
                    + "C.ABAN8_E1 "
                    + "FROM "
                    + "CONVERT_DOCUMENT C "
                    + "WHERE "
                    + "C.ID_CONVERT_DOCUMENT=" + id_convert_document;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                reg_dgl_document.setId_convert_document(rs.getLong(1));
                reg_dgl_document.setSync_point(rs.getString(2));
                reg_dgl_document.setPasswords(rs.getString(3));
                reg_dgl_document.setDocument_type(rs.getString(4));
                reg_dgl_document.setId_dte(rs.getLong(5));
                reg_dgl_document.setId_documento(rs.getLong(6));
                reg_dgl_document.setContenidotc(rs.getString(7));
                reg_dgl_document.setNo_orden_e1(rs.getString(8));
                reg_dgl_document.setTipo_orden_e1(rs.getString(9));
                reg_dgl_document.setNo_compania(rs.getString(10));
                reg_dgl_document.setFecha_documento(rs.getString(11));
                reg_dgl_document.setNo_factura(rs.getString(12));
                reg_dgl_document.setTax_id_receptor(rs.getString(13));
                reg_dgl_document.setNombre_receptor(rs.getString(14));
                reg_dgl_document.setFecha_envio(rs.getString(15));
                reg_dgl_document.setProcess_result(rs.getString(16));
                reg_dgl_document.setProcesado(rs.getString(17));
                reg_dgl_document.setAban8_d1(rs.getString(18));
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(reg_dgl_document);
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_document(), ERRROR: " + ex.toString();
                }
            } catch (Exception ex1) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-obtener_document(), ERRROR: " + ex.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-obtener_document(), ERRROR: " + ex.toString();
            }
        }

        return resultado;
    }
    
    public String obtener_referencia(String ambiente, Long id_document) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);

            conn.setAutoCommit(false);

            Entidad.felcr.RegDglReferencia reg_dgl_referencia = new Entidad.felcr.RegDglReferencia();

            String sql = "SELECT "
                    + "R.ID_REFERENCIA, "
                    + "R.ID_DOCUMENTO, "
                    + "R.ID_DOCUMENT_TYPE_REF, "
                    + "R.NO_DOCUMENTO_REF, "
                    + "R.ID_BATCH, "
                    + "R.FECHA_DOCUMENTO_REF, "
                    + "R.RAZONREF, "
                    + "R.ID_CODIGO_REF, "
                    + "R.COMENTARIO_ADJUNTO, "
                    + "R.TIPO_DESPACHO, "
                    + "R.TIPO_NOTA_CREDITO "
                    + "FROM "
                    + "REFERENCIA R "
                    + "WHERE "
                    + "R.ID_DOCUMENTO = " + id_document;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                reg_dgl_referencia.setId_referencia(rs.getLong(1));
                reg_dgl_referencia.setId_documento(rs.getLong(2));
                reg_dgl_referencia.setId_document_type_ref(rs.getLong(3));
                reg_dgl_referencia.setNo_documento_ref(rs.getString(4));
                reg_dgl_referencia.setId_batch(rs.getString(5));
                reg_dgl_referencia.setFecha_documento_ref(rs.getString(6));
                reg_dgl_referencia.setRazonref(rs.getString(7));
                reg_dgl_referencia.setId_codigo_ref(rs.getLong(8));
                reg_dgl_referencia.setComentario_adjunto(rs.getString(9));
                reg_dgl_referencia.setTipo_despacho(rs.getInt(10));
                reg_dgl_referencia.setTipo_nota_credito(rs.getString(11));
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(reg_dgl_referencia);
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_referencia(), ERRROR: " + ex.toString();
                }
            } catch (Exception ex1) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-obtener_referencia(), ERRROR: " + ex.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-obtener_referencia(), ERRROR: " + ex.toString();
            }
        }

        return resultado;
    }
    
    public String obtener_cat_codigo_ref(String ambiente) {
        String resultado = "";

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_felcr(ambiente);

            conn.setAutoCommit(false);

            List<Entidad.felcr.Cat_Codigo_Ref> lst_cat_codigo_ref = new ArrayList<>();

            String sql = "SELECT "
                    + "F.ID_CODIGO_REF, "
                    + "F.COD_CODIGO_REF, "
                    + "F.DESCRIPTION "
                    + "FROM "
                    + "CODIGO_REF F";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Entidad.felcr.Cat_Codigo_Ref cat_codigo_ref = new Entidad.felcr.Cat_Codigo_Ref();
                cat_codigo_ref.setID_CODIGO_REF(rs.getLong(1));
                cat_codigo_ref.setCOD_CODIGO_REF(rs.getString(2));
                cat_codigo_ref.setDESCRIPTION(rs.getString(3));
                lst_cat_codigo_ref.add(cat_codigo_ref);
            }
            rs.close();
            stmt.close();

            conn.commit();
            conn.setAutoCommit(true);

            Gson gson = new GsonBuilder().serializeNulls().create();
            resultado = gson.toJson(lst_cat_codigo_ref);
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_cat_codigo_ref(), ERRROR: " + ex.toString();
                }
            } catch (Exception ex1) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-obtener_cat_codigo_ref(), ERRROR: " + ex.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-obtener_cat_codigo_ref(), ERRROR: " + ex.toString();
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
