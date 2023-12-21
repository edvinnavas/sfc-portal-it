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
public class RegDglReceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_receptor;
    private Long id_tipo_contribuyente;
    private String tax_id;
    private String nombre_receptor;
    private String direccion;
    private String correo;
    private String codigo_area;

    @Override
    public String toString() {
        return "RegDglReceptor{" + "id_receptor=" + id_receptor + ", id_tipo_contribuyente=" + id_tipo_contribuyente + ", tax_id=" + tax_id + ", nombre_receptor=" + nombre_receptor + ", direccion=" + direccion + ", correo=" + correo + ", codigo_area=" + codigo_area + '}';
    }
    
}
