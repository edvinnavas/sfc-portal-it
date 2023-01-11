package Bean.EcsaCrGoSocket;

import java.io.Serializable;

public class Fcr_Detalle_List implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private Integer id_detalle;
    private Integer id_documento;
    private Integer tipo_producto;
    private String codigo_producto;
    private String detalle_producto;
    private Double cantidad;
    private String unidad_medida;
    private String unidad_medida_comercial;
    private Double precio_unitario;
    private Double monto_descuento;
    private Double monto_total;
    private Double subtotal;
    private Double monto_total_linea;
    private String naturaliza_descuento;
    private Integer porcentaje_impuesto;
    private String exento;
    private String cabys;

    public Fcr_Detalle_List(Integer id, Integer id_detalle, Integer id_documento, Integer tipo_producto, String codigo_producto, String detalle_producto, Double cantidad, String unidad_medida, String unidad_medida_comercial, Double precio_unitario, Double monto_descuento, Double monto_total, Double subtotal, Double monto_total_linea, String naturaliza_descuento, Integer porcentaje_impuesto, String exento, String cabys) {
        this.id = id;
        this.id_detalle = id_detalle;
        this.id_documento = id_documento;
        this.tipo_producto = tipo_producto;
        this.codigo_producto = codigo_producto;
        this.detalle_producto = detalle_producto;
        this.cantidad = cantidad;
        this.unidad_medida = unidad_medida;
        this.unidad_medida_comercial = unidad_medida_comercial;
        this.precio_unitario = precio_unitario;
        this.monto_descuento = monto_descuento;
        this.monto_total = monto_total;
        this.subtotal = subtotal;
        this.monto_total_linea = monto_total_linea;
        this.naturaliza_descuento = naturaliza_descuento;
        this.porcentaje_impuesto = porcentaje_impuesto;
        this.exento = exento;
        this.cabys = cabys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(Integer id_documento) {
        this.id_documento = id_documento;
    }

    public Integer getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(Integer tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public String getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(String codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getDetalle_producto() {
        return detalle_producto;
    }

    public void setDetalle_producto(String detalle_producto) {
        this.detalle_producto = detalle_producto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public String getUnidad_medida_comercial() {
        return unidad_medida_comercial;
    }

    public void setUnidad_medida_comercial(String unidad_medida_comercial) {
        this.unidad_medida_comercial = unidad_medida_comercial;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Double getMonto_descuento() {
        return monto_descuento;
    }

    public void setMonto_descuento(Double monto_descuento) {
        this.monto_descuento = monto_descuento;
    }

    public Double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(Double monto_total) {
        this.monto_total = monto_total;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getMonto_total_linea() {
        return monto_total_linea;
    }

    public void setMonto_total_linea(Double monto_total_linea) {
        this.monto_total_linea = monto_total_linea;
    }

    public String getNaturaliza_descuento() {
        return naturaliza_descuento;
    }

    public void setNaturaliza_descuento(String naturaliza_descuento) {
        this.naturaliza_descuento = naturaliza_descuento;
    }

    public Integer getPorcentaje_impuesto() {
        return porcentaje_impuesto;
    }

    public void setPorcentaje_impuesto(Integer porcentaje_impuesto) {
        this.porcentaje_impuesto = porcentaje_impuesto;
    }

    public String getExento() {
        return exento;
    }

    public void setExento(String exento) {
        this.exento = exento;
    }
    
    public String getCabys() {
        return cabys;
    }

    public void setCabys(String cabys) {
        this.cabys = cabys;
    }
    
}
