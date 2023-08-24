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
public class Receptor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id_receptor;
    
    private Integer id_tipo_contribuyente;
    private String id_tax_receptor;
    private String nombre_receptor;
    private String nombre_comercial_receptor;
    private String cdgintrecep;
    private String calle;
    private String departamento;
    private String distrito;
    private String ciudad;
    private String municipio;
    private String email;
    private String codigo_pais_telefono;
    private String telefono;
    private String codigo_pais_fax;
    private String fax;

    @Override
    public String toString() {
        return "Receptor{" + "id_receptor=" + id_receptor + ", id_tipo_contribuyente=" + id_tipo_contribuyente + ", id_tax_receptor=" + id_tax_receptor + ", nombre_receptor=" + nombre_receptor + ", nombre_comercial_receptor=" + nombre_comercial_receptor + ", cdgintrecep=" + cdgintrecep + ", calle=" + calle + ", departamento=" + departamento + ", distrito=" + distrito + ", ciudad=" + ciudad + ", municipio=" + municipio + ", email=" + email + ", codigo_pais_telefono=" + codigo_pais_telefono + ", telefono=" + telefono + ", codigo_pais_fax=" + codigo_pais_fax + ", fax=" + fax + '}';
    }
    
}
