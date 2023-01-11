package Bean.Rol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Rol_Lista;
import Entidad.Usuario;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import ClientRestService.Cliente_Rest_Drive;

@Named(value = "Roles")
@ViewScoped
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private List<Rol_Lista> lista_rol;
    private Rol_Lista sel_rol;

    public Roles() {
        this.lista_rol = new ArrayList<>();
    }

    public void cargar_datos(Usuario usuario) {
        try {
            this.usuario = usuario;

            String cadenasql = "SELECT "
                    + "a.id_rol, "
                    + "a.nombre, "
                    + "if(a.activo=1,'SI','NO') activo, "
                    + "if(a.editable=1,'SI','NO') editable, "
                    + "if(a.eliminable=1,'SI','NO') eliminable "
                    + "FROM "
                    + "rol a";
            this.lista_rol = new ArrayList<>();
            Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_drive.drive(cadenasql);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] col = lista_drive.get(i).split("♣");
                Rol_Lista rol_lista = new Rol_Lista(Long.parseLong(col[0]), col[1], col[2], col[3], col[4]);
                this.lista_rol.add(rol_lista);
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public List<Rol_Lista> getLista_rol() {
        return lista_rol;
    }

    public void setLista_rol(List<Rol_Lista> lista_rol) {
        this.lista_rol = lista_rol;
    }

    public Rol_Lista getSel_rol() {
        return sel_rol;
    }

    public void setSel_rol(Rol_Lista sel_rol) {
        this.sel_rol = sel_rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
