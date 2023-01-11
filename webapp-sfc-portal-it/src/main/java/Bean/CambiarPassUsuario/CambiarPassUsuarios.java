package Bean.CambiarPassUsuario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Usuario;
import Entidad.Usuario_Lista;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import ClientRestService.Cliente_Rest_Drive;

@Named(value = "CambiarPassUsuarios")
@ViewScoped
public class CambiarPassUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    private List<Usuario_Lista> lista_usuario;
    private Usuario_Lista sel_usuario;

    public CambiarPassUsuarios() {
        this.lista_usuario = new ArrayList<>();
    }

    public void cargar_datos(Usuario usuario) {
        try {
            this.usuario = usuario;
            String cadenasql;
            if (Objects.equals(this.usuario.getId_rol(), Long.valueOf("1"))) {
                cadenasql = "SELECT "
                        + "a.id_usuario, "
                        + "a.usuario, "
                        + "b.nombre, "
                        + "if(a.activo=1,'SI','NO') activo, "
                        + "if(a.editable=1,'SI','NO') editable, "
                        + "if(a.eliminable=1,'SI','NO') eliminable "
                        + "FROM "
                        + "usuario a "
                        + "left join rol b on (a.id_rol=b.id_rol)";
            } else {
                cadenasql = "SELECT "
                        + "a.id_usuario, "
                        + "a.usuario, "
                        + "b.nombre, "
                        + "if(a.activo=1,'SI','NO') activo, "
                        + "if(a.editable=1,'SI','NO') editable, "
                        + "if(a.eliminable=1,'SI','NO') eliminable "
                        + "FROM "
                        + "usuario a "
                        + "left join rol b on (a.id_rol=b.id_rol)"
                        + "WHERE "
                        + "a.id_usuario=" + this.usuario.getId_usuario();
            }
            
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
