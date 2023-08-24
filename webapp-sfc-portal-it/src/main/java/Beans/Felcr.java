package Beans;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@Named(value = "Felcr")
@Getter
@Setter
public class Felcr implements Serializable {

    private static final long serialVersionUID = 1L;

    private Entidades.UsuarioSesion usuario_sesion;
    private List<Entidades.RegTblDteCr> lst_reg_tbl_dtecr;
    private Entidades.RegTblDteCr sel_reg_tbl_dtecr;
    private Date fecha_facturacion;
    private String tabla;
    private List<SelectItem> lst_tabla;
    private String ambiente;

    @PostConstruct
    public void init() {
        try {
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            this.lst_reg_tbl_dtecr = new ArrayList<>();
            this.fecha_facturacion = new Date();
            this.lst_tabla = new ArrayList<>();
            this.lst_tabla.add(new SelectItem("F4211", "Facturación"));
            this.lst_tabla.add(new SelectItem("F42119", "Contabilidad"));
            this.filtrar_tabla();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: init(), ERRROR: " + ex.toString());
        }
    }

    public void cargar_vista(Entidades.UsuarioSesion usuario_sesion) {
        try {
            this.usuario_sesion = usuario_sesion;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema.", "Vista-DTE-CR."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: cargar_vista(), ERRROR: " + ex.toString());
        }
    }

    public void filtrar_tabla() {
        try {            
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String json_result = cliente_rest_api.lista_dtes(this.ambiente, dateFormat1.format(this.fecha_facturacion));

            Type lista_viaje_type = new TypeToken<List<Entidades.RegTblDteCr>>() {
            }.getType();
            
            this.lst_reg_tbl_dtecr = new Gson().fromJson(json_result, lista_viaje_type);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: filtrar_tabla(), ERRROR: " + ex.toString());
        }
    }

}
