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

@Named(value = "Fcr_Dialog_Receptor")
@ViewScoped
public class Fcr_Dialog_Receptor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Integer id_receptor;
    private Integer id_tipo_contribuyente;
    private String tax_id;
    private String nombre_cliente;
    private String direccion;
    private String correo;
    private String codigo_area;
    
    private List<SelectItem> lst_tipo_contribuyente;
    
    private Boolean btnGuardar;
    
    public Fcr_Dialog_Receptor() {
        try {
            this.lst_tipo_contribuyente = new ArrayList<>();
            String cadenasql = "SELECT "
                    + "TC.ID_TIPO_CONTRIBUYENTE, "
                    + "TC.COD_TIPO_CONTRIBUYENTE || '-' || TC.DESCRIPTION DESCRIPCION "
                    + "FROM "
                    + "TIPO_CONTRIBUYENTE TC "
                    + "ORDER BY "
                    + "DESCRIPCION";
            
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
                this.lst_tipo_contribuyente.add(new SelectItem(Integer.valueOf(vector_result[i][0]),vector_result[i][1]));
            }
        } catch (Exception ex) {
            System.out.println("ERROR FCR_DIALOG_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_REFERENCIA FCR_DIALOG_REFERENCIA: " + ex.toString()));
        }
    }
    
    public void mostrar_dialog_receptor(Integer id_convert_document, String procesado, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_convert_document != null) {
                String cadenasql = "SELECT "
                        + "R.ID_RECEPTOR, "
                        + "R.ID_TIPO_CONTRIBUYENTE, "
                        + "R.NRODOCRECEP, "
                        + "R.NMBRECEP, "
                        + "R.CALLE, "
                        + "R.EMAIL, "
                        + "R.CODIGOPAIS "
                        + "FROM "
                        + "RECEPTOR R "
                        + "LEFT JOIN ENCABEZADO E ON (E.ID_RECEPTOR = R.ID_RECEPTOR) "
                        + "LEFT JOIN DOCUMENTO D ON (D.ID_ENCABEZADO = E.ID_ENCABEZADO) "
                        + "LEFT JOIN DTE DTE ON (DTE.ID_DOCUMENTO = D.ID_DOCUMENTO) "
                        + "LEFT JOIN CONVERT_DOCUMENT CD ON (DTE.ID_DOCUMENTO = CD.ID_DOCUMENTO AND DTE.ID_DTE = CD.ID_DTE) "
                        + "WHERE "
                        + "CD.ID_CONVERT_DOCUMENT=" + id_convert_document;

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
                    this.id_receptor = Integer.valueOf(vector_result[i][0]);
                    this.id_tipo_contribuyente = Integer.valueOf(vector_result[i][1]);
                    this.tax_id = vector_result[i][2];
                    this.nombre_cliente = vector_result[i][3];
                    this.direccion = vector_result[i][4];
                    this.correo = vector_result[i][5];
                    this.codigo_area = vector_result[i][6];
                }
                
                if(procesado.equals("SI")) {
                    this.btnGuardar = true;
                } else {
                    this.btnGuardar = false;
                }

                PrimeFaces.current().executeScript("PF('varReceptor').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_RECEPTOR FCR_DIALOG_RECEPTOR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_RECEPTOR FCR_DIALOG_RECEPTOR: " + ex.toString()));
        }
    }
    
    public void modificar_receptor () {
        try {
            Fcr_Servicio servicio = new Fcr_Servicio();
            String resultado = servicio.modificarReceptor(this.usuario.getUsuario(), this.id_receptor, this.id_tipo_contribuyente, this.tax_id, this.nombre_cliente, this.direccion, this.correo, this.codigo_area);
            
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(49), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Modificar receptor GO-SOCKET ID_RECEPTOR: " + this.id_receptor);
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado.substring(2)));
            }
            
            PrimeFaces.current().executeScript("PF('varReceptor').hide();");
        } catch(Exception ex) {
            System.out.println("ERROR MODIFICAR_RECEPTOR FCR_DIALOG_RECEPTOR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MODIFICAR_RECEPTOR FCR_DIALOG_RECEPTOR: " + ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getId_receptor() {
        return id_receptor;
    }

    public void setId_receptor(Integer id_receptor) {
        this.id_receptor = id_receptor;
    }

    public Integer getId_tipo_contribuyente() {
        return id_tipo_contribuyente;
    }

    public void setId_tipo_contribuyente(Integer id_tipo_contribuyente) {
        this.id_tipo_contribuyente = id_tipo_contribuyente;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigo_area() {
        return codigo_area;
    }

    public void setCodigo_area(String codigo_area) {
        this.codigo_area = codigo_area;
    }

    public List<SelectItem> getLst_tipo_contribuyente() {
        return lst_tipo_contribuyente;
    }

    public void setLst_tipo_contribuyente(List<SelectItem> lst_tipo_contribuyente) {
        this.lst_tipo_contribuyente = lst_tipo_contribuyente;
    }

    public Boolean getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(Boolean btnGuardar) {
        this.btnGuardar = btnGuardar;
    }
    
}
