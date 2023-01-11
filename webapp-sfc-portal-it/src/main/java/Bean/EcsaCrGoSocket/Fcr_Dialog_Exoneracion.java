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

@Named(value = "Fcr_Dialog_Exoneracion")
@ViewScoped
public class Fcr_Dialog_Exoneracion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Integer id_exoneracion;
    private Integer id_documento;
    private String activo;
    private Integer id_tipo_exoneracion;
    private String num_doc;
    private Date fecha_emision;
    private String nombre_institucion;
    private Double porcentaje_exoneracion;
    private Double monto_exoneracion;
    
    private List<SelectItem> lst_tipo_exoneracion;
    private List<SelectItem> lst_activo;
    
    private Boolean btnGuardar;

    public Fcr_Dialog_Exoneracion() {
        try {
            this.lst_tipo_exoneracion = new ArrayList<>();
            String cadenasql = "SELECT "
                    + "T.ID_TIPO_EXONERACION, "
                    + "T.NOMBRE "
                    + "FROM "
                    + "TIPO_EXONERACION T "
                    + "ORDER BY "
                    + "T.ID_TIPO_EXONERACION";
            
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
                this.lst_tipo_exoneracion.add(new SelectItem(Integer.valueOf(vector_result[i][0]),vector_result[i][1]));
            }
            
            this.lst_activo = new ArrayList<>();
            this.lst_activo.add(new SelectItem("NO","NO"));
            this.lst_activo.add(new SelectItem("SI","SI"));
            
        } catch (Exception ex) {
            System.out.println("ERROR FCR_DIALOG_EXONERACION FCR_DIALOG_EXONERACION: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_EXONERACION FCR_DIALOG_EXONERACION: " + ex.toString()));
        }
    }
    
    public void mostrar_dialog_exoneracion(Integer id_document, String procesado, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_document != null) {
                String cadenasql = " SELECT "
                        + "R.ID_EXONERACION, "
                        + "R.ID_DOCUMENTO, "
                        + "R.ACTIVO, "
                        + "R.ID_TIPO_EXONERACION, "
                        + "R.NUM_DOC, "
                        + "R.FECHA_EMISION, "
                        + "R.NOMBRE_INSTITUCION, "
                        + "R.PORCENTAJE_EXONERACION, "
                        + "R.MONTO_EXONERACION "
                        + "FROM "
                        + "EXONERACION R "
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

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                for (Integer i = 1; i < filas; i++) {
                    this.id_exoneracion = Integer.valueOf(vector_result[i][0]);
                    this.id_documento = Integer.valueOf(vector_result[i][1]);
                    this.activo = vector_result[i][2];
                    this.id_tipo_exoneracion = Integer.valueOf(vector_result[i][3]);
                    this.num_doc = vector_result[i][4];
                    this.fecha_emision = dateFormat.parse(vector_result[i][5]);
                    this.nombre_institucion = vector_result[i][6];
                    this.porcentaje_exoneracion = Double.valueOf(vector_result[i][7]);
                    this.monto_exoneracion = Double.valueOf(vector_result[i][8]);
                }
                
                if(procesado.equals("SI")) {
                    this.btnGuardar = true;
                } else {
                    this.btnGuardar = false;
                }

                PrimeFaces.current().executeScript("PF('varExoneracion').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_EXONERACION FCR_DIALOG_EXONERACION: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_EXONERACION FCR_DIALOG_EXONERACION: " + ex.toString()));
        }
    }
    
    public void modificar_exoneracion () {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            Fcr_Servicio servicio = new Fcr_Servicio();
            String resultado = servicio.modificarExoneracion(this.id_exoneracion, this.id_documento, this.activo, this.id_tipo_exoneracion, this.num_doc, dateFormat.format(this.fecha_emision), this.nombre_institucion, this.porcentaje_exoneracion, this.monto_exoneracion);
            
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(50), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Modificar exoneracion GO-SOCKET ID_EXONERACION: " + this.id_exoneracion);
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado.substring(2)));
            }
            
            PrimeFaces.current().executeScript("PF('varExoneracion').hide();");
            
        } catch(Exception ex) {
            System.out.println("ERROR MODIFICAR_EXONERACION FCR_DIALOG_EXONERACION: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MODIFICAR_EXONERACION FCR_DIALOG_EXONERACION: " + ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getId_exoneracion() {
        return id_exoneracion;
    }

    public void setId_exoneracion(Integer id_exoneracion) {
        this.id_exoneracion = id_exoneracion;
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

    public Integer getId_tipo_exoneracion() {
        return id_tipo_exoneracion;
    }

    public void setId_tipo_exoneracion(Integer id_tipo_exoneracion) {
        this.id_tipo_exoneracion = id_tipo_exoneracion;
    }

    public String getNum_doc() {
        return num_doc;
    }

    public void setNum_doc(String num_doc) {
        this.num_doc = num_doc;
    }

    public Date getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Date fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getNombre_institucion() {
        return nombre_institucion;
    }

    public void setNombre_institucion(String nombre_institucion) {
        this.nombre_institucion = nombre_institucion;
    }

    public Double getPorcentaje_exoneracion() {
        return porcentaje_exoneracion;
    }

    public void setPorcentaje_exoneracion(Double porcentaje_exoneracion) {
        this.porcentaje_exoneracion = porcentaje_exoneracion;
    }

    public Double getMonto_exoneracion() {
        return monto_exoneracion;
    }

    public void setMonto_exoneracion(Double monto_exoneracion) {
        this.monto_exoneracion = monto_exoneracion;
    }

    public List<SelectItem> getLst_tipo_exoneracion() {
        return lst_tipo_exoneracion;
    }

    public void setLst_tipo_exoneracion(List<SelectItem> lst_tipo_exoneracion) {
        this.lst_tipo_exoneracion = lst_tipo_exoneracion;
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
