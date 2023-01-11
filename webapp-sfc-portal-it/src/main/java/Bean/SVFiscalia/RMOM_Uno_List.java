package Bean.SVFiscalia;

import java.io.Serializable;
import java.util.Date;

public class RMOM_Uno_List implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id_trans;
    private String token_valido;
    private Date fecha_reporte;
    private String codigo_persona_reporta;
    private String cargo_persona_reporta;
    private Integer codigo_mensaje;
    private String descripcionmensaje;
    private String codigotransaccion;
    private Integer registrosprocesados;

    public RMOM_Uno_List(Integer id_trans, String token_valido, Date fecha_reporte, String codigo_persona_reporta, String cargo_persona_reporta, Integer codigo_mensaje, String descripcionmensaje, String codigotransaccion, Integer registrosprocesados) {
        this.id_trans = id_trans;
        this.token_valido = token_valido;
        this.fecha_reporte = fecha_reporte;
        this.codigo_persona_reporta = codigo_persona_reporta;
        this.cargo_persona_reporta = cargo_persona_reporta;
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

    public String getCodigo_persona_reporta() {
        return codigo_persona_reporta;
    }

    public void setCodigo_persona_reporta(String codigo_persona_reporta) {
        this.codigo_persona_reporta = codigo_persona_reporta;
    }

    public String getCargo_persona_reporta() {
        return cargo_persona_reporta;
    }

    public void setCargo_persona_reporta(String cargo_persona_reporta) {
        this.cargo_persona_reporta = cargo_persona_reporta;
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
