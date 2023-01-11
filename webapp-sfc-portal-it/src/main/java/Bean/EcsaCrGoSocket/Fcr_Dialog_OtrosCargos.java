package Bean.EcsaCrGoSocket;

import Entidad.Evento;
import Entidad.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Fcr_Servicio;

@Named(value = "Fcr_Dialog_OtrosCargos")
@ViewScoped
public class Fcr_Dialog_OtrosCargos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Integer id_otros_cargos;
    private Integer id_documento;
    private String activo;
    private Integer id_tipo_otros_cargos;
    private String numero_identificacion;
    private String razon_social;
    private String descripcion;
    private Double porcentaje_otros_cargos;
    private Double monto_otros_cargos;
    
    private List<SelectItem> lst_tipo_otros_cargos;
    private List<SelectItem> lst_activo;
    
    private Boolean btnGuardar;

    public Fcr_Dialog_OtrosCargos() {
        try {
            this.lst_tipo_otros_cargos = new ArrayList<>();
            String cadenasql = "SELECT "
                    + "T.ID_TIPO_OTROS_CARGOS, "
                    + "T.NOMBRE "
                    + "FROM "
                    + "TIPO_OTROS_CARGOS T "
                    + "ORDER BY "
                    + "T.ID_TIPO_OTROS_CARGOS";
            
            Fcr_Servicio servicio = new Fcr_Servicio();
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
                this.lst_tipo_otros_cargos.add(new SelectItem(Integer.valueOf(vector_result[i][0]),vector_result[i][1]));
            }
            
            this.lst_activo = new ArrayList<>();
            this.lst_activo.add(new SelectItem("NO","NO"));
            this.lst_activo.add(new SelectItem("SI","SI"));
            
        } catch (Exception ex) {
            System.out.println("ERROR FCR_DIALOG_OTROS_CARGOS FCR_DIALOG_OTROS_CARGOS: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_OTROS_CARGOS FCR_DIALOG_OTROS_CARGOS: " + ex.toString()));
        }
    }
    
    public void mostrar_dialog_otros_cargos(Integer id_document, String procesado, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_document != null) {
                String cadenasql = " SELECT "
                        + "R.ID_OTROS_CARGOS, "
                        + "R.ID_DOCUMENTO, "
                        + "R.ACTIVO, "
                        + "R.ID_TIPO_OTROS_CARGOS, "
                        + "R.NUMERO_IDENTIFICACION, "
                        + "R.RAZON_SOCIAL, "
                        + "R.DESCRIPCION, "
                        + "R.PORCENTAJE_OTROS_CARGOS, "
                        + "R.MONTO_OTROS_CARGOS "
                        + "FROM "
                        + "OTROS_CARGOS R "
                        + "WHERE "
                        + "R.ID_DOCUMENTO = " + id_document;

                Fcr_Servicio servicio = new Fcr_Servicio();
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
                    this.id_otros_cargos = Integer.valueOf(vector_result[i][0]);
                    this.id_documento = Integer.valueOf(vector_result[i][1]);
                    this.activo = vector_result[i][2];
                    this.id_tipo_otros_cargos = Integer.valueOf(vector_result[i][3]);
                    this.numero_identificacion = vector_result[i][4];
                    this.razon_social = vector_result[i][5];
                    this.descripcion = vector_result[i][6];
                    this.porcentaje_otros_cargos = Double.valueOf(vector_result[i][7]);
                    this.monto_otros_cargos = Double.valueOf(vector_result[i][8]);
                }
                
                if(procesado.equals("SI")) {
                    this.btnGuardar = true;
                } else {
                    this.btnGuardar = false;
                }

                PrimeFaces.current().executeScript("PF('varOtrosCargos').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_OTROS_CARGOS FCR_DIALOG_OTROS_CARGOS: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_OTROS_CARGOS FCR_DIALOG_OTROS_CARGOS: " + ex.toString()));
        }
    }
    
    public void modificar_otros_cargos () {
        try {
            Fcr_Servicio servicio = new Fcr_Servicio();
            String resultado = servicio.modificarOtrosCargos(this.id_otros_cargos, this.id_documento, this.activo, this.id_tipo_otros_cargos, this.numero_identificacion, this.razon_social, this.descripcion, this.porcentaje_otros_cargos, this.monto_otros_cargos);
            
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(51), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Modificar otros_cargos GO-SOCKET ID_OTROS_CARGOS: " + this.id_otros_cargos);
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado.substring(2)));
            }
            
            PrimeFaces.current().executeScript("PF('varOtrosCargos').hide();");
            
        } catch(Exception ex) {
            System.out.println("ERROR MODIFICAR_OTROS_CARGOS FCR_DIALOG_OTROS_CARGOS: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MODIFICAR_OTROS_CARGOS FCR_DIALOG_OTROS_CARGOS: " + ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getId_otros_cargos() {
        return id_otros_cargos;
    }

    public void setId_otros_cargos(Integer id_otros_cargos) {
        this.id_otros_cargos = id_otros_cargos;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(Integer id_documento) {
        this.id_documento = id_documento;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public Integer getId_tipo_otros_cargos() {
        return id_tipo_otros_cargos;
    }

    public void setId_tipo_otros_cargos(Integer id_tipo_otros_cargos) {
        this.id_tipo_otros_cargos = id_tipo_otros_cargos;
    }

    public String getNumero_identificacion() {
        return numero_identificacion;
    }

    public void setNumero_identificacion(String numero_identificacion) {
        this.numero_identificacion = numero_identificacion;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPorcentaje_otros_cargos() {
        return porcentaje_otros_cargos;
    }

    public void setPorcentaje_otros_cargos(Double porcentaje_otros_cargos) {
        this.porcentaje_otros_cargos = porcentaje_otros_cargos;
    }

    public Double getMonto_otros_cargos() {
        return monto_otros_cargos;
    }

    public void setMonto_otros_cargos(Double monto_otros_cargos) {
        this.monto_otros_cargos = monto_otros_cargos;
    }

    public List<SelectItem> getLst_tipo_otros_cargos() {
        return lst_tipo_otros_cargos;
    }

    public void setLst_tipo_otros_cargos(List<SelectItem> lst_tipo_otros_cargos) {
        this.lst_tipo_otros_cargos = lst_tipo_otros_cargos;
    }

    public List<SelectItem> getLst_activo() {
        return lst_activo;
    }

    public void setLst_activo(List<SelectItem> lst_activo) {
        this.lst_activo = lst_activo;
    }

    public Boolean getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(Boolean btnGuardar) {
        this.btnGuardar = btnGuardar;
    }
    
}
