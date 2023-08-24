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
public class Documento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_documento;
    private Integer id_encabezado;
    private Integer id_cae;

    @Override
    public String toString() {
        return "Documento{" + "id_documento=" + id_documento + ", id_encabezado=" + id_encabezado + ", id_cae=" + id_cae + '}';
    }
    
}
