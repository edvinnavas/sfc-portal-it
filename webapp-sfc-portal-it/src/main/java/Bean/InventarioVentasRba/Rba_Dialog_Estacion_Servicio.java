package Bean.InventarioVentasRba;

import Entidad.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import ClientRestService.Rba_Servicio;

@Named(value = "Rba_Dialog_Estacion_Servicio")
@ViewScoped
public class Rba_Dialog_Estacion_Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private Integer opcion_carga;

    private Integer id_estacion_servicio;
    private String cod_e1;
    private String cod_envoy;
    private String cod_payware;
    private String nombre_estacion;
    private String dcto_e1;
    private String mcu_e1;
    private String shan_e1;
    private String kcoo_e1;
    private String rout_e1;

    private Boolean txt_cod_e1;
    private Boolean txt_cod_envoy;
    private Boolean txt_cod_payware;
    private Boolean txt_nombre_estacion;
    private Boolean txt_dcto_e1;
    private Boolean txt_mcu_e1;
    private Boolean txt_shan_e1;
    private Boolean txt_kcoo_e1;
    private Boolean txt_rout_e1;
    private Boolean btnAgregarCodNaf;
    private Boolean btnEliminarCodNaf;
    private Boolean btnAceptar;

    private List<Rba_Codigo_Naf_List> lst_cod_naf;
    private Rba_Codigo_Naf_List sel_cod_naf;

    public void carga_info_crear() {
        try {
            this.opcion_carga = 1;

            this.cod_e1 = "";
            this.cod_envoy = "";
            this.cod_payware = "";
            this.nombre_estacion = "";
            this.dcto_e1 = "";
            this.mcu_e1 = "";
            this.shan_e1 = "";
            this.kcoo_e1 = "";
            this.rout_e1 = "";

            this.txt_cod_e1 = false;
            this.txt_cod_envoy = false;
            this.txt_cod_payware = false;
            this.txt_nombre_estacion = false;
            this.txt_dcto_e1 = false;
            this.txt_mcu_e1 = false;
            this.txt_shan_e1 = false;
            this.txt_kcoo_e1 = false;
            this.txt_rout_e1 = false;
            this.btnAgregarCodNaf = false;
            this.btnEliminarCodNaf = false;
            this.btnAceptar = false;

            this.lst_cod_naf = new ArrayList<>();

            PrimeFaces.current().executeScript("PF('varEstacionServicio').show();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: carga_info_crear ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void carga_info_modificar(Integer id_estacion_servicio) {
        try {
            this.opcion_carga = 2;

            if (id_estacion_servicio != null) {
                this.id_estacion_servicio = id_estacion_servicio;
                String cadenasql = "SELECT "
                        + "D.COD_E1, "
                        + "D.COD_ENVOY, "
                        + "D.COD_PAYWARE, "
                        + "D.NOMBRE_ESTACION, "
                        + "D.DCTO_E1, "
                        + "D.MCU_E1, "
                        + "D.SHAN_E1, "
                        + "D.KCOO_E1, "
                        + "D.ROUT_E1 "
                        + "FROM "
                        + "DIM_ESTACION_SERVICIO D "
                        + "WHERE "
                        + "D.ID_ESTACION_SERVICIO=" + this.id_estacion_servicio;

                Rba_Servicio servicio = new Rba_Servicio();
                List<String> resultado = servicio.reporte(cadenasql);

                Integer filas = resultado.size();
                Integer columnas = resultado.size();
                String[][] vector_result = new String[resultado.size()][columnas];
                for (Integer i = 0; i < resultado.size(); i++) {
                    for (Integer j = 0; j < resultado.size(); j++) {
                        vector_result[i][j] = resultado.get(j);
                    }
                }

                for (Integer i = 1; i < filas; i++) {
                    this.cod_e1 = vector_result[i][0];
                    this.cod_envoy = vector_result[i][1];
                    this.cod_payware = vector_result[i][2];
                    this.nombre_estacion = vector_result[i][3];
                    this.dcto_e1 = vector_result[i][4];
                    this.mcu_e1 = vector_result[i][5];
                    this.shan_e1 = vector_result[i][6];
                    this.kcoo_e1 = vector_result[i][7];
                    this.rout_e1 = vector_result[i][8];
                }

                this.lst_cod_naf = new ArrayList<>();
                cadenasql = "SELECT S.COD_NAF FROM SUR_COD_NAF S WHERE S.ID_ESTACION_SERVICIO=" + this.id_estacion_servicio;
                servicio = new Rba_Servicio();
                resultado = servicio.reporte(cadenasql);

                filas = resultado.size();
                columnas = resultado.size();
                vector_result = new String[resultado.size()][columnas];
                for (Integer i = 0; i < resultado.size(); i++) {
                    for (Integer j = 0; j < resultado.size(); j++) {
                        vector_result[i][j] = resultado.get(j);
                    }
                }

                for (Integer i = 1; i < filas; i++) {
                    Rba_Codigo_Naf_List cod_naf = new Rba_Codigo_Naf_List(i, vector_result[i][0]);
                    this.lst_cod_naf.add(cod_naf);
                }

                this.txt_cod_e1 = false;
                this.txt_cod_envoy = false;
                this.txt_cod_payware = false;
                this.txt_nombre_estacion = false;
                this.txt_dcto_e1 = false;
                this.txt_mcu_e1 = false;
                this.txt_shan_e1 = false;
                this.txt_kcoo_e1 = false;
                this.txt_rout_e1 = false;
                this.btnAgregarCodNaf = false;
                this.btnEliminarCodNaf = false;
                this.btnAceptar = false;

                PrimeFaces.current().executeScript("PF('varEstacionServicio').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar una Estación de Servicio."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: carga_info_modificar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void aceptar() {
        try {
            if (this.opcion_carga == 1) {
                this.insertar();
            }
            if (this.opcion_carga == 2) {
                this.modificar();
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: aceptar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void insertar() {
        try {
            String lst_cod_naf = "";
            for (Integer i = 0; i < this.lst_cod_naf.size(); i++) {
                if (i == 0) {
                    lst_cod_naf = this.lst_cod_naf.get(i).getCod_naf();
                } else {
                    lst_cod_naf = lst_cod_naf + "," + this.lst_cod_naf.get(i).getCod_naf();
                }
            }

            Rba_Servicio servicio = new Rba_Servicio();
            String resultado = servicio.insertarEstacionServicio(this.cod_e1, this.cod_envoy, this.cod_payware, this.nombre_estacion, this.dcto_e1, this.mcu_e1, this.shan_e1, this.kcoo_e1, this.rout_e1, lst_cod_naf);

            PrimeFaces.current().executeScript("PF('varEstacionServicio').hide();");
            PrimeFaces.current().executeScript("PF('var_tbl_estaciones').clearFilters();");

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: insertar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void modificar() {
        try {
            String lst_cod_naf = "";
            for (Integer i = 0; i < this.lst_cod_naf.size(); i++) {
                if (i == 0) {
                    lst_cod_naf = this.lst_cod_naf.get(i).getCod_naf();
                } else {
                    lst_cod_naf = lst_cod_naf + "," + this.lst_cod_naf.get(i).getCod_naf();
                }
            }

            Rba_Servicio servicio = new Rba_Servicio();
            String resultado = servicio.modificarEstacionServicio(this.id_estacion_servicio, this.cod_e1, this.cod_envoy, this.cod_payware, this.nombre_estacion, this.dcto_e1, this.mcu_e1, this.shan_e1, this.kcoo_e1, this.rout_e1, lst_cod_naf);

            PrimeFaces.current().executeScript("PF('varEstacionServicio').hide();");
            PrimeFaces.current().executeScript("PF('var_tbl_estaciones').clearFilters();");

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void agregar_cod_naf() {
        try {
            Integer correlativo = this.lst_cod_naf.size() + 1;
            Rba_Codigo_Naf_List codigo = new Rba_Codigo_Naf_List(correlativo, "-");
            this.lst_cod_naf.add(codigo);
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: agregar_cod_naf ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void eliminar_cod_naf() {
        try {
            if (this.sel_cod_naf != null) {
                this.lst_cod_naf.remove(this.sel_cod_naf);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "DEBE SELECCIONAR UN REGISTRO DE LA TABLA DE CODIGOS NAF."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_cod_naf ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getOpcion_carga() {
        return opcion_carga;
    }

    public void setOpcion_carga(Integer opcion_carga) {
        this.opcion_carga = opcion_carga;
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

    public Boolean getTxt_cod_e1() {
        return txt_cod_e1;
    }

    public void setTxt_cod_e1(Boolean txt_cod_e1) {
        this.txt_cod_e1 = txt_cod_e1;
    }

    public Boolean getTxt_cod_envoy() {
        return txt_cod_envoy;
    }

    public void setTxt_cod_envoy(Boolean txt_cod_envoy) {
        this.txt_cod_envoy = txt_cod_envoy;
    }

    public Boolean getTxt_cod_payware() {
        return txt_cod_payware;
    }

    public void setTxt_cod_payware(Boolean txt_cod_payware) {
        this.txt_cod_payware = txt_cod_payware;
    }

    public Boolean getTxt_nombre_estacion() {
        return txt_nombre_estacion;
    }

    public void setTxt_nombre_estacion(Boolean txt_nombre_estacion) {
        this.txt_nombre_estacion = txt_nombre_estacion;
    }

    public Boolean getTxt_dcto_e1() {
        return txt_dcto_e1;
    }

    public void setTxt_dcto_e1(Boolean txt_dcto_e1) {
        this.txt_dcto_e1 = txt_dcto_e1;
    }

    public Boolean getTxt_mcu_e1() {
        return txt_mcu_e1;
    }

    public void setTxt_mcu_e1(Boolean txt_mcu_e1) {
        this.txt_mcu_e1 = txt_mcu_e1;
    }

    public Boolean getTxt_shan_e1() {
        return txt_shan_e1;
    }

    public void setTxt_shan_e1(Boolean txt_shan_e1) {
        this.txt_shan_e1 = txt_shan_e1;
    }

    public Boolean getTxt_kcoo_e1() {
        return txt_kcoo_e1;
    }

    public void setTxt_kcoo_e1(Boolean txt_kcoo_e1) {
        this.txt_kcoo_e1 = txt_kcoo_e1;
    }

    public Boolean getTxt_rout_e1() {
        return txt_rout_e1;
    }

    public void setTxt_rout_e1(Boolean txt_rout_e1) {
        this.txt_rout_e1 = txt_rout_e1;
    }

    public Boolean getBtnAgregarCodNaf() {
        return btnAgregarCodNaf;
    }

    public void setBtnAgregarCodNaf(Boolean btnAgregarCodNaf) {
        this.btnAgregarCodNaf = btnAgregarCodNaf;
    }

    public Boolean getBtnEliminarCodNaf() {
        return btnEliminarCodNaf;
    }

    public void setBtnEliminarCodNaf(Boolean btnEliminarCodNaf) {
        this.btnEliminarCodNaf = btnEliminarCodNaf;
    }

    public Boolean getBtnAceptar() {
        return btnAceptar;
    }

    public void setBtnAceptar(Boolean btnAceptar) {
        this.btnAceptar = btnAceptar;
    }

    public List<Rba_Codigo_Naf_List> getLst_cod_naf() {
        return lst_cod_naf;
    }

    public void setLst_cod_naf(List<Rba_Codigo_Naf_List> lst_cod_naf) {
        this.lst_cod_naf = lst_cod_naf;
    }

    public Rba_Codigo_Naf_List getSel_cod_naf() {
        return sel_cod_naf;
    }

    public void setSel_cod_naf(Rba_Codigo_Naf_List sel_cod_naf) {
        this.sel_cod_naf = sel_cod_naf;
    }

}
