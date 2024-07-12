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
public class Log_Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_evento;
    private Tipo_Evento tipo_evento;
    private Usuario usuario;
    private String descripcion;
    private Date fecha_hora;

}
