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
public class Referencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_referencia;
    private Integer id_documento;
    private Integer id_document_type_ref;
    private String no_documento_ref;
    private String id_batch;
    private String fecha_documento_ref;
    private String razonref;
    private Integer id_codigo_ref;
    private String comentario_adjunto;

    @Override
    public String toString() {
        return "Referencia{" + "id_referencia=" + id_referencia + ", id_documento=" + id_documento + ", id_document_type_ref=" + id_document_type_ref + ", no_documento_ref=" + no_documento_ref + ", id_batch=" + id_batch + ", fecha_documento_ref=" + fecha_documento_ref + ", razonref=" + razonref + ", id_codigo_ref=" + id_codigo_ref + ", comentario_adjunto=" + comentario_adjunto + '}';
    }
    
}
