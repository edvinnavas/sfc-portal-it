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

@Named(value = "Reporte_Diario_Efectivo_Uno")
@ViewScoped
public class Reporte_Diario_Efectivo_Uno implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private List<RDE_Uno_List> lista_rde;
    private RDE_Uno_List sel_rde;
    private Integer empresa;
    private String titulo;

    public Reporte_Diario_Efectivo_Uno() {
        this.lista_rde = new ArrayList<>();
    }

    public void cargar_datos(Usuario usuario, Integer empresa) {
        try {
            this.usuario = usuario;
            this.empresa = empresa;

            switch (this.empresa) {
                case 1: {
                    this.titulo = "Reporte diario de efectivo (UNO El Salvador).";
                    break;
                }
                case 2: {
                    this.titulo = "Reporte diario de efectivo (ECSA El Salvador).";
                    break;
                }
            }

            String cadenasql = "SELECT "
                    + "ID_TRANS, "
                    + "TOKEN_VALIDO, "
                    + "TO_CHAR(FECHA_REPORTE,'dd/MM/yyyy') FECHA_REPROTE, "
                    + "CODIGO_MENSAJE, "
                    + "DESCRIPCIONMENSAJE, "
                    + "CODIGOTRANSACCION, "
                    + "REGISTROSPROCESADOS "
                    + "FROM "
                    + "SVF_TRANS_DIARIO_ENCABEZADO "
                    + "WHERE "
                    + "EMPRESA = " + this.empresa + " "
                    + "ORDER BY "
                    + "TOKEN_VALIDO, "
                    + "TO_CHAR(FECHA_REPORTE,'dd/MM/yyyy')";

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

            this.lista_rde = new ArrayList<>();
            SimpleDateFormat formatoDate = new SimpleDateFormat("dd/MM/yyyy");
            for (Integer i = 1; i < filas; i++) {
                RDE_Uno_List rde = new RDE_Uno_List(
                        Integer.valueOf(vector_result[i][0]),
                        vector_result[i][1],
                        formatoDate.parse(vector_result[i][2]),
                        Integer.valueOf(vector_result[i][3]),
                        vector_result[i][4],
                        vector_result[i][5],
                        Integer.valueOf(vector_result[i][6]));
                this.lista_rde.add(rde);
            }

            PrimeFaces.current().executeScript("PF('var_tblSVFiscaliaRDE').clearFilters();");

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void eliminar_trans() {
        try {
            if (this.sel_rde != null) {
                if (this.sel_rde.getCodigo_mensaje() != 100) {
                    Svf_Servicio servicio = new Svf_Servicio();
                    String resultado = servicio.eliminarTransaccion(Integer.valueOf(this.usuario.getId_usuario().toString()), this.sel_rde.getId_trans(), 1);
                    if (resultado.trim().substring(0, 5).equals("EXITO")) {
                        this.cargar_datos(this.usuario, this.empresa);

                        // OBTENER FECHA ACTUAL.
                        Date fecha_actual = new Date();
                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                        // REGISTRAR EVENTO.
                        Evento evento = new Evento(Long.valueOf(26), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "UNO: Eliminar transacciones reporte diario de efectivo: " + this.sel_rde.getId_trans());
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

    public List<RDE_Uno_List> getLista_rde() {
        return lista_rde;
    }

    public void setLista_rde(List<RDE_Uno_List> lista_rde) {
        this.lista_rde = lista_rde;
    }

    public RDE_Uno_List getSel_rde() {
        return sel_rde;
    }

    public void setSel_rde(RDE_Uno_List sel_rde) {
        this.sel_rde = sel_rde;
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
