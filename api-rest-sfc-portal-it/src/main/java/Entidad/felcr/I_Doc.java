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
public class I_Doc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_doc;
    private Integer id_ambiente;
    private String contenidotc;
    private Integer id_document_type;
    private String numero_correlativo;
    private String fechaemis;
    private String establecimiento;
    private Integer id_condicion_pago;
    private String terminopagocdg;

    @Override
    public String toString() {
        return "I_Doc{" + "id_doc=" + id_doc + ", id_ambiente=" + id_ambiente + ", contenidotc=" + contenidotc + ", id_document_type=" + id_document_type + ", numero_correlativo=" + numero_correlativo + ", fechaemis=" + fechaemis + ", establecimiento=" + establecimiento + ", id_condicion_pago=" + id_condicion_pago + ", terminopagocdg=" + terminopagocdg + '}';
    }
    
}
