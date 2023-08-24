package Entidad.felcr;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Detalle implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_detalle;
    private Integer id_documento;
    private Integer id_tipo_codigo_producto;
    private String valor_codigo_producto;
    private String descripcion;
    private Double cantidad;
    private String unidad_medida;
    private String unidad_medida_comercial;
    private Double precio_unitario;
    private Double monto_descuento;
    private Double monto_total;
    private Double subtotal;
    private Double monto_total_linea;
    private String naturaleza_descuento;

    @Override
    public String toString() {
        return "Detalle{" + "id_detalle=" + id_detalle + ", id_documento=" + id_documento + ", id_tipo_codigo_producto=" + id_tipo_codigo_producto + ", valor_codigo_producto=" + valor_codigo_producto + ", descripcion=" + descripcion + ", cantidad=" + cantidad + ", unidad_medida=" + unidad_medida + ", unidad_medida_comercial=" + unidad_medida_comercial + ", precio_unitario=" + precio_unitario + ", monto_descuento=" + monto_descuento + ", monto_total=" + monto_total + ", subtotal=" + subtotal + ", monto_total_linea=" + monto_total_linea + ", naturaleza_descuento=" + naturaleza_descuento + '}';
    }
    
}
