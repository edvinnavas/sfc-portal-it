package Bean.FelConfiguracion;

import ClientRestService.Cliente_Rest_Drive;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Evento;
import Entidad.Fel_Conf_List;
import Entidad.Usuario;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Fel;

@Named(value = "FelConfiguracion")
@ViewScoped
public class FelConfiguracion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ambiente;
    private Usuario usuario;

    private List<Fel_Conf_List> lista_conf;
    private Fel_Conf_List sel_conf;

    private List<SelectItem> lst_ambientes;

    public FelConfiguracion() {
        this.lista_conf = new ArrayList<>();
        this.lst_ambientes = new ArrayList<>();
    }

    public void cargar_datos(String ambiente, Usuario usuario) {
        try {
            this.ambiente = ambiente;
            this.usuario = usuario;

            this.lst_ambientes = new ArrayList<>();
            this.lst_ambientes.add(new SelectItem("1", "1"));
            this.lst_ambientes.add(new SelectItem("0", "0"));

            String cadenasql = "SELECT "
                    + "F.ID_AMBIENTE ID_AMBIENTE, "
                    + "CASE "
                    + "WHEN F.ID_AMBIENTE=1 THEN 'PRODDTA - AMBIENTE DE PRODUCCION' "
                    + "WHEN F.ID_AMBIENTE=2 THEN 'CRPDTA - AMBIENTE DE PRUEBAS' END DESCRIPCION, "
                    + "F.ACTIVO VALOR "
                    + "FROM "
                    + "FEL_AMBIENTE F";
            this.lista_conf = new ArrayList<>();
            Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_drive.drive_fel_energia(this.ambiente, cadenasql);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] col = lista_drive.get(i).split("♣");
                Fel_Conf_List fel_conf_list = new Fel_Conf_List(Long.valueOf(i), col[1], col[2]);
                this.lista_conf.add(fel_conf_list);
            }

            PrimeFaces.current().executeScript("PF('varTblFelConfiguracion').clearFilters();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            Cliente_Rest_Fel cliente_rest_fel = new Cliente_Rest_Fel("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_fel.modificar_ambiente(this.ambiente, this.sel_conf.getId_conf(), Integer.valueOf(newValue.toString()));
            this.cargar_datos(this.ambiente, this.usuario);

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(Long.valueOf(12), this.usuario.getId_usuario(), Long.valueOf(dateFormat_1.format(fecha_actual)), "FEL modificar ambiente carga JDE.");
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado));
        }
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Fel_Conf_List> getLista_conf() {
        return lista_conf;
    }

    public void setLista_conf(List<Fel_Conf_List> lista_conf) {
        this.lista_conf = lista_conf;
    }

    public Fel_Conf_List getSel_conf() {
        return sel_conf;
    }

    public void setSel_conf(Fel_Conf_List sel_conf) {
        this.sel_conf = sel_conf;
    }

    public List<SelectItem> getLst_ambientes() {
        return lst_ambientes;
    }

    public void setLst_ambientes(List<SelectItem> lst_ambientes) {
        this.lst_ambientes = lst_ambientes;
    }

}
