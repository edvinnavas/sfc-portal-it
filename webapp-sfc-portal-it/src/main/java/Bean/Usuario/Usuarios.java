package Bean.Usuario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Evento;
import Entidad.Usuario;
import Entidad.Usuario_Lista;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import ClientRestService.Cliente_Rest_Drive;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Usuario;

@Named(value = "Usuarios")
@ViewScoped
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private List<Usuario_Lista> lista_usuario;
    private Usuario_Lista sel_usuario;

    public Usuarios() {
        this.lista_usuario = new ArrayList<>();
    }

    public void cargar_datos(Usuario usuario) {
        try {
            this.usuario = usuario;

            String cadenasql = "SELECT "
                    + "a.id_usuario, "
                    + "a.usuario, "
                    + "b.nombre, "
                    + "if(a.activo=1,'SI','NO') activo, "
                    + "if(a.editable=1,'SI','NO') editable, "
                    + "if(a.eliminable=1,'SI','NO') eliminable "
                    + "FROM "
                    + "usuario a "
                    + "left join rol b on (a.id_rol=b.id_rol)";
            this.lista_usuario = new ArrayList<>();
            Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_drive.drive(cadenasql);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] col = lista_drive.get(i).split("♣");
                Usuario_Lista usuario_lista = new Usuario_Lista(Long.valueOf(col[0]), col[1], col[2], col[3], col[4], col[5]);
                this.lista_usuario.add(usuario_lista);
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void reset_pass() {
        try {
            if (this.sel_usuario != null) {
                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                String resultado = cliente_rest_usuario.reset_pass_usuario(this.sel_usuario.getId_usuario());

                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(9), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Reset contraseña usuario: " + this.sel_usuario.getId_usuario());
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un usuario."));
            }

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: reset_pass ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void desactivar() {
        try {
            if (this.sel_usuario != null) {
                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                String resultado = cliente_rest_usuario.desactivar(this.sel_usuario.getId_usuario());
                this.cargar_datos(this.usuario);

                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(10), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Desactivar usuario: " + this.sel_usuario.getId_usuario());
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un usuario."));
            }

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: reset_pass ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void activar() {
        try {
            if (this.sel_usuario != null) {
                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                String resultado = cliente_rest_usuario.activar(this.sel_usuario.getId_usuario());
                this.cargar_datos(this.usuario);

                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(11), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Activar usuario: " + this.sel_usuario.getId_usuario());
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un usuario."));
            }

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: reset_pass ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public List<Usuario_Lista> getLista_usuario() {
        return lista_usuario;
    }

    public void setLista_usuario(List<Usuario_Lista> lista_usuario) {
        this.lista_usuario = lista_usuario;
    }

    public Usuario_Lista getSel_usuario() {
        return sel_usuario;
    }

    public void setSel_usuario(Usuario_Lista sel_usuario) {
        this.sel_usuario = sel_usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
