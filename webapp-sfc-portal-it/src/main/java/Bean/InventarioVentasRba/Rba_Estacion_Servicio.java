package Bean.InventarioVentasRba;

import Entidad.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import ClientRestService.Rba_Servicio;

@Named(value = "Rba_Estacion_Servicio")
@ViewScoped
public class Rba_Estacion_Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private List<Rba_Estaciones_Servicio_List> lista_estaciones;
    private Rba_Estaciones_Servicio_List sel_estaciones;

    public Rba_Estacion_Servicio() {
        this.lista_estaciones = new ArrayList<>();
    }

    public void cargar_datos(Usuario usuario) {
        try {
            this.usuario = usuario;

            String cadenasql = "SELECT "
                    + "D.ID_ESTACION_SERVICIO, "
                    + "D.COD_E1, "
                    + "D.COD_ENVOY, "
                    + "D.COD_PAYWARE, "
                    + "D.NOMBRE_ESTACION, "
                    + "D.DCTO_E1, "
                    + "D.MCU_E1, "
                    + "D.SHAN_E1, "
                    + "D.KCOO_E1, "
                    + "D.ROUT_E1, "
                    + "D.ACTIVO "
                    + "FROM "
                    + "DIM_ESTACION_SERVICIO D ";

            Rba_Servicio servicio = new Rba_Servicio();
            List<String> resultado = servicio.reporte(cadenasql);

            Integer filas = resultado.size();
            Integer columnas = resultado.size();
            String[][] vector_result = new String[resultado.size()][columnas];
            for (Integer i = 0; i < resultado.size(); i++) {
                for (Integer j = 0; j < resultado.size(); j++) {
                    vector_result[i][j] = resultado.get(j);
                }
            }

            this.lista_estaciones = new ArrayList<>();
            for (Integer i = 1; i < filas; i++) {
                Rba_Estaciones_Servicio_List comp = new Rba_Estaciones_Servicio_List(
                        Integer.valueOf(vector_result[i][0]),
                        vector_result[i][1],
                        vector_result[i][2],
                        vector_result[i][3],
                        vector_result[i][4],
                        vector_result[i][5],
                        vector_result[i][6],
                        vector_result[i][7],
                        vector_result[i][8],
                        vector_result[i][9],
                        vector_result[i][10]);
                this.lista_estaciones.add(comp);
            }

            PrimeFaces.current().executeScript("PF('var_tbl_estaciones').clearFilters();");

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void desactivar() {
        try {
            if (this.sel_estaciones != null) {
                Rba_Servicio servicio = new Rba_Servicio();
                String resultado = servicio.desactivarEstacionServicio(this.sel_estaciones.getId_estacion_servicio());
                this.cargar_datos(this.usuario);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar una Estación de Servicio."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: desactivar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void activar() {
        try {
            if (this.sel_estaciones != null) {
                Rba_Servicio servicio = new Rba_Servicio();
                String resultado = servicio.activarEstacionServicio(this.sel_estaciones.getId_estacion_servicio());
                this.cargar_datos(this.usuario);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar una Estación de Servicio."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: activar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Rba_Estaciones_Servicio_List> getLista_estaciones() {
        return lista_estaciones;
    }

    public void setLista_estaciones(List<Rba_Estaciones_Servicio_List> lista_estaciones) {
        this.lista_estaciones = lista_estaciones;
    }

    public Rba_Estaciones_Servicio_List getSel_estaciones() {
        return sel_estaciones;
    }

    public void setSel_estaciones(Rba_Estaciones_Servicio_List sel_estaciones) {
        this.sel_estaciones = sel_estaciones;
    }

}
