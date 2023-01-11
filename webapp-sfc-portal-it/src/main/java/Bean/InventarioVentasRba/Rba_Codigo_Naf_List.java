package Bean.InventarioVentasRba;

import java.io.Serializable;

public class Rba_Codigo_Naf_List  implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer correlativo;
    private String cod_naf;

    public Rba_Codigo_Naf_List(Integer correlativo, String cod_naf) {
        this.correlativo = correlativo;
        this.cod_naf = cod_naf;
    }

    public Integer getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(Integer correlativo) {
        this.correlativo = correlativo;
    }

    public String getCod_naf() {
        return cod_naf;
    }

    public void setCod_naf(String cod_naf) {
        this.cod_naf = cod_naf;
    }
    
}
