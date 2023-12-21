package Entidad.felcr;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cat_Tipo_Contribuyente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ID_TIPO_CONTRIBUYENTE;
    private String COD_TIPO_CONTRIBUYENTE;
    private String DESCRIPTION;

    @Override
    public String toString() {
        return "Cat_Tipo_Contribuyente{" + "ID_TIPO_CONTRIBUYENTE=" + ID_TIPO_CONTRIBUYENTE + ", COD_TIPO_CONTRIBUYENTE=" + COD_TIPO_CONTRIBUYENTE + ", DESCRIPTION=" + DESCRIPTION + '}';
    }
    
}
