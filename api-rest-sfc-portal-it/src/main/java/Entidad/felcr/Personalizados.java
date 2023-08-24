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
public class Personalizados implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_personalizados;
    private Integer id_dte;
    private Integer id_documento;
    private String sistema_emisor;
    private String numero_interno;
    private String direccion_receptor;
    private String correos_receptor;
    private String imprimir;
    private String distribuir;
    private String monto_letras;
    private String observaciones;

    @Override
    public String toString() {
        return "Personalizados{" + "id_personalizados=" + id_personalizados + ", id_dte=" + id_dte + ", id_documento=" + id_documento + ", sistema_emisor=" + sistema_emisor + ", numero_interno=" + numero_interno + ", direccion_receptor=" + direccion_receptor + ", correos_receptor=" + correos_receptor + ", imprimir=" + imprimir + ", distribuir=" + distribuir + ", monto_letras=" + monto_letras + ", observaciones=" + observaciones + '}';
    }
    
}
