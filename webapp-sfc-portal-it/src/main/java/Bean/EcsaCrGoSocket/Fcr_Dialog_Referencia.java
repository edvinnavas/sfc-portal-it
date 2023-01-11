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

@Named(value = "Fcr_Dialog_Referencia")
@ViewScoped
public class Fcr_Dialog_Referencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Integer id_referencia;
    private Integer id_documento;
    private Integer id_document_type_ref;
    private String no_documento_ref;
    private String id_batch;
    private String fecha_documento_ref;
    private String razonref;
    private Integer id_codigo_ref;
    private String comentario_adjunto;
    private Integer tipo_despacho;
    private String tipo_nota_credito;
    
    private List<SelectItem> lst_codigo_ref;
    private List<SelectItem> lst_tipo_nota_credito;
    
    private Boolean btnGuardar;

    public Fcr_Dialog_Referencia() {
        try {
            this.lst_codigo_ref = new ArrayList<>();
            String cadenasql = "SELECT "
                    + "C.ID_CODIGO_REF, "
                    + "C.DESCRIPTION "
                    + "FROM "
                    + "CODIGO_REF C "
                    + "WHERE "
                    + "C.ID_CODIGO_REF IN (7,6,8,9)";
            
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
                this.lst_codigo_ref.add(new SelectItem(Integer.valueOf(vector_result[i][0]),vector_result[i][1]));
            }
            
            this.lst_tipo_nota_credito = new ArrayList<>();
            this.lst_tipo_nota_credito.add(new SelectItem("TOTAL","TOTAL"));
            this.lst_tipo_nota_credito.add(new SelectItem("PACIAL","PACIAL"));
            
        } catch (Exception ex) {
            System.out.println("ERROR FCR_DIALOG_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString()));
        }
    }
    
    public void mostrar_dialog_referencia(Integer id_document, String procesado, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_document != null) {
                String cadenasql = " SELECT "
                        + "R.ID_REFERENCIA, "
                        + "R.ID_DOCUMENTO, "
                        + "R.ID_DOCUMENT_TYPE_REF, "
                        + "R.NO_DOCUMENTO_REF, "
                        + "R.ID_BATCH, "
                        + "R.FECHA_DOCUMENTO_REF, "
                        + "R.RAZONREF, "
                        + "R.ID_CODIGO_REF, "
                        + "R.COMENTARIO_ADJUNTO, "
                        + "R.TIPO_DESPACHO, " 
                        + "R.TIPO_NOTA_CREDITO "
                        + "FROM "
                        + "REFERENCIA R "
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
                    this.id_referencia = Integer.valueOf(vector_result[i][0]);
                    this.id_documento = Integer.valueOf(vector_result[i][1]);
                    this.id_document_type_ref = Integer.valueOf(vector_result[i][2]);
                    this.no_documento_ref = vector_result[i][3];
                    this.id_batch = vector_result[i][4];
                    this.fecha_documento_ref = vector_result[i][5];
                    this.razonref = vector_result[i][6];
                    this.id_codigo_ref = Integer.valueOf(vector_result[i][7]);
                    this.comentario_adjunto = vector_result[i][8];
                    this.tipo_despacho = Integer.valueOf(vector_result[i][9]);
                    this.tipo_nota_credito = vector_result[i][10];
                }
                
                if(procesado.equals("SI")) {
                    this.btnGuardar = true;
                } else {
                    this.btnGuardar = false;
                }

                PrimeFaces.current().executeScript("PF('varReferencia').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString()));
        }
    }
    
    public void modificar_referencia () {
        try {
            Fcr_Servicio servicio = new Fcr_Servicio();
            String resultado = servicio.modificarReferencia(this.usuario.getUsuario(), this.id_referencia, this.no_documento_ref, this.id_batch, this.comentario_adjunto, this.id_codigo_ref, this.tipo_nota_credito);
            
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(13), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Modificar referencia GO-SOCKET ID_REFERENCIA: " + this.id_referencia);
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado.substring(2)));
            }
            
            PrimeFaces.current().executeScript("PF('varReferencia').hide();");
            
        } catch(Exception ex) {
            System.out.println("ERROR MODIFICAR_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MODIFICAR_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString()));
        }
    }

    public Integer getId_referencia() {
        return id_referencia;
    }

    public void setId_referencia(Integer id_referencia) {
        this.id_referencia = id_referencia;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(Integer id_documento) {
        this.id_documento = id_documento;
    }

    public Integer getId_document_type_ref() {
        return id_document_type_ref;
    }

    public void setId_document_type_ref(Integer id_document_type_ref) {
        this.id_document_type_ref = id_document_type_ref;
    }

    public String getNo_documento_ref() {
        return no_documento_ref;
    }

    public void setNo_documento_ref(String no_documento_ref) {
        this.no_documento_ref = no_documento_ref;
    }

    public String getId_batch() {
        return id_batch;
    }

    public void setId_batch(String id_batch) {
        this.id_batch = id_batch;
    }

    public String getFecha_documento_ref() {
        return fecha_documento_ref;
    }

    public void setFecha_documento_ref(String fecha_documento_ref) {
        this.fecha_documento_ref = fecha_documento_ref;
    }

    public String getRazonref() {
        return razonref;
    }

    public void setRazonref(String razonref) {
        this.razonref = razonref;
    }

    public Integer getId_codigo_ref() {
        return id_codigo_ref;
    }

    public void setId_codigo_ref(Integer id_codigo_ref) {
        this.id_codigo_ref = id_codigo_ref;
    }

    public String getComentario_adjunto() {
        return comentario_adjunto;
    }

    public void setComentario_adjunto(String comentario_adjunto) {
        this.comentario_adjunto = comentario_adjunto;
    }

    public Integer getTipo_despacho() {
        return tipo_despacho;
    }

    public void setTipo_despacho(Integer tipo_despacho) {
        this.tipo_despacho = tipo_despacho;
    }

    public Boolean getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(Boolean btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public List<SelectItem> getLst_codigo_ref() {
        return lst_codigo_ref;
    }

    public void setLst_codigo_ref(List<SelectItem> lst_codigo_ref) {
        this.lst_codigo_ref = lst_codigo_ref;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo_nota_credito() {
        return tipo_nota_credito;
    }

    public void setTipo_nota_credito(String tipo_nota_credito) {
        this.tipo_nota_credito = tipo_nota_credito;
    }

    public List<SelectItem> getLst_tipo_nota_credito() {
        return lst_tipo_nota_credito;
    }

    public void setLst_tipo_nota_credito(List<SelectItem> lst_tipo_nota_credito) {
        this.lst_tipo_nota_credito = lst_tipo_nota_credito;
    }
    
}
