package Bean.SVFiscalia;

import java.io.Serializable;
import java.util.Date;

public class RMME_Uno_List implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id_trans;
    private String token_valido;
    private Date fecha_reporte;
    private Integer codigo_mensaje;
    private String descripcionmensaje;
    private String codigotransaccion;
    private Integer registrosprocesados;

    public RMME_Uno_List(Integer id_trans, String token_valido, Date fecha_reporte, Integer codigo_mensaje, String descripcionmensaje, String codigotransaccion, Integer registrosprocesados) {
        this.id_trans = id_trans;
        this.token_valido = token_valido;
        this.fecha_reporte = fecha_reporte;
        this.codigo_mensaje = codigo_mensaje;
        this.descripcionmensaje = descripcionmensaje;
        this.codigotransaccion = codigotransaccion;
        this.registrosprocesados = registrosprocesados;
    }

    public Integer getId_trans() {
        return id_trans;
    }

    public void setId_trans(Integer id_trans) {
        this.id_trans = id_trans;
    }

    public String getToken_valido() {
        return token_valido;
    }

    public void setToken_valido(String token_valido) {
        this.token_valido = token_valido;
    }

    public Date getFecha_reporte() {
        return fecha_reporte;
    }

    public void setFecha_reporte(Date fecha_reporte) {
        this.fecha_reporte = fecha_reporte;
    }

    public Integer getCodigo_mensaje() {
        return codigo_mensaje;
    }

    public void setCodigo_mensaje(Integer codigo_mensaje) {
        this.codigo_mensaje = codigo_mensaje;
    }

    public String getDescripcionmensaje() {
        return descripcionmensaje;
    }

    public void setDescripcionmensaje(String descripcionmensaje) {
        this.descripcionmensaje = descripcionmensaje;
    }

    public String getCodigotransaccion() {
        return codigotransaccion;
    }

    public void setCodigotransaccion(String codigotransaccion) {
        this.codigotransaccion = codigotransaccion;
    }

    public Integer getRegistrosprocesados() {
        return registrosprocesados;
    }

    public void setRegistrosprocesados(Integer registrosprocesados) {
        this.registrosprocesados = registrosprocesados;
    }

}
