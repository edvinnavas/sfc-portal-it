package Bean.SVFiscalia;

import Entidad.Evento;
import Entidad.Usuario;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import ClientRestService.Cliente_Rest_Evento;

@Named(value = "Cargar_RDE_Dialog_Uno")
@ViewScoped
public class Cargar_RDE_Dialog_Uno implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private UploadedFile documento_transacciones;

    private Integer empresa;

    public Cargar_RDE_Dialog_Uno() {

    }

    public void cargar_datos(Usuario usuario, Integer empresa) {
        try {
            this.usuario = usuario;
            this.empresa = empresa;

            PrimeFaces.current().executeScript("PF('var_dlgSVFiscaliaRDECargar').show();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void cargar() {
        String path = "";
        String resultado = "";

        try {
            if (this.documento_transacciones.getFileName() != null && !this.documento_transacciones.getFileName().equals("")) {
                Date fecha = new Date();

                Integer anio = fecha.getYear() + 1900;
                Integer mes = fecha.getMonth() + 1;
                Integer dia = fecha.getDate();

                Integer hora = fecha.getHours();
                Integer minuto = fecha.getMinutes();
                Integer segundo = fecha.getSeconds();

                String fecha_texto = anio.toString();
                if (mes < 10) {
                    fecha_texto = fecha_texto + "0" + mes;
                } else {
                    fecha_texto = fecha_texto + mes;
                }
                if (dia < 10) {
                    fecha_texto = fecha_texto + "0" + dia;
                } else {
                    fecha_texto = fecha_texto + dia;
                }
                if (hora < 10) {
                    fecha_texto = fecha_texto + "0" + hora;
                } else {
                    fecha_texto = fecha_texto + hora;
                }
                if (minuto < 10) {
                    fecha_texto = fecha_texto + "0" + minuto;
                } else {
                    fecha_texto = fecha_texto + minuto;
                }
                if (segundo < 10) {
                    fecha_texto = fecha_texto + "0" + segundo;
                } else {
                    fecha_texto = fecha_texto + segundo;
                }

                // path = "C:\\\\rep_svf_fiscalia\\" + fecha_texto + "_RDE.xlsx";
                path = "/root/rep_svf_fiscalia/" + fecha_texto + "_RDE.xlsx";

                File destfile_adjunto = new File(path);
                FileUtils.copyInputStreamToFile(this.documento_transacciones.getInputStream(), destfile_adjunto);
            }

            Svf_Servicio servicio = new Svf_Servicio();
            resultado = servicio.cargarArchivoReporteDiario(Integer.valueOf(this.usuario.getId_usuario().toString()), path, this.empresa);
            if (resultado.trim().substring(0, 5).equals("EXITO")) {
                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(25), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "UNO: Cargar transacciones reporte diario de efectivo.");
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", resultado));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UploadedFile getDocumento_transacciones() {
        return documento_transacciones;
    }

    public void setDocumento_transacciones(UploadedFile documento_transacciones) {
        this.documento_transacciones = documento_transacciones;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

}
