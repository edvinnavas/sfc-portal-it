package Bean.InventarioVentasRba;

import java.io.Serializable;

public class Rba_Estaciones_Servicio_List implements Serializable {

    private static final long serialVersionUID = 1L;

    //Identificador
    private Integer id_estacion_servicio;

    //Estacion de servicio
    private String cod_e1;
    private String cod_envoy;
    private String cod_payware;
    private String nombre_estacion;
    private String dcto_e1;
    private String mcu_e1;
    private String shan_e1;
    private String kcoo_e1;
    private String rout_e1;
    private String activo;

    public Rba_Estaciones_Servicio_List(Integer id_estacion_servicio, String cod_e1, String cod_envoy, String cod_payware, String nombre_estacion, String dcto_e1, String mcu_e1, String shan_e1, String kcoo_e1, String rout_e1, String activo) {
        this.id_estacion_servicio = id_estacion_servicio;
        this.cod_e1 = cod_e1;
        this.cod_envoy = cod_envoy;
        this.cod_payware = cod_payware;
        this.nombre_estacion = nombre_estacion;
        this.dcto_e1 = dcto_e1;
        this.mcu_e1 = mcu_e1;
        this.shan_e1 = shan_e1;
        this.kcoo_e1 = kcoo_e1;
        this.rout_e1 = rout_e1;
        this.activo = activo;
    }

    public Integer getId_estacion_servicio() {
        return id_estacion_servicio;
    }

    public void setId_estacion_servicio(Integer id_estacion_servicio) {
        this.id_estacion_servicio = id_estacion_servicio;
    }

    public String getCod_e1() {
        return cod_e1;
    }

    public void setCod_e1(String cod_e1) {
        this.cod_e1 = cod_e1;
    }

    public String getCod_envoy() {
        return cod_envoy;
    }

    public void setCod_envoy(String cod_envoy) {
        this.cod_envoy = cod_envoy;
    }

    public String getCod_payware() {
        return cod_payware;
    }

    public void setCod_payware(String cod_payware) {
        this.cod_payware = cod_payware;
    }

    public String getNombre_estacion() {
        return nombre_estacion;
    }

    public void setNombre_estacion(String nombre_estacion) {
        this.nombre_estacion = nombre_estacion;
    }

    public String getDcto_e1() {
        return dcto_e1;
    }

    public void setDcto_e1(String dcto_e1) {
        this.dcto_e1 = dcto_e1;
    }

    public String getMcu_e1() {
        return mcu_e1;
    }

    public void setMcu_e1(String mcu_e1) {
        this.mcu_e1 = mcu_e1;
    }

    public String getShan_e1() {
        return shan_e1;
    }

    public void setShan_e1(String shan_e1) {
        this.shan_e1 = shan_e1;
    }

    public String getKcoo_e1() {
        return kcoo_e1;
    }

    public void setKcoo_e1(String kcoo_e1) {
        this.kcoo_e1 = kcoo_e1;
    }

    public String getRout_e1() {
        return rout_e1;
    }

    public void setRout_e1(String rout_e1) {
        this.rout_e1 = rout_e1;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

}
