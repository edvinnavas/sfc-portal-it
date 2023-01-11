package ClientRestService;

import java.io.Serializable;
import java.util.List;

public class Fcr_Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    public Fcr_Servicio() {

    }

    public String logueo(String usuario, String contrasena) {
        String resultado = "";
        return resultado;
    }

    public List<String> reporte(String cadenasql) {
        List<String> resultado = null;
        return resultado;
    }

    public String cargarDocs(String usuario, Integer anio, Integer mes, Integer dia, String tabla) {
        String resultado = "";
        return resultado;
    }

    public String gosocket(String usuario, Integer idDocumentConvert) {
        String resultado = "";
        return resultado;
    }

    public String anularDocumento(String usuario, Integer idDocumentConvert, String refacturacion) {
        String resultado = "";
        return resultado;
    }

    public String reFacturar(String usuario, Integer idDocumentConvert, String tabla) {
        String resultado = "";
        return resultado;
    }

    public String modificarReceptor(String usuario, Integer idReceptor, Integer idTipoContribuyente, String taxId, String nombreCliente, String direccion, String correo, String codigoArea) {
        String resultado = "";
        return resultado;
    }

    public String modificarReferencia(String usuario, Integer idReferencia, String noDocumentoRef, String idBatch, String comentarioAdjunto, Integer idCodigoRef, String tipoNotaCredito) {
        String resultado = "";
        return resultado;
    }

    public String modificarExoneracion(Integer idExoneracion, Integer idDocumento, String activoExoneracion, Integer idTipoExoneracion, String numDoc, String fechaEmision, String nombreInstitucion, Double porcentajeExoneracion, Double montoExoneracion) {
        String resultado = "";
        return resultado;
    }

    public String modificarOtrosCargos(Integer idOtrosCargos, Integer idDocumento, String activoOtrosCargos, Integer idTipoOtrosCargos, String numeroIdentificacion, String razonSocial, String descripcion, Double porcentajeOtrosCargos, Double montoOtrosCargos) {
        String resultado = "";
        return resultado;
    }

}
