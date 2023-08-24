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
public class Dte implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_dte;
    private Integer id_documento;

    @Override
    public String toString() {
        return "Dte{" + "id_dte=" + id_dte + ", id_documento=" + id_documento + '}';
    }
    
}
