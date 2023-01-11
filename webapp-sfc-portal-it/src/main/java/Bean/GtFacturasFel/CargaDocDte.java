package Bean.GtFacturasFel;

import ClientRestService.Cliente_Rest_Drive;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Dte_Lista;
import Entidad.Evento;
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
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Fel;

@Named(value = "CargaDocDte")
@ViewScoped
public class CargaDocDte implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ambiente;
    private Usuario usuario;
    private List<Dte_Lista> lista_dte;
    private Dte_Lista sel_dte;
    private String kcoo_compania_jde;
    private Date fecha_inicial;
    private Date fecha_final;
    private String tipo_documento;
    private List<SelectItem> lst_tipo_documento;
    private String ambiente_jde;
    private String ambiente_jde_uni;

    public CargaDocDte() {
        this.lista_dte = new ArrayList<>();
        this.fecha_inicial = new Date();
        this.fecha_final = new Date();
        this.lst_tipo_documento = new ArrayList<>();
    }

    public void cargar_datos(String ambiente, Usuario usuario, String kcoo_compania_jde) {
        try {
            this.ambiente = ambiente;
            this.usuario = usuario;
            this.kcoo_compania_jde = kcoo_compania_jde;

            String nombre_empresa = "";
            switch (this.kcoo_compania_jde) {
                case "02100": {
                    nombre_empresa = "XACBAL";
                    break;
                }
                case "02101": {
                    nombre_empresa = "ELGUA";
                    break;
                }
                case "02102": {
                    nombre_empresa = "TRESA";
                    break;
                }
                case "02103": {
                    nombre_empresa = "COMERCIA";
                    break;
                }
                case "02104": {
                    nombre_empresa = "MERSA";
                    break;
                }
            }

            this.lst_tipo_documento = new ArrayList<>();
            this.lst_tipo_documento.add(new SelectItem("FACT", "FACT"));
            this.lst_tipo_documento.add(new SelectItem("NCRE", "NCRE"));
            this.lst_tipo_documento.add(new SelectItem("NDEB", "NDEB"));

            String filtro_ambiente = "";
            String cadenasql = "SELECT F.AMBIENTE FROM FEL_AMBIENTE F WHERE F.ACTIVO=1";
            Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_drive.drive_fel_energia(this.ambiente, cadenasql);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] col = lista_drive.get(i).split("♣");
                filtro_ambiente = col[0];
                this.ambiente_jde = "FEL | GUATEFACTURAS | EMPRESA: " + nombre_empresa + " | AMBIENTE-JDE: " + filtro_ambiente;
                this.ambiente_jde_uni = filtro_ambiente;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("dd/MM/yyyy");
            cadenasql = "SELECT "
                    + "A.ID_DTE ID, "
                    + "A.AN8_CLIENTE_JDE AB_CLIENTE, "
                    + "A.NOMBRE_CLIENTE_JDE CLIENTE, "
                    + "A.DOCO_NO_ORDEN_JDE NO_ORDEN, "
                    + "(SELECT B.ID_TIPO_DOCUMENTO FROM FEL_CAT_TIPO_DOCUMENTO B WHERE B.CODIGO_JDE=A.DCTO_TIPO_ORDEN_JDE) TIPO_ORDEN, "
                    + "A.DOC_NO_DOCUMENTO_JDE NO_DOCUMENTO, "
                    + "TO_CHAR(TO_DATE(A.FECHA_DOCUMENTO,'yyyyMMdd'),'dd/MM/yyyy') FECHA_DOCUMENTO, "
                    + "A.ENVIADO ENVIADO, "
                    + "A.AUTORIZADO AUTORIZADO, "
                    + "A.AMBIENTE AMBIENTE, "
                    + "A.OBSERVACION OBSERVACION "
                    + "FROM "
                    + "FEL_DTE A "
                    + "WHERE "
                    + "A.KCOO_COMPANIA_JDE='" + this.kcoo_compania_jde + "' AND "
                    + "A.AMBIENTE='" + filtro_ambiente + "' AND "
                    + "A.FECHA_DOCUMENTO BETWEEN " + dateFormat.format(this.fecha_inicial) + " AND " + dateFormat.format(this.fecha_final);
            this.lista_dte = new ArrayList<>();
            jsonString = cliente_rest_drive.drive_fel_energia(this.ambiente, cadenasql);
            listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] col = lista_drive.get(i).split("♣");
                Dte_Lista dte_lista = new Dte_Lista(Long.valueOf(col[0]), Integer.valueOf(col[1]), col[2], Integer.valueOf(col[3]), col[4], Integer.valueOf(col[5]), dateFormat_1.parse(col[6]), col[7], col[8], col[9], col[10]);
                this.lista_dte.add(dte_lista);
            }

            PrimeFaces.current().executeScript("PF('varTblCargaDocDte').clearFilters();");

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void jde_to_fel() {
        try {
            SimpleDateFormat dateFormat_3 = new SimpleDateFormat("yyyyMMdd");
            Cliente_Rest_Fel cliente_rest_fel = new Cliente_Rest_Fel("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_fel.jdetofel(this.ambiente, this.kcoo_compania_jde, Long.valueOf(dateFormat_3.format(this.fecha_inicial)), Long.valueOf(dateFormat_3.format(this.fecha_final)));
            this.cargar_datos(this.ambiente, this.usuario, this.kcoo_compania_jde);

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            SimpleDateFormat dateFormat_2 = new SimpleDateFormat("dd/MM/yyyy");
            Evento evento = new Evento(Long.valueOf(15), this.usuario.getId_usuario(), Long.valueOf(dateFormat_1.format(fecha_actual)), "Carga documentos DTE: (" + dateFormat_2.format(this.fecha_inicial) + " - " + dateFormat_2.format(this.fecha_final) + ")");
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_jde ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void desmarcar() {
        try {
            if (this.sel_dte != null) {
                if (this.sel_dte.getAutorizado().trim().equals("NO")) {
                    Cliente_Rest_Fel cliente_rest_fel = new Cliente_Rest_Fel("UserTerraRest", "R3st-T3rR@");
                    String resultado = cliente_rest_fel.desmarcar(this.ambiente, this.sel_dte.getId_dte());
                    this.cargar_datos(this.ambiente, this.usuario, this.kcoo_compania_jde);

                    // OBTENER FECHA ACTUAL.
                    Date fecha_actual = new Date();
                    SimpleDateFormat dateFormat_1 = new SimpleDateFormat("yyyyMMddHHmmss");

                    // REGISTRAR EVENTO.
                    Evento evento = new Evento(Long.valueOf(19), this.usuario.getId_usuario(), Long.valueOf(dateFormat_1.format(fecha_actual)), "Desmarcar DTE: (" + this.sel_dte.getNo_orden() + " - " + this.sel_dte.getTipo_orden() + ")");
                    List<Evento> lista_eventos = new ArrayList<>();
                    lista_eventos.add(evento);
                    Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                    cliente_rest_evento.crear_evento(lista_eventos);

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "No puede desmarcar una documento AUTORIZADO."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un documento."));
            }

        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_jde ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Dte_Lista> getLista_dte() {
        return lista_dte;
    }

    public void setLista_dte(List<Dte_Lista> lista_dte) {
        this.lista_dte = lista_dte;
    }

    public Dte_Lista getSel_dte() {
        return sel_dte;
    }

    public void setSel_dte(Dte_Lista sel_dte) {
        this.sel_dte = sel_dte;
    }

    public Date getFecha_inicial() {
        return fecha_inicial;
    }

    public void setFecha_inicial(Date fecha_inicial) {
        this.fecha_inicial = fecha_inicial;
    }

    public Date getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(Date fecha_final) {
        this.fecha_final = fecha_final;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public List<SelectItem> getLst_tipo_documento() {
        return lst_tipo_documento;
    }

    public void setLst_tipo_documento(List<SelectItem> lst_tipo_documento) {
        this.lst_tipo_documento = lst_tipo_documento;
    }

    public String getAmbiente_jde() {
        return ambiente_jde;
    }

    public void setAmbiente_jde(String ambiente_jde) {
        this.ambiente_jde = ambiente_jde;
    }

    public String getAmbiente_jde_uni() {
        return ambiente_jde_uni;
    }

    public void setAmbiente_jde_uni(String ambiente_jde_uni) {
        this.ambiente_jde_uni = ambiente_jde_uni;
    }

    public String getKcoo_compania_jde() {
        return kcoo_compania_jde;
    }

    public void setKcoo_compania_jde(String kcoo_compania_jde) {
        this.kcoo_compania_jde = kcoo_compania_jde;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

}
