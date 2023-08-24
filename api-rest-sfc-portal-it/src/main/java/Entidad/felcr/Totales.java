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
public class Totales implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_totales;
    private String moneda;
    private Double tipo_cambio;
    private Double total_grabado;
    private Double total_descuento;
    private Double total_venta_neta;
    private Double total_exento;
    private Double total_impuesto;
    private Double total_venta;
    private Double total_comprobante;
    private Double total_servicios_gravados;
    private Double total_servicios_exentos;
    private Double total_mercancias_gravadas;
    private Double total_mercancias_exentas;

    @Override
    public String toString() {
        return "Totales{" + "id_totales=" + id_totales + ", moneda=" + moneda + ", tipo_cambio=" + tipo_cambio + ", total_grabado=" + total_grabado + ", total_descuento=" + total_descuento + ", total_venta_neta=" + total_venta_neta + ", total_exento=" + total_exento + ", total_impuesto=" + total_impuesto + ", total_venta=" + total_venta + ", total_comprobante=" + total_comprobante + ", total_servicios_gravados=" + total_servicios_gravados + ", total_servicios_exentos=" + total_servicios_exentos + ", total_mercancias_gravadas=" + total_mercancias_gravadas + ", total_mercancias_exentas=" + total_mercancias_exentas + '}';
    }
    
}
