package Entidad;

import java.io.Serializable;

public class Fel_Conf_List implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id_conf;
    private String descripcion;
    private String valor;

    public Fel_Conf_List(Long id_conf, String descripcion, String valor) {
        this.id_conf = id_conf;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public Long getId_conf() {
        return id_conf;
    }

    public void setId_conf(Long id_conf) {
        this.id_conf = id_conf;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
