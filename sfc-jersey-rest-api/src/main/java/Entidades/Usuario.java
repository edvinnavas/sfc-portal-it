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
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_usuario;
    private String nombre_completo;
    private String nombre_usuario;
    private String constrasena;
    private String correo_electronico;
    private Integer activo;
    private String autenticacion;
    private String descripcion;
    private Rol rol;
    private String usuario_m;
    private Date fecha_hora;

}
