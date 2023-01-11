package Bean.GtFacturasFel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.DTE_FEL;
import Entidad.Dte_Lista;
import Entidad.Evento;
import Entidad.Usuario;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Fel;

@Named(value = "CargaDocDteDialog")
@ViewScoped
public class CargaDocDteDialog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ambiente;
    private Usuario usuario;

    private String kcoo_compania_jde;

    private List<Dte_Lista> lista_dte;
    private Date fecha_inicial;
    private Date fecha_final;
    private String tipo_documento;

    private StreamedContent file;

    public CargaDocDteDialog() {

    }

    public void cargar_datos(String ambiente, Usuario usuario, List<Dte_Lista> lista_dte, String tipo_documento, Date fecha_inicial, Date fecha_final, String kcoo_compania_jde) {
        try {
            this.ambiente = ambiente;
            this.usuario = usuario;
            this.lista_dte = lista_dte;
            this.tipo_documento = tipo_documento;
            this.fecha_inicial = fecha_inicial;
            this.fecha_final = fecha_final;
            this.kcoo_compania_jde = kcoo_compania_jde;

            PrimeFaces.current().executeScript("PF('dialogFelDescArchivoVar').show();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void archivo_fel() {
        try {
            List<Dte_Lista> lista_documentos = new ArrayList<>();
            for (Integer i = 0; i < this.lista_dte.size(); i++) {
                if (this.lista_dte.get(i).getTipo_orden().equals(this.tipo_documento) && this.lista_dte.get(i).getEnviado().equals("NO")) {
                    lista_documentos.add(this.lista_dte.get(i));
                }
            }

            Cliente_Rest_Fel cliente_rest_fel = new Cliente_Rest_Fel("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_fel.generar_archivo(this.ambiente, lista_documentos);
            Type listType = new TypeToken<ArrayList<DTE_FEL>>() {
            }.getType();
            List<DTE_FEL> lista_dte_fel = new Gson().fromJson(jsonString, listType);

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dateFormat_1 = new SimpleDateFormat("yyyyMMddHHmmss");

            String dir_ambiente;
            if (this.ambiente.equals("PY")) {
                dir_ambiente = "PY";
            } else {
                dir_ambiente = "PD";
            }

            String path_directorio = "/SFC_PORTAL_IT/FELGT_ENERGIA/" + dir_ambiente;
            File directorio = new File(path_directorio);
            if (!directorio.exists()) {
                directorio.mkdir();
            }

            String nombre_archivo = path_directorio + "/fel" + dateFormat_1.format(fecha_actual) + ".txt";

            File archivo_loco = new File(nombre_archivo);
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo_loco), "Cp1252"));

            for (Integer a = 0; a < lista_dte_fel.size(); a++) {
                // ESCRIBE ENCABEZADO.
                if (lista_dte_fel.get(a).getDte_fel_encabezado() != null) {
                    DecimalFormat doubleFormat = new DecimalFormat("0.000000");
                    doubleFormat.setRoundingMode(RoundingMode.FLOOR);

                    String numero_acceso = "";
                    String direccion_encabezado = "";

                    String campo_adicionales = "\n";
                    // ID_DESTINO_VENTA DISTINTO A 1 (X <> 1)
                    if (lista_dte_fel.get(a).getDte_fel_encabezado().getId_destino_venta() != 1) {
                        String incoterm = lista_dte_fel.get(a).getDte_fel_encabezado().getIncoterm();
                        String destinatario = lista_dte_fel.get(a).getDte_fel_encabezado().getNombre_comprador();
                        String codigo_destinatario = " ";
                        String nombre_comprador = lista_dte_fel.get(a).getDte_fel_encabezado().getNombre_comprador();
                        String direccion_comprador = lista_dte_fel.get(a).getDte_fel_encabezado().getDireccion();
                        String codigo_comprador = " ";
                        String otra_referencia = "";
                        String codigo_exportacion = lista_dte_fel.get(a).getDte_fel_encabezado().getCodigo_exportador();
                        String direccion_destino = lista_dte_fel.get(a).getDte_fel_encabezado().getDireccion();
                        direccion_encabezado = lista_dte_fel.get(a).getDte_fel_encabezado().getDireccion();

                        otra_referencia = lista_dte_fel.get(a).getDte_fel_encabezado().getNit_comprador();
                        // lista_dte_fel.get(a).getDte_fel_encabezado().setNit_comprador("C/F");
                        campo_adicionales = "|" + incoterm + "|"
                                + destinatario + "|"
                                + codigo_destinatario + "|"
                                + nombre_comprador + "|"
                                + direccion_comprador + "|"
                                + codigo_comprador + "|"
                                + otra_referencia + "|"
                                + codigo_exportacion + "|"
                                + direccion_destino + "|"
                                + lista_dte_fel.get(a).getDte_fel_encabezado().getAb_cliente() + "|"
                                + lista_dte_fel.get(a).getDte_fel_encabezado().getId_tipo_receptor() + "\n";
                    } else {
                        String incoterm = "";
                        String destinatario = "";
                        String codigo_destinatario = " ";
                        String nombre_comprador = "";
                        String direccion_comprador = "";
                        String codigo_comprador = " ";
                        String otra_referencia = "";
                        String codigo_exportacion = "";
                        String direccion_destino = "";

                        campo_adicionales = "|" + incoterm + "|"
                                + destinatario + "|"
                                + codigo_destinatario + "|"
                                + nombre_comprador + "|"
                                + direccion_comprador + "|"
                                + codigo_comprador + "|"
                                + otra_referencia + "|"
                                + codigo_exportacion + "|"
                                + direccion_destino + "|"
                                + lista_dte_fel.get(a).getDte_fel_encabezado().getAb_cliente() + "|"
                                + lista_dte_fel.get(a).getDte_fel_encabezado().getId_tipo_receptor() + "\n";
                    }

                    fw.write(lista_dte_fel.get(a).getDte_fel_encabezado().getTipo_registro() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getFecha_documento() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getId_tipo_documento() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getNit_comprador().replaceAll("-", "") + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getId_codigo_moneda() + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_encabezado().getTasa_cambio()) + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getOrden_externo() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getId_tipo_venta() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getId_destino_venta() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getEnviar_correo() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getNombre_comprador() + "|"
                            // + lista_dte_fel.get(a).getDte_fel_encabezado().getDireccion() + "|"
                            + direccion_encabezado + "|"
                            + numero_acceso + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getSerie_admin() + "|"
                            + lista_dte_fel.get(a).getDte_fel_encabezado().getNumero_admin()
                            + campo_adicionales);
                }

                // ESCRIBE DETALLE.
                if (lista_dte_fel.get(a).getDte_fel_detalle() != null) {
                    for (Integer b = 0; b < lista_dte_fel.get(a).getDte_fel_detalle().size(); b++) {
                        DecimalFormat doubleFormat_1 = new DecimalFormat("0.00000");
                        doubleFormat_1.setRoundingMode(RoundingMode.HALF_UP);
                        DecimalFormat doubleFormat_2 = new DecimalFormat("0.000000");
                        doubleFormat_2.setRoundingMode(RoundingMode.HALF_UP);
                        DecimalFormat doubleFormat_3 = new DecimalFormat("0.0000");
                        doubleFormat_3.setRoundingMode(RoundingMode.HALF_UP);
                        DecimalFormat doubleFormat_4 = new DecimalFormat("0.00");
                        doubleFormat_4.setRoundingMode(RoundingMode.HALF_UP);
                        DecimalFormat doubleFormat_desc = new DecimalFormat("0.00");
                        doubleFormat_desc.setRoundingMode(RoundingMode.HALF_UP);

                        fw.write(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getTipo_registro() + "|"
                                + doubleFormat_1.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getCantidad()) + "|"
                                + lista_dte_fel.get(a).getDte_fel_detalle().get(b).getId_unidad_medida() + "|"
                                + doubleFormat_2.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getPrecio()) + "|"
                                + doubleFormat_3.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getPorcentaje_descuento() * 100) + "|"
                                + doubleFormat_desc.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_descuento()) + "|"
                                + doubleFormat_desc.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_bruto()) + "|"
                                + doubleFormat_4.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_exento()) + "|"
                                + doubleFormat_4.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_neto()) + "|"
                                + doubleFormat_desc.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_iva()) + "|"
                                + doubleFormat_4.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_otros()) + "|"
                                + doubleFormat_4.format(lista_dte_fel.get(a).getDte_fel_detalle().get(b).getImporte_total()) + "|"
                                + lista_dte_fel.get(a).getDte_fel_detalle().get(b).getProducto() + "|"
                                + lista_dte_fel.get(a).getDte_fel_detalle().get(b).getDescipcion() + "|"
                                + lista_dte_fel.get(a).getDte_fel_detalle().get(b).getId_tipo_venta() + "\n");
                    }
                }

                // ESCRIBE ASOCIADOS.
                if (lista_dte_fel.get(a).getDte_fel_asociados() != null) {
                    fw.write(lista_dte_fel.get(a).getDte_fel_asociados().getTipo_registro() + "|"
                            + lista_dte_fel.get(a).getDte_fel_asociados().getId_tipo_documento() + "|"
                            + lista_dte_fel.get(a).getDte_fel_asociados().getSerie() + "|"
                            + lista_dte_fel.get(a).getDte_fel_asociados().getNumero() + "|"
                            + lista_dte_fel.get(a).getDte_fel_asociados().getFecha_documento() + "\n");
                }

                // ESCRIBE TOTALES.
                if (lista_dte_fel.get(a).getDte_fel_totales() != null) {
                    DecimalFormat doubleFormat = new DecimalFormat("0.00");
                    doubleFormat.setRoundingMode(RoundingMode.HALF_UP);
                    DecimalFormat doubleFormat_desc = new DecimalFormat("0.00");
                    doubleFormat_desc.setRoundingMode(RoundingMode.HALF_UP);
                    fw.write(lista_dte_fel.get(a).getDte_fel_totales().getTipo_registro() + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_bruto()) + "|"
                            + doubleFormat_desc.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_descuento()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_exento()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_neto()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_iva()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_otros()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_total()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getPorcentaje_isr()) + "|"
                            + doubleFormat.format(lista_dte_fel.get(a).getDte_fel_totales().getImporte_isr()) + "|"
                            + lista_dte_fel.get(a).getDte_fel_totales().getRegistros_detalle() + "|"
                            + lista_dte_fel.get(a).getDte_fel_totales().getDocumentos_asociados() + "\n");
                }
            }
            fw.close();

            // TIPO DE EVENTO.
            Long tipo_evento = Long.valueOf(0);
            switch (this.tipo_documento) {
                case "FACT": {
                    tipo_evento = Long.valueOf(16);
                    break;
                }
                case "NCRE": {
                    tipo_evento = Long.valueOf(17);
                    break;
                }
                case "NDEB": {
                    tipo_evento = Long.valueOf(18);
                    break;
                }
            }

            InputStream stream = new FileInputStream(nombre_archivo);
            this.file = DefaultStreamedContent.builder()
                    .contentType("text/plain")
                    .name("FEL_" + this.tipo_documento + "_" + dateFormat_1.format(fecha_actual) + ".txt")
                    .stream(() -> stream)
                    .build();

            // REGISTRAR EVENTO.
            SimpleDateFormat dateFormat_2 = new SimpleDateFormat("dd/MM/yyyy");
            Evento evento = new Evento(tipo_evento, this.usuario.getId_usuario(), Long.valueOf(dateFormat_1.format(fecha_actual)), "Generar archivo FEL: " + nombre_archivo + " (" + dateFormat_2.format(this.fecha_inicial) + " - " + dateFormat_2.format(this.fecha_final) + ")");
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            PrimeFaces.current().executeScript("PF('dialogFelDescArchivoVar').hide();");
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

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
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
