package Entidades;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cat_Codigo_Ref implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long ID_CODIGO_REF;
    private String COD_CODIGO_REF;
    private String DESCRIPTION;

    @Override
    public String toString() {
        return "Cat_Codigo_Ref{" + "ID_CODIGO_REF=" + ID_CODIGO_REF + ", COD_CODIGO_REF=" + COD_CODIGO_REF + ", DESCRIPTION=" + DESCRIPTION + '}';
    }
    
}
