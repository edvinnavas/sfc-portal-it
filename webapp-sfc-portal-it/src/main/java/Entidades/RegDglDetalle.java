package Entidades;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegDglDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long id_detalle;
    private Long id_documento;
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

    @Override
    public String toString() {
        return "RegDglDetalle{" + "id=" + id + ", id_detalle=" + id_detalle + ", id_documento=" + id_documento + ", tipo_producto=" + tipo_producto + ", codigo_producto=" + codigo_producto + ", detalle_producto=" + detalle_producto + ", cantidad=" + cantidad + ", unidad_medida=" + unidad_medida + ", unidad_medida_comercial=" + unidad_medida_comercial + ", precio_unitario=" + precio_unitario + ", monto_descuento=" + monto_descuento + ", monto_total=" + monto_total + ", subtotal=" + subtotal + ", monto_total_linea=" + monto_total_linea + ", naturaliza_descuento=" + naturaliza_descuento + ", porcentaje_impuesto=" + porcentaje_impuesto + ", exento=" + exento + ", cabys=" + cabys + '}';
    }
    
}
