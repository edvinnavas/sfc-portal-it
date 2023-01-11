package Bean.EcsaCrGoSocket;

import Entidad.Usuario;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import ClientRestService.Fcr_Servicio;
import java.util.List;

@Named(value = "Fcr_Dialog_Convert_Document")
@ViewScoped
public class Fcr_Dialog_Convert_Document implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Integer id_convert_document;
    private String sync_point;
    private String passwords;
    private String document_type;
    private Integer id_tde;
    private Integer id_documento;
    private String contenidotc;
    private String no_orden_e1;
    private String tipo_orden_e1;
    private String no_compania;
    private String fecha_documento;
    private String no_factura;
    private String tax_id_receptor;
    private String nombre_receptor;
    private String fecha_envio;
    private String process_result;
    private String procesado;
    private String aban8_d1;

    public void mostrar_dialog_convert_document(Integer id_convert_document, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_convert_document != null) {
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
                        + "C.ABAN8_E1 "
                        + "FROM "
                        + "CONVERT_DOCUMENT C "
                        + "WHERE "
                        + "C.ID_CONVERT_DOCUMENT=" + id_convert_document;

                Fcr_Servicio servicio = new Fcr_Servicio();
                List<String> resultado = servicio.reporte(cadenasql);

                Integer filas = resultado.size();
                Integer columnas = resultado.size();
                String[][] vector_result = new String[resultado.size()][columnas];
                for (Integer i = 0; i < resultado.size(); i++) {
                    for (Integer j = 0; j < resultado.size(); j++) {
                        vector_result[i][j] = resultado.get(j);
                    }
                }

                for (Integer i = 1; i < filas; i++) {
                    this.id_convert_document = Integer.valueOf(vector_result[i][0]);
                    this.sync_point = vector_result[i][1];
                    this.passwords = vector_result[i][2];
                    this.document_type = vector_result[i][3];
                    this.id_tde = Integer.valueOf(vector_result[i][4]);
                    this.id_documento = Integer.valueOf(vector_result[i][5]);
                    this.contenidotc = vector_result[i][6];
                    this.no_orden_e1 = vector_result[i][7];
                    this.tipo_orden_e1 = vector_result[i][8];
                    this.no_compania = vector_result[i][9];
                    this.fecha_documento = vector_result[i][10];
                    this.no_factura = vector_result[i][11];
                    this.tax_id_receptor = vector_result[i][12];
                    this.nombre_receptor = vector_result[i][13];
                    this.fecha_envio = vector_result[i][14];
                    this.process_result = vector_result[i][15];
                    this.procesado = vector_result[i][16];
                    this.aban8_d1 = vector_result[i][17];
                }

                PrimeFaces.current().executeScript("PF('varConvertDocument').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_CONVERT_DOCUMENT FCR_DIALOG_CONVERT_DOCUMENT: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_CONVERT_DOCUMENT FCR_DIALOG_CONVERT_DOCUMENT: " + ex.toString()));
        }
    }

    public Integer getId_convert_document() {
        return id_convert_document;
    }

    public void setId_convert_document(Integer id_convert_document) {
        this.id_convert_document = id_convert_document;
    }

    public String getSync_point() {
        return sync_point;
    }

    public void setSync_point(String sync_point) {
        this.sync_point = sync_point;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public Integer getId_tde() {
        return id_tde;
    }

    public void setId_tde(Integer id_tde) {
        this.id_tde = id_tde;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(Integer id_documento) {
        this.id_documento = id_documento;
    }

    public String getContenidotc() {
        return contenidotc;
    }

    public void setContenidotc(String contenidotc) {
        this.contenidotc = contenidotc;
    }

    public String getNo_orden_e1() {
        return no_orden_e1;
    }

    public void setNo_orden_e1(String no_orden_e1) {
        this.no_orden_e1 = no_orden_e1;
    }

    public String getTipo_orden_e1() {
        return tipo_orden_e1;
    }

    public void setTipo_orden_e1(String tipo_orden_e1) {
        this.tipo_orden_e1 = tipo_orden_e1;
    }

    public String getNo_compania() {
        return no_compania;
    }

    public void setNo_compania(String no_compania) {
        this.no_compania = no_compania;
    }

    public String getFecha_documento() {
        return fecha_documento;
    }

    public void setFecha_documento(String fecha_documento) {
        this.fecha_documento = fecha_documento;
    }

    public String getNo_factura() {
        return no_factura;
    }

    public void setNo_factura(String no_factura) {
        this.no_factura = no_factura;
    }

    public String getTax_id_receptor() {
        return tax_id_receptor;
    }

    public void setTax_id_receptor(String tax_id_receptor) {
        this.tax_id_receptor = tax_id_receptor;
    }

    public String getNombre_receptor() {
        return nombre_receptor;
    }

    public void setNombre_receptor(String nombre_receptor) {
        this.nombre_receptor = nombre_receptor;
    }

    public String getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(String fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public String getProcess_result() {
        return process_result;
    }

    public void setProcess_result(String process_result) {
        this.process_result = process_result;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

    public String getAban8_d1() {
        return aban8_d1;
    }

    public void setAban8_d1(String aban8_d1) {
        this.aban8_d1 = aban8_d1;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
