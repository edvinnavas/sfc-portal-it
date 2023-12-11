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
public class RegDglTotales implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_totales;
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

    @Override
    public String toString() {
        return "RegDglTotales{" + "id_totales=" + id_totales + ", moneda=" + moneda + ", tipo_cambio=" + tipo_cambio + ", subtotal=" + subtotal + ", total_descuento=" + total_descuento + ", total_venta_neta=" + total_venta_neta + ", total_exento=" + total_exento + ", total_impuesto=" + total_impuesto + ", total_venta=" + total_venta + ", total_comprobante=" + total_comprobante + ", total_servicios_gravados=" + total_servicios_gravados + ", total_servicios_exentos=" + total_servicios_exentos + ", total_mercaderia_gravados=" + total_mercaderia_gravados + ", total_mercaderia_exentos=" + total_mercaderia_exentos + '}';
    }
    
}
