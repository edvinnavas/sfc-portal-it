package Recurso;

import java.io.Serializable;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("gosocket")
public class MyResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it (PORTAL-IT)!";
    }

    @POST
    @Path("gosocket")
    @Produces(MediaType.TEXT_PLAIN)
    public String gosocket(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.PosManager posmanager = new Control.PosManager();
            resultado = posmanager.gosocket(credenciales[0], Integer.valueOf(credenciales[1]));
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: gosocket(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: gosocket(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

}
