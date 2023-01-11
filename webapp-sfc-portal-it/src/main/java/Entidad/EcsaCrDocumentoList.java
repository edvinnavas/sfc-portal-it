package Entidad;

import java.io.Serializable;

public class EcsaCrDocumentoList implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String refacturacion;

    public EcsaCrDocumentoList(Integer id_convert_document, String sync_point, String passwords, String document_type, Integer id_tde, Integer id_documento, String contenidotc, String no_orden_e1, String tipo_orden_e1, String no_compania, String fecha_documento, String no_factura, String tax_id_receptor, String nombre_receptor, String fecha_envio, String process_result, String procesado, String refacturacion) {
        this.id_convert_document = id_convert_document;
        this.sync_point = sync_point;
        this.passwords = passwords;
        this.document_type = document_type;
        this.id_tde = id_tde;
        this.id_documento = id_documento;
        this.contenidotc = contenidotc;
        this.no_orden_e1 = no_orden_e1;
        this.tipo_orden_e1 = tipo_orden_e1;
        this.no_compania = no_compania;
        this.fecha_documento = fecha_documento;
        this.no_factura = no_factura;
        this.tax_id_receptor = tax_id_receptor;
        this.nombre_receptor = nombre_receptor;
        this.fecha_envio = fecha_envio;
        this.process_result = process_result;
        this.procesado = procesado;
        this.refacturacion = refacturacion;
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

    public String getRefacturacion() {
        return refacturacion;
    }

    public void setRefacturacion(String refacturacion) {
        this.refacturacion = refacturacion;
    }

}
