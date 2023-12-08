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
public class RegDglDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_convert_document;
    private String sync_point;
    private String passwords;
    private String document_type;
    private Long id_dte;
    private Long id_documento;
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

    @Override
    public String toString() {
        return "RegDglDocument{" + "id_convert_document=" + id_convert_document + ", sync_point=" + sync_point + ", passwords=" + passwords + ", document_type=" + document_type + ", id_dte=" + id_dte + ", id_documento=" + id_documento + ", contenidotc=" + contenidotc + ", no_orden_e1=" + no_orden_e1 + ", tipo_orden_e1=" + tipo_orden_e1 + ", no_compania=" + no_compania + ", fecha_documento=" + fecha_documento + ", no_factura=" + no_factura + ", tax_id_receptor=" + tax_id_receptor + ", nombre_receptor=" + nombre_receptor + ", fecha_envio=" + fecha_envio + ", process_result=" + process_result + ", procesado=" + procesado + ", aban8_d1=" + aban8_d1 + '}';
    }
    
}
