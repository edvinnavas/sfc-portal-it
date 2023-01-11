package Bean.EcsaCrGoSocket;

import Entidad.EcsaCrDocumentoList;
import Entidad.Evento;
import Entidad.Usuario;
import java.io.Serializable;
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
import ClientRestService.Fcr_Servicio;

@Named(value = "EcsaCrDocumentoFiscales")
@ViewScoped
public class EcsaCrDocumentoFiscales implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private List<EcsaCrDocumentoList> lista_documentos;
    private EcsaCrDocumentoList sel_documentos;

    private List<SelectItem> lst_tabla;
    
    private Date fecha_facturacion;
    private String tabla;
    
    public EcsaCrDocumentoFiscales() {
        this.lst_tabla = new ArrayList<>();
        this.lista_documentos = new ArrayList<>();
        this.fecha_facturacion = new Date();
    }

    public void cargar_datos(Usuario usuario) {
        try {
            this.usuario = usuario;
            
            this.lst_tabla = new ArrayList<>();
            this.lst_tabla.add(new SelectItem("F4211", "FACTURACIÓN"));
            this.lst_tabla.add(new SelectItem("F42119", "CONTABILIDAD"));
            
            Integer anio = this.fecha_facturacion.getYear() + 1900;
            Integer mes = this.fecha_facturacion.getMonth() + 1;
            Integer dia = this.fecha_facturacion.getDate();

            String anio_letra = "";
            if (anio < 10) {
                anio_letra = "0" + anio.toString();
            } else {
                anio_letra = anio.toString();
            }

            String mes_letra = "";
            if (mes < 10) {
                mes_letra = "0" + mes.toString();
            } else {
                mes_letra = mes.toString();
            }

            String dia_letra = "";
            if (dia < 10) {
                dia_letra = "0" + dia.toString();
            } else {
                dia_letra = dia.toString();
            }
            
            String cadenasql = "SELECT "
                    + "C.ID_CONVERT_DOCUMENT, "
                    + "C.SYNC_POINT, "
                    + "C.PASSWORDS, "
                    + "C.ID_DOCUMENT_TYPE, "
                    + "C.ID_DTE, "
                    + "C.ID_DOCUMENTO, "
                    + "C.CONTENIDOTC, "
                    + "C.NO_ORDEN_E1, "
                    + "C.TIPO_ORDEN_E1, "
                    + "C.NO_COMPANIA, "
                    + "SUBSTR(C.FECHA_DOCUMENTO,7,2) || '/' || SUBSTR(C.FECHA_DOCUMENTO,5,2) || '/' || SUBSTR(C.FECHA_DOCUMENTO,0,4) || ' ' || SUBSTR(C.FECHA_DOCUMENTO,9,2) || ':' || SUBSTR(C.FECHA_DOCUMENTO,11,2) || ':' || SUBSTR(C.FECHA_DOCUMENTO,13,2) FECHA_DOCUMENTO, "
                    + "C.NO_FACTURA, "
                    + "C.TAX_ID_RECEPTOR, "
                    + "C.NOMBRE_RECEPTOR, "
                    + "SUBSTR(C.FECHA_ENVIO,7,2) || '/' || SUBSTR(C.FECHA_ENVIO,5,2) || '/' || SUBSTR(C.FECHA_ENVIO,0,4) || ' ' || SUBSTR(C.FECHA_ENVIO,9,2) || ':' || SUBSTR(C.FECHA_ENVIO,11,2) || ':' || SUBSTR(C.FECHA_ENVIO,13,2) FECHA_ENVIO, "
                    + "C.PROCESS_RESULT, "
                    + "C.PROCESADO, " 
                    + "CASE WHEN R.ID_DOCUMENT_TYPE_REF=10 THEN 'SI' ELSE 'NO' END REFACTURACION "
                    + "FROM "
                    + "CONVERT_DOCUMENT C "
                    + "LEFT JOIN REFERENCIA R ON (C.ID_DOCUMENTO=R.ID_DOCUMENTO) "
                    + "WHERE "
                    + "SUBSTR(C.FECHA_DOCUMENTO,0,4) = '" + anio_letra + "' AND "
                    + "SUBSTR(C.FECHA_DOCUMENTO,5,2) = '" + mes_letra + "' AND "
                    + "SUBSTR(C.FECHA_DOCUMENTO,7,2) = '" + dia_letra + "'";

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
            
            this.lista_documentos = new ArrayList<>();
            for (Integer i = 1; i < filas; i++) {
                EcsaCrDocumentoList convert_document = new EcsaCrDocumentoList(
                        Integer.valueOf(vector_result[i][0]),
                        vector_result[i][1],
                        vector_result[i][2],
                        vector_result[i][3],
                        Integer.valueOf(vector_result[i][4]),
                        Integer.valueOf(vector_result[i][5]),
                        vector_result[i][6],
                        vector_result[i][7],
                        vector_result[i][8],
                        vector_result[i][9],
                        vector_result[i][10],
                        vector_result[i][11],
                        vector_result[i][12],
                        vector_result[i][13],
                        vector_result[i][14],
                        vector_result[i][15],
                        vector_result[i][16],
                        vector_result[i][17]);
                this.lista_documentos.add(convert_document);
            }
            
            PrimeFaces.current().executeScript("PF('var_tbl_documentos').clearFilters();");
            
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void gosocket() {
        try {
            if (this.sel_documentos != null) {
                if (this.sel_documentos.getProcesado().trim().equals("NO")) {
                    Fcr_Servicio servicio = new Fcr_Servicio();
                    String resultado = servicio.gosocket(this.usuario.getUsuario(), this.sel_documentos.getId_convert_document());
                    this.cargar_datos(this.usuario);

                    String opcion = resultado.substring(0, 1);
                    if (opcion.equals("0")) {
                        // OBTENER FECHA ACTUAL.
                        Date fecha_actual = new Date();
                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                        // REGISTRAR EVENTO.
                        Evento evento = new Evento(new Long(21), this.usuario.getId_usuario(), Long.parseLong(dataFormat.format(fecha_actual)), "Enviar documento GO-SOCKET: " + this.sel_documentos.getId_convert_document());
                        List<Evento> lista_eventos = new ArrayList<>();
                        lista_eventos.add(evento);
                        Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                        cliente_rest_evento.crear_evento(lista_eventos);
                        
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
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: gosocket ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void cargar_docs() {
        try {
            Integer anio = this.fecha_facturacion.getYear() + 1900;
            Integer mes = this.fecha_facturacion.getMonth() + 1;
            Integer dia = this.fecha_facturacion.getDate();

            Fcr_Servicio servicio = new Fcr_Servicio();
            String resultado = servicio.cargarDocs(this.usuario.getUsuario(), anio, mes, dia, this.tabla);
            this.cargar_datos(this.usuario);

            String opcion = resultado.substring(0, 1);
            if (opcion.equals("0")) {
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(new Long(22), this.usuario.getId_usuario(), Long.parseLong(dataFormat.format(fecha_actual)), "Cargar documentos JDE-ITAPP-GO-SOCKET: (" + anio + "/" + mes + "/" + dia + " ==> " + this.tabla + ")");
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado.substring(2)));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_docs ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }
    
    public void anular_documento() {
        try {
            if (this.sel_documentos != null) {
                if (this.sel_documentos.getProcesado().equals("NO")) {
                    Fcr_Servicio servicio = new Fcr_Servicio();
                    String resultado = servicio.anularDocumento(this.usuario.getUsuario(), this.sel_documentos.getId_convert_document(), this.sel_documentos.getRefacturacion());
                    this.cargar_datos(this.usuario);

                    String opcion = resultado.substring(0, 1);
                    if (opcion.equals("0")) {
                        // OBTENER FECHA ACTUAL.
                        Date fecha_actual = new Date();
                        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                        // REGISTRAR EVENTO.
                        Evento evento = new Evento(new Long(23), this.usuario.getId_usuario(), Long.parseLong(dataFormat.format(fecha_actual)), "Anular documento JDE-ITAPP-GO-SOCKET: " + this.sel_documentos.getId_convert_document());
                        List<Evento> lista_eventos = new ArrayList<>();
                        lista_eventos.add(evento);
                        Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                        cliente_rest_evento.crear_evento(lista_eventos);
                        
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "No puede eliminar documentos autorizados por hacienda."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un documento fiscal."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: anular_documento ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }
    
    public void refacturar() {
        try {
            if (this.sel_documentos != null) {
                if(this.sel_documentos.getProcesado().equals("SI")) {
                    if(this.sel_documentos.getProcess_result().length() >= 50) {
                        Fcr_Servicio servicio = new Fcr_Servicio();
                        String resultado = servicio.reFacturar(this.usuario.getUsuario(), this.sel_documentos.getId_convert_document(), this.tabla);
                        this.cargar_datos(this.usuario);
                        String opcion = resultado.substring(0, 1);
                        if (opcion.equals("0")) {
                            // OBTENER FECHA ACTUAL.
                            Date fecha_actual = new Date();
                            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                            // REGISTRAR EVENTO.
                            Evento evento = new Evento(new Long(24), this.usuario.getId_usuario(), Long.parseLong(dataFormat.format(fecha_actual)), "Refacturar documento GO-SOCKET: " + this.sel_documentos.getId_convert_document());
                            List<Evento> lista_eventos = new ArrayList<>();
                            lista_eventos.add(evento);
                            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest","R3st-T3rR@");
                            cliente_rest_evento.crear_evento(lista_eventos);
                            
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado.substring(2)));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", resultado.substring(2)));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "El documento no puede ser Re-Facturado porque no tiene Process Result correcto."));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "El documento no puede ser Re-Facturado porque no ha sido procesado."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un documento fiscal."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: refacturar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<EcsaCrDocumentoList> getLista_documentos() {
        return lista_documentos;
    }

    public void setLista_documentos(List<EcsaCrDocumentoList> lista_documentos) {
        this.lista_documentos = lista_documentos;
    }

    public EcsaCrDocumentoList getSel_documentos() {
        return sel_documentos;
    }

    public void setSel_documentos(EcsaCrDocumentoList sel_documentos) {
        this.sel_documentos = sel_documentos;
    }

    public Date getFecha_facturacion() {
        return fecha_facturacion;
    }

    public void setFecha_facturacion(Date fecha_facturacion) {
        this.fecha_facturacion = fecha_facturacion;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public List<SelectItem> getLst_tabla() {
        return lst_tabla;
    }

    public void setLst_tabla(List<SelectItem> lst_tabla) {
        this.lst_tabla = lst_tabla;
    }
    
}
