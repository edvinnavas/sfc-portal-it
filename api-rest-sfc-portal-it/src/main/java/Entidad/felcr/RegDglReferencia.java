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
public class RegDglReferencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_referencia;
    private Long id_documento;
    private Long id_document_type_ref;
    private String no_documento_ref;
    private String id_batch;
    private String fecha_documento_ref;
    private String razonref;
    private Long id_codigo_ref;
    private String comentario_adjunto;
    private Integer tipo_despacho;
    private String tipo_nota_credito;

    @Override
    public String toString() {
        return "RegDglReferencia{" + "id_referencia=" + id_referencia + ", id_documento=" + id_documento + ", id_document_type_ref=" + id_document_type_ref + ", no_documento_ref=" + no_documento_ref + ", id_batch=" + id_batch + ", fecha_documento_ref=" + fecha_documento_ref + ", razonref=" + razonref + ", id_codigo_ref=" + id_codigo_ref + ", comentario_adjunto=" + comentario_adjunto + ", tipo_despacho=" + tipo_despacho + ", tipo_nota_credito=" + tipo_nota_credito + '}';
    }
    
}
