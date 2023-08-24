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
public class Id_Doc_Medio_Pago implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_doc;
    private Integer id_medio_pago;

    @Override
    public String toString() {
        return "Id_Doc_Medio_Pago{" + "id_doc=" + id_doc + ", id_medio_pago=" + id_medio_pago + '}';
    }
    
}
