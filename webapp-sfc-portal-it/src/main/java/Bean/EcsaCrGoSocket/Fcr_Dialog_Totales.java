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

@Named(value = "Fcr_Dialog_Totales")
@ViewScoped
public class Fcr_Dialog_Totales implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Integer id_totales;
    private String moneda;
    private Double tipo_cambio;
    private Double subtotal;
    private Double total_descuento;
    private Double total_venta_neta;
    private Double total_exento;
    private Double total_impuesto;
    private Double total_venta;
    private Double total_comprobante;
    private Double total_servicios_gravados;
    private Double total_servicios_exentos;
    private Double total_mercaderia_gravados;
    private Double total_mercaderia_exentos;

    public void mostrar_dialog_totales(Integer id_document, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_document != null) {
                String cadenasql = "SELECT "
                        + "TOT.ID_TOTALES, "
                        + "TOT.MONEDA, "
                        + "TOT.FCTCONV, "
                        + "TOT.SUBTOTAL, "
                        + "TOT.MNTDCTO, "
                        + "TOT.MNTBASE, "
                        + "TOT.MNTEXE, "
                        + "TOT.MNTIMP, "
                        + "TOT.SALDOANTERIOR, "
                        + "TOT.VLRPAGAR, "
                        + "TOT.MONTOCONCEPTO1, "
                        + "TOT.MONTOCONCEPTO2, "
                        + "TOT.MONTOCONCEPTO3, "
                        + "TOT.MONTOCONCEPTO4 "
                        + "FROM TOTALES TOT WHERE TOT.ID_TOTALES IN ( "
                        + "SELECT ENC.ID_TOTALES FROM ENCABEZADO ENC WHERE ENC.ID_ENCABEZADO IN ( "
                        + "SELECT DOC.ID_ENCABEZADO FROM DOCUMENTO DOC WHERE DOC.ID_DOCUMENTO IN ( "
                        + "SELECT DTE.ID_DOCUMENTO FROM DTE DTE WHERE (DTE.ID_DTE, DTE.ID_DOCUMENTO) IN ( "
                        + "SELECT CON.ID_DTE, CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_document + "))))";

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
                    this.id_totales = Integer.valueOf(vector_result[i][0]);
                    this.moneda = vector_result[i][1];
                    this.tipo_cambio = Double.valueOf(vector_result[i][2]);
                    this.subtotal = Double.valueOf(vector_result[i][3]);
                    this.total_descuento = Double.valueOf(vector_result[i][4]);
                    this.total_venta_neta = Double.valueOf(vector_result[i][5]);
                    this.total_exento = Double.valueOf(vector_result[i][6]);
                    this.total_impuesto = Double.valueOf(vector_result[i][7]);
                    this.total_venta = Double.valueOf(vector_result[i][8]);
                    this.total_comprobante = Double.valueOf(vector_result[i][9]);
                    this.total_servicios_gravados = Double.valueOf(vector_result[i][10]);
                    this.total_servicios_exentos = Double.valueOf(vector_result[i][11]);
                    this.total_mercaderia_gravados = Double.valueOf(vector_result[i][12]);
                    this.total_mercaderia_exentos = Double.valueOf(vector_result[i][13]);
                }

                PrimeFaces.current().executeScript("PF('varTotales').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_TOTALES FCR_DIALOG_TOTALES: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_TOTALES FCR_DIALOG_TOTALES: " + ex.toString()));
        }
    }

    public Integer getId_totales() {
        return id_totales;
    }

    public void setId_totales(Integer id_totales) {
        this.id_totales = id_totales;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Double getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(Double tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotal_descuento() {
        return total_descuento;
    }

    public void setTotal_descuento(Double total_descuento) {
        this.total_descuento = total_descuento;
    }

    public Double getTotal_venta_neta() {
        return total_venta_neta;
    }

    public void setTotal_venta_neta(Double total_venta_neta) {
        this.total_venta_neta = total_venta_neta;
    }

    public Double getTotal_exento() {
        return total_exento;
    }

    public void setTotal_exento(Double total_exento) {
        this.total_exento = total_exento;
    }

    public Double getTotal_impuesto() {
        return total_impuesto;
    }

    public void setTotal_impuesto(Double total_impuesto) {
        this.total_impuesto = total_impuesto;
    }

    public Double getTotal_venta() {
        return total_venta;
    }

    public void setTotal_venta(Double total_venta) {
        this.total_venta = total_venta;
    }

    public Double getTotal_comprobante() {
        return total_comprobante;
    }

    public void setTotal_comprobante(Double total_comprobante) {
        this.total_comprobante = total_comprobante;
    }

    public Double getTotal_servicios_gravados() {
        return total_servicios_gravados;
    }

    public void setTotal_servicios_gravados(Double total_servicios_gravados) {
        this.total_servicios_gravados = total_servicios_gravados;
    }

    public Double getTotal_servicios_exentos() {
        return total_servicios_exentos;
    }

    public void setTotal_servicios_exentos(Double total_servicios_exentos) {
        this.total_servicios_exentos = total_servicios_exentos;
    }

    public Double getTotal_mercaderia_gravados() {
        return total_mercaderia_gravados;
    }

    public void setTotal_mercaderia_gravados(Double total_mercaderia_gravados) {
        this.total_mercaderia_gravados = total_mercaderia_gravados;
    }

    public Double getTotal_mercaderia_exentos() {
        return total_mercaderia_exentos;
    }

    public void setTotal_mercaderia_exentos(Double total_mercaderia_exentos) {
        this.total_mercaderia_exentos = total_mercaderia_exentos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
