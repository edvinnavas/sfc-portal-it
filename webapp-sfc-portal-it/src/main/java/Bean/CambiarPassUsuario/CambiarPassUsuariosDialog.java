package Bean.CambiarPassUsuario;

import Entidad.Evento;
import Entidad.Usuario;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Usuario;

@Named(value = "CambiarPassUsuariosDialog")
@ViewScoped
public class CambiarPassUsuariosDialog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private Long id_usuario;
    private String contrasena;
    private String recontrasena;

    public CambiarPassUsuariosDialog() {
        
    }
    
    public void mostrar_dialog_modificar_usuario(Usuario usuario, Long id_usuario) {
        try {
            if(id_usuario != null) {
                this.usuario = usuario;
                this.id_usuario = id_usuario;

                this.contrasena = "";
                this.recontrasena = "";
                
                PrimeFaces.current().executeScript("PF('dialogCambiarPassUsuariosVar').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un usuario del listado."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_modificar_usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }
    
    public void modificar_usuario() {
        try {
            if(this.contrasena.equals(this.recontrasena)) {
                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest","R3st-T3rR@");
                String resultado = cliente_rest_usuario.cambiar_pass_usuario(this.id_usuario, this.contrasena);

                PrimeFaces.current().executeScript("PF('dialogCambiarPassUsuariosVar').hide();");
                PrimeFaces.current().executeScript("PF('varTblCambiarPassUsuarios').clearFilters();");
                
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(new Long(20), this.usuario.getId_usuario(), Long.parseLong(dataFormat.format(fecha_actual)), "Cambiar constraseña: " + this.id_usuario);
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Las contraseñas deben coincidir."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Modificar_Usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRecontrasena() {
        return recontrasena;
    }

    public void setRecontrasena(String recontrasena) {
        this.recontrasena = recontrasena;
    }
    
}
