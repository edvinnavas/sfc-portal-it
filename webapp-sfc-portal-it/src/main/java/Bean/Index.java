package Bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Evento;
import Entidad.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import ClientRestService.Cliente_Rest_Drive;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Usuario;

@Named(value = "Index")
@SessionScoped
public class Index implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ambiente;
    private String usuario;
    private String contrasena;

    public Index() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public String login() {
        String resultado;

        try {
            Usuario n_usuario = new Usuario(Long.valueOf("0"), this.usuario, "-", "-", Long.valueOf("0"), this.contrasena, "-", 0, 0, 0, Long.valueOf("0"));
            List<Usuario> lista_usuario = new ArrayList<>();
            lista_usuario.add(n_usuario);
            Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
            String respuesta = cliente_rest_usuario.login2(lista_usuario);
            String[] mensaje = respuesta.split(",");
            if (mensaje[0].equals("1")) {
                // OBTENER ID_USUARIO.
                Long id_usuario = Long.valueOf("0");
                String cadenasql = "select u.id_usuario from usuario u where u.usuario='" + this.usuario + "'";
                Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                String jsonString = cliente_rest_drive.drive(cadenasql);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] usuario_rest = lista_drive.get(i).split("♣");
                    id_usuario = Long.valueOf(usuario_rest[0]);
                }
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf("1"), id_usuario, Long.valueOf(dataFormat.format(fecha_actual)), "Acceso al sistema.");
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);

                this.ambiente = "PY";
                resultado = "menu";
            } else {
                resultado = "index";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", mensaje[1]));
            }
        } catch (Exception ex) {
            resultado = "index";
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: login ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }

        return resultado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

}
