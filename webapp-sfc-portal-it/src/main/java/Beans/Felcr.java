package Beans;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.primefaces.PrimeFaces;

@ViewScoped
@Named(value = "Felcr")
@Getter
@Setter
public class Felcr implements Serializable {

    private static final long serialVersionUID = 1L;

    // Listado DTE's.
    private Entidades.UsuarioSesion usuario_sesion;
    private List<Entidades.RegTblDteCr> lst_reg_tbl_dtecr;
    private Entidades.RegTblDteCr sel_reg_tbl_dtecr;
    private Date fecha_facturacion;
    private String tabla;
    private List<SelectItem> lst_tabla;
    private String ambiente;
    // private String nombre_usuario;
    
    // Dialogo ConvertDocument.
    private String txtConverDocument;
    private String txtDocumentType;
    private String txtNoOrderE1;
    private String txtTipoOrdenE1;
    private String txtNoCompania;
    private String txtFechaDocumento;
    private String txtNoFactura;
    private String txtCodigoCliente;
    private String txtTaxIdReceptor;
    private String txtNombreReceptor;
    private String txtFechaEnvio;
    private String txtProcessResult;
    private String txtProcesado;

    // Dialogo Referencia.
    private String txtIdReferencia;
    private String txtIdDocumento;
    private String txtIdDocumentoTypeRef;
    private String txtFechaDocumentoRef;
    private String txtRazonRef;
    private Integer somIdCodigoRef;
    private List<SelectItem> lst_somIdCodigoRef;
    private String txtTipoDespacho;
    private String txtNoDocumentoRef;
    private String txtIdBatch;
    private String txtareaComenarioAdjunto;
    private String somIdTipoNotaCredito;
    private List<SelectItem> lst_somIdTipoNotaCredito;
    private Boolean btnGuardarReferenciaDisabled;
    
    @PostConstruct
    public void init() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            this.lst_reg_tbl_dtecr = new ArrayList<>();
            this.fecha_facturacion = new Date();
            this.lst_tabla = new ArrayList<>();
            this.lst_tabla.add(new SelectItem("F4211", "Facturación"));
            this.lst_tabla.add(new SelectItem("F42119", "Contabilidad"));
            this.filtrar_tabla();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: init(), ERRROR: " + ex.toString());
        }
    }

    public void cargar_vista(Entidades.UsuarioSesion usuario_sesion) {
        try {
            this.usuario_sesion = usuario_sesion;
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.setAttribute("usuario_sesion", this.usuario_sesion);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema.", "Vista-DTE-CR."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: cargar_vista(), ERRROR: " + ex.toString());
        }
    }

    public void filtrar_tabla() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String json_result = cliente_rest_api.lista_dtes(this.ambiente, dateFormat1.format(this.fecha_facturacion));

            Type lista_viaje_type = new TypeToken<List<Entidades.RegTblDteCr>>() {
            }.getType();
            
            this.lst_reg_tbl_dtecr = new Gson().fromJson(json_result, lista_viaje_type);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: filtrar_tabla(), ERRROR: " + ex.toString());
        }
    }
    
    public void enviar_gosocket() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            if (this.sel_reg_tbl_dtecr != null) {
                if (this.sel_reg_tbl_dtecr.getProcesado().trim().equals("NO")) {
                    String parametros = this.ambiente + "♣" + this.sel_reg_tbl_dtecr.getId_convert_document();
                    ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
                    String resultado = cliente_rest_api.gosocket(parametros);
                    String opcion = resultado.substring(0, 1);
                    if (opcion.equals("0")) {
                        this.filtrar_tabla();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "El documento fiscal ya fue enviado a GoSocket."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un documento fiscal."));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: enviar_gosocket(), ERRROR: " + ex.toString());
        }
    }

    public void extraer_docs_jde() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            Integer anio = this.fecha_facturacion.getYear() + 1900;
            Integer mes = this.fecha_facturacion.getMonth() + 1;
            Integer dia = this.fecha_facturacion.getDate();
            String parametros = this.ambiente + "♣" + this.usuario_sesion.getNombre_usuario() + "♣" + anio + "♣" + mes + "♣" + dia + "♣" + this.tabla;
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String resultado = cliente_rest_api.cargar_docs(parametros);
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                this.filtrar_tabla();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: extraer_docs_jde(), ERRROR: " + ex.toString());
        }
    }
    
    public void mostrar_document_convert() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String json_result = cliente_rest_api.felcr_obtener_document(this.ambiente, Long.valueOf(this.sel_reg_tbl_dtecr.getId_convert_document().toString()));
            
            Type reg_dgl_document_type = new TypeToken<Entidades.RegDglDocument>() {
            }.getType();
            
            Entidades.RegDglDocument reg_dgl_document = new Gson().fromJson(json_result, reg_dgl_document_type);
            this.txtConverDocument = reg_dgl_document.getId_convert_document().toString();
            this.txtDocumentType = reg_dgl_document.getDocument_type();
            this.txtNoOrderE1 = reg_dgl_document.getNo_orden_e1();
            this.txtTipoOrdenE1 = reg_dgl_document.getTipo_orden_e1();
            this.txtNoCompania = reg_dgl_document.getNo_compania();
            this.txtFechaDocumento = reg_dgl_document.getFecha_documento();
            this.txtNoFactura = reg_dgl_document.getNo_factura();
            this.txtCodigoCliente = reg_dgl_document.getAban8_d1();
            this.txtTaxIdReceptor = reg_dgl_document.getTax_id_receptor();
            this.txtNombreReceptor = reg_dgl_document.getNombre_receptor();
            this.txtFechaEnvio = reg_dgl_document.getFecha_envio();
            this.txtProcessResult = reg_dgl_document.getProcess_result();
            this.txtProcesado = reg_dgl_document.getProcesado();
            
            PrimeFaces.current().executeScript("PF('widvarConverDocument').show();");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: mostrar_document_convert(), ERRROR: " + ex.toString());
        }
    }
    
    public void mostrar_referencia() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String json_result = cliente_rest_api.obtener_cat_codigo_ref(this.ambiente);

            Type cat_lst_codigo_ref_type = new TypeToken<List<Entidades.Cat_Codigo_Ref>>() {
            }.getType();

            List<Entidades.Cat_Codigo_Ref> cat_codigo_ref = new Gson().fromJson(json_result, cat_lst_codigo_ref_type);

            this.lst_somIdCodigoRef = new ArrayList<>();
            for (Integer i = 0; i < cat_codigo_ref.size(); i++) {
                this.lst_somIdCodigoRef.add(new SelectItem(cat_codigo_ref.get(i).getID_CODIGO_REF(), cat_codigo_ref.get(i).getDESCRIPTION()));
            }
            
            this.lst_somIdTipoNotaCredito = new ArrayList<>();
            this.lst_somIdTipoNotaCredito.add(new SelectItem("TOTAL","TOTAL"));
            this.lst_somIdTipoNotaCredito.add(new SelectItem("PARCIAL","PARCIAL"));
            
            json_result = cliente_rest_api.felcr_obtener_referencia(this.ambiente, Long.valueOf(this.sel_reg_tbl_dtecr.getId_documento().toString()));

            Type reg_dgl_referencia_type = new TypeToken<Entidades.RegDglReferencia>() {
            }.getType();

            Entidades.RegDglReferencia reg_dgl_referencia = new Gson().fromJson(json_result, reg_dgl_referencia_type);
            this.txtIdReferencia = reg_dgl_referencia.getId_referencia().toString();
            this.txtIdDocumento = reg_dgl_referencia.getId_documento().toString();
            this.txtIdDocumentoTypeRef = reg_dgl_referencia.getId_document_type_ref().toString();
            this.txtFechaDocumentoRef = reg_dgl_referencia.getFecha_documento_ref();
            this.txtRazonRef = reg_dgl_referencia.getRazonref();
            this.somIdCodigoRef = reg_dgl_referencia.getTipo_despacho();
            this.txtTipoDespacho = reg_dgl_referencia.getTipo_despacho().toString();
            this.txtNoDocumentoRef = reg_dgl_referencia.getNo_documento_ref();
            this.txtIdBatch = reg_dgl_referencia.getId_batch();
            this.txtareaComenarioAdjunto = reg_dgl_referencia.getComentario_adjunto();
            this.somIdTipoNotaCredito = reg_dgl_referencia.getTipo_nota_credito();
            if(this.sel_reg_tbl_dtecr.getProcesado().equals("SI")) {
                this.btnGuardarReferenciaDisabled = true;
            } else {
                this.btnGuardarReferenciaDisabled = false;
            }

            PrimeFaces.current().executeScript("PF('widvarReferencia').show();");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: mostrar_referencia(), ERRROR: " + ex.toString());
        }
    }
    
    public void guardar_referencia() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            String parametros = this.ambiente + "♣" + this.usuario_sesion.getNombre_usuario() + "♣" + this.txtIdReferencia + "♣" + this.txtNoDocumentoRef + "♣" + this.txtIdBatch + "♣" + this.txtareaComenarioAdjunto + "♣" + this.somIdCodigoRef + "♣" + this.somIdTipoNotaCredito;
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String resultado = cliente_rest_api.modificar_referencia(parametros);
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: guardar_referencia(), ERRROR: " + ex.toString());
        }
    }
    
    public void anular_documento() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            this.usuario_sesion = (Entidades.UsuarioSesion) session.getAttribute("usuario_sesion");
            
            if(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath().trim().equals("/apps")) {
                this.ambiente = "PD";
            } else {
                this.ambiente = "PY";
            }
            
            String parametros = this.ambiente + "♣" + this.usuario_sesion.getNombre_usuario() + "♣" + this.sel_reg_tbl_dtecr.getId_convert_document() + "♣" + this.sel_reg_tbl_dtecr.getRefacturacion();
            ClientesRest.ClienteRestApi cliente_rest_api = new ClientesRest.ClienteRestApi();
            String resultado = cliente_rest_api.anular_documento(parametros);
            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                this.filtrar_tabla();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema.", ex.toString()));
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: anular_documento(), ERRROR: " + ex.toString());
        }
    }
    
}
