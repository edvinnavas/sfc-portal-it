package ClientRestService;

import java.io.Serializable;
import java.util.List;

public class Rba_Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    public Rba_Servicio() {

    }

    public String eliminarEstacionServicio(Integer idEstacionServicio) {
        String resultado = "";
        return resultado;
    }

    public String facturasEstacionServicio(String anio, String mes, String dia) {
        String resultado = "";
        return resultado;
    }

    public String habilitarFacturacionFecha(Integer anio, Integer mes, Integer dia) {
        String resultado = "";
        return resultado;
    }

    public String insertarBridge(Integer anio, Integer mes, Integer dia, String jsonSring) {
        String resultado = "";
        return resultado;
    }

    public String insertarEDIRBA(Integer anio, Integer mes, Integer dia, String jsonSring) {
        String resultado = "";
        return resultado;
    }

    public String insertarEstacionServicio(String codE1, String codEnvoy, String codPayware, String nombreEstacion, String dctoE1, String mcuE1, String shanE1, String kcooE1, String routE1, String lstCodNaf) {
        String resultado = "";
        return resultado;
    }

    public String logueo(String usuario, String contrasena) {
        String resultado = "";
        return resultado;
    }

    public String modificarEstacionServicio(Integer idEstacionServicio, String codE1, String codEnvoy, String codPayware, String nombreEstacion, String dctoE1, String mcuE1, String shanE1, String kcooE1, String routE1, String lstCodNaf) {
        String resultado = "";
        return resultado;
    }

    public List<String> reporte(java.lang.String cadenasql) {
        List<String> resultado = null;
        return resultado;
    }

    public String desactivarEstacionServicio(Integer idEstacionServicio) {
        String resultado = "";
        return resultado;
    }

    public String activarEstacionServicio(Integer idEstacionServicio) {
        String resultado = "";
        return resultado;
    }

}
