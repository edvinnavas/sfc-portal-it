package Bean.EcsaCrGoSocket;

import Entidad.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import ClientRestService.Fcr_Servicio;

@Named(value = "Fcr_Dialog_Detalle")
@ViewScoped
public class Fcr_Dialog_Detalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    
    List<Fcr_Detalle_List> lista_detalle;
    Fcr_Detalle_List sel_detalle;

    public void mostrar_dialog_detalle(Integer id_document, Usuario usuario) {
        try {
            this.usuario = usuario;
            if (id_document != null) {
                String cadenasql = "SELECT "
                        + "D.ID_DETALLE ID_DETALLE, "
                        + "D.ID_DOCUMENTO ID_DOCUMENTO, "
                        + "D.ID_TIPO_CODIGO_PRODUCTO TIPO_CODIGO_PRODUCTO, "
                        + "D.VLRCODIGO CODIGO_PRODUCTO, "
                        + "D.DSCITEM DETALLE_PRODUCTO, "
                        + "D.QTYITEM CANTIDAD, "
                        + "D.UNMDITEM UNIDAD_MEDIDA, "
                        + "D.UNIDADMEDIDACOMERCIAL UNIDAD_MEDIDA_COMERCIAL, "
                        + "D.PRCNETOITEM PRECIO_UNITARIO, "
                        + "D.MNTDSCTO MONTO_DESCUENTO, "
                        + "D.MONTOBRUTOITEM MONTO_TOTAL, "
                        + "D.MONTONETOITEM SUBTOTAL, "
                        + "D.MONTOTOTALITEM MONTO_TOTAL_LINEA, "
                        + "D.NROCTAPREDIAL NATURALEZA_DESCUENTO, "
                        + "D.IMPUESTO PORCENTAJE_IMPUESTO, "
                        + "D.EXENTO EXENTO, "
                        + "D.CABYS CABYS "
                        + "FROM "
                        + "DETALLE D "
                        + "WHERE "
                        + "D.ID_DOCUMENTO IN (SELECT CON.ID_DOCUMENTO FROM CONVERT_DOCUMENT CON WHERE CON.ID_CONVERT_DOCUMENT=" + id_document + ")";

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

                this.lista_detalle = new ArrayList<>();
                for (Integer i = 1; i < filas; i++) {
                    Fcr_Detalle_List fcr_detalle_list = new Fcr_Detalle_List(
                            i,
                            Integer.valueOf(vector_result[i][0]),
                            Integer.valueOf(vector_result[i][1]),
                            Integer.valueOf(vector_result[i][2]),
                            vector_result[i][3],
                            vector_result[i][4],
                            Double.valueOf(vector_result[i][5]),
                            vector_result[i][6],
                            vector_result[i][7],
                            Double.valueOf(vector_result[i][8]),
                            Double.valueOf(vector_result[i][9]),
                            Double.valueOf(vector_result[i][10]),
                            Double.valueOf(vector_result[i][11]),
                            Double.valueOf(vector_result[i][12]),
                            vector_result[i][13],
                            Integer.valueOf(vector_result[i][14]),
                            vector_result[i][15],
                            vector_result[i][16]);
                    this.lista_detalle.add(fcr_detalle_list);
                }

                PrimeFaces.current().executeScript("PF('varDetalle').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un registro."));
            }
        } catch (Exception ex) {
            System.out.println("ERROR MOSTRAR_DIALOG_DETALLE FCR_DIALOG_DETALLE: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", "ERROR MOSTRAR_DIALOG_DETALLE FCR_DIALOG_DETALLE: " + ex.toString()));
        }
    }

    public List<Fcr_Detalle_List> getLista_detalle() {
        return lista_detalle;
    }

    public void setLista_detalle(List<Fcr_Detalle_List> lista_detalle) {
        this.lista_detalle = lista_detalle;
    }

    public Fcr_Detalle_List getSel_detalle() {
        return sel_detalle;
    }

    public void setSel_detalle(Fcr_Detalle_List sel_detalle) {
        this.sel_detalle = sel_detalle;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
