package Bean.GtFacturasFel;

import Entidad.Usuario;
import java.io.File;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import ClientRestService.Cliente_Rest_Fel;

@Named(value = "Autorizar_Dialog")
@ViewScoped
public class Autorizar_Dialog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ambiente;
    private Usuario usuario;

    private String kcoo_compania_jde;

    private UploadedFile documento_generado;
    private String ambiente_jde;

    public Autorizar_Dialog() {

    }

    public void cargar_datos(String ambiente, Usuario usuario, String ambiente_jde, String kcoo_compania_jde) {
        try {
            this.ambiente = ambiente;
            this.usuario = usuario;
            this.ambiente_jde = ambiente_jde;
            this.kcoo_compania_jde = kcoo_compania_jde;

            PrimeFaces.current().executeScript("PF('dialogAutorizarVar').show();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: cargar_datos ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void autorizar() {
        String path = "";
        String resultado;

        try {
            if (this.documento_generado.getFileName() != null && !this.documento_generado.getFileName().equals("")) {
                String path_directorio = "/SFC_PORTAL_IT/FELGT_ENERGIA/" + this.ambiente + "/DOC_GENERADO/";
                File directorio = new File(path_directorio);
                if (!directorio.exists()) {
                    directorio.mkdir();
                }

                path = path_directorio + this.documento_generado.getFileName();
                File destfile_adjunto = new File(path);
                FileUtils.copyInputStreamToFile(this.documento_generado.getInputStream(), destfile_adjunto);
            }

            Cliente_Rest_Fel cliente_rest_fel = new Cliente_Rest_Fel("UserTerraRest", "R3st-T3rR@");
            resultado = cliente_rest_fel.autorizar(this.ambiente, path, this.ambiente_jde, this.kcoo_compania_jde);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: autorizar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UploadedFile getDocumento_generado() {
        return documento_generado;
    }

    public void setDocumento_generado(UploadedFile documento_generado) {
        this.documento_generado = documento_generado;
    }

    public String getAmbiente_jde() {
        return ambiente_jde;
    }

    public void setAmbiente_jde(String ambiente_jde) {
        this.ambiente_jde = ambiente_jde;
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
