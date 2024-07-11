package Entidades;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tipo_Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_tipo_menu;
    private String nombre;
    private Integer activo;
    private String descripcion;
    private String usuario_m;
    private Date fecha_hora;

}
