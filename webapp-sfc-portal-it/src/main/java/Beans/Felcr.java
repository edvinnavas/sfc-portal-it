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
    private String nombre_usuario;

    @PostConstruct
    public void init() {
        try {
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
            if (usuario_sesion.getId_usuario() != null) {
                this.usuario_sesion = usuario_sesion;
                this.nombre_usuario = this.usuario_sesion.getNombre_usuario();
            }
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
    
    public void enviar_gosocket() {
        try {
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            if (this.sel_reg_tbl_dtecr != null) {
                if (this.sel_reg_tbl_dtecr.getProcesado().trim().equals("NO")) {
                    String parametros = this.ambiente + "♣" + this.nombre_usuario + "♣" + this.sel_reg_tbl_dtecr.getId_convert_document();
                    ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
                    String resultado = cliente_rest_api.gosocket(parametros);
                    String opcion = resultado.substring(0, 1);
                    if (opcion.equals("0")) {
                        this.filtrar_tabla();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "El documento fiscal ya fue enviado a GoSocket."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un documento fiscal."));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: enviar_gosocket(), ERRROR: " + ex.toString());
        }
    }

    public void extraer_docs_jde() {
        try {
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            Integer anio = this.fecha_facturacion.getYear() + 1900;
            Integer mes = this.fecha_facturacion.getMonth() + 1;
            Integer dia = this.fecha_facturacion.getDate();
            String parametros = this.ambiente + "♣" + this.nombre_usuario + "♣" + anio + "♣" + mes + "♣" + dia + "♣" + this.tabla;
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String resultado = cliente_rest_api.cargar_docs(parametros);
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                this.filtrar_tabla();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: extraer_docs_jde(), ERRROR: " + ex.toString());
        }
    }
    
}
