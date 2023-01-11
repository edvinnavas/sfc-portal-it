package Bean.SVFiscalia;

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

@Named(value = "Reporte_Mensual_Medios_Electronicos_Uno")
@ViewScoped
public class Reporte_Mensual_Medios_Electronicos_Uno implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private List<RMME_Uno_List> lista_rmme;
    private RMME_Uno_List sel_rmme;
    private Integer empresa;
    private String titulo;

    public Reporte_Mensual_Medios_Electronicos_Uno() {
        this.lista_rmme = new ArrayList<>();
    }

    public void cargar_datos(Usuario usuario, Integer empresa) {
        try {
            this.usuario = usuario;
            this.empresa = empresa;

            switch (this.empresa) {
                case 1: {
                    this.titulo = "Reporte mensual medios electrónicos (UNO El Salvador).";
                    break;
                }
                case 2: {
                    this.titulo = "Reporte mensual medios electrónicos (ECSA El Salvador).";
                    break;
                }
            }

            String cadenasql = "SELECT "
                    + "ID_TRANS, "
                    + "TOKEN_VALIDO, "
                    + "TO_CHAR(FECHA_REPORTE_UIF,'dd/MM/yyyy') FECHA_REPORTE, "
                    + "CODIGO_MENSAJE, "
                    + "DESCRIPCIONMENSAJE, "
                    + "CODIGOTRANSACCION, "
                    + "REGISTROSPROCESADOS "
                    + "FROM "
                    + "SVF_TRANS_MEN_EL_ENCABEZADO "
                    + "WHERE "
                    + "EMPRESA = " + this.empresa + " "
                    + "ORDER BY "
                    + "TOKEN_VALIDO, "
                    + "TO_CHAR(FECHA_REPORTE_UIF,'dd/MM/yyyy')";
            Svf_Servicio servicio = new Svf_Servicio();
            List<String> resultado = servicio.reporte(cadenasql);

            Integer filas = resultado.size();
            Integer columnas = resultado.size();
            String[][] vector_result = new String[resultado.size()][columnas];
            for (Integer i = 0; i < resultado.size(); i++) {
                for (Integer j = 0; j < resultado.size(); j++) {
                    vector_result[i][j] = resultado.get(j);
                }
            }

            this.lista_rmme = new ArrayList<>();
            SimpleDateFormat formatoDate = new SimpleDateFormat("dd/MM/yyyy");
            for (Integer i = 1; i < filas; i++) {
                RMME_Uno_List rmme = new RMME_Uno_List(
                        Integer.valueOf(vector_result[i][0]),
                        vector_result[i][1],
                        formatoDate.parse(vector_result[i][2]),
                        Integer.valueOf(vector_result[i][3]),
                        vector_result[i][4],
                        vector_result[i][5],
                        Integer.valueOf(vector_result[i][6]));
                this.lista_rmme.add(rmme);
            }

            PrimeFaces.current().executeScript("PF('var_tblSVFiscaliaRMME').clearFilters();");

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void eliminar_trans() {
        try {
            if (this.sel_rmme != null) {
                if (this.sel_rmme.getCodigo_mensaje() != 100) {
                    Svf_Servicio servicio = new Svf_Servicio();
                    String resultado = servicio.eliminarTransaccion(Integer.valueOf(this.usuario.getId_usuario().toString()), this.sel_rmme.getId_trans(), 4);
                    if (resultado.trim().substring(0, 5).equals("EXITO")) {
                        this.cargar_datos(this.usuario, this.empresa);

                        // OBTENER FECHA ACTUAL.
                        Date fecha_actual = new Date();
                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                        // REGISTRAR EVENTO.
                        Evento evento = new Evento(new Long(32), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "UNO: Eliminar transacciones reporte mensual medios electrónicos: " + this.sel_rmme.getId_trans());
                        List<Evento> lista_eventos = new ArrayList<>();
                        lista_eventos.add(evento);
                        Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                        cliente_rest_evento.crear_evento(lista_eventos);

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "La transacción no puede ser eliminada, porque el valor del código de mensaje es 100."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro de la tabla de transacciones."));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<RMME_Uno_List> getLista_rmme() {
        return lista_rmme;
    }

    public void setLista_rmme(List<RMME_Uno_List> lista_rmme) {
        this.lista_rmme = lista_rmme;
    }

    public RMME_Uno_List getSel_rmme() {
        return sel_rmme;
    }

    public void setSel_rmme(RMME_Uno_List sel_rmme) {
        this.sel_rmme = sel_rmme;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
