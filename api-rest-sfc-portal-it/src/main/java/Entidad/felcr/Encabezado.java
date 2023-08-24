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
public class Encabezado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_encabezado;
    private Integer id_doc;
    private Integer id_emisor;
    private Integer id_receptor;
    private Integer id_totales;

    @Override
    public String toString() {
        return "Encabezado{" + "id_encabezado=" + id_encabezado + ", id_doc=" + id_doc + ", id_emisor=" + id_emisor + ", id_receptor=" + id_receptor + ", id_totales=" + id_totales + '}';
    }
    
}
