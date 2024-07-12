package Entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rol_Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Rol rol;
    private List<Menu> lista_menu;
    private String usuario_m;
    private Date fecha_hora;

}
