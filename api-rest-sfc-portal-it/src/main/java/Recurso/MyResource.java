package Recurso;

import java.io.Serializable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("portal-it")
public class MyResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it (PORTAL-IT)!";
    }

    @POST
    @Path("usuario/login3")
    public String login3(String credenciales_param) {
        String resultado;

        try {
            String[] credenciales = credenciales_param.split(",");
            Control.Ctrl_Usuario control_usuario = new Control.Ctrl_Usuario();
            resultado = control_usuario.login3(credenciales[0], credenciales[1]);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: login3(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: login3(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    @GET
    @Path("autenticar/{usuario}/{contrasena}")
    @Produces(MediaType.APPLICATION_JSON)
    public String autenticar(
            @PathParam("usuario") String usuario,
            @PathParam("contrasena") String contrasena) {

        String resultado;

        try {
            Control.Ctrl_Usuario ctrl_usuario = new Control.Ctrl_Usuario();
            resultado = ctrl_usuario.autenticar(usuario, contrasena);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    @GET
    @Path("lista_dte_cr/{ambiente}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public String lista_dte_cr(
            @PathParam("ambiente") String ambiente, 
            @PathParam("fecha") String fecha) {
        
        String resultado;

        try {
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.lista_dte_cr(ambiente, fecha);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: lista_dte_cr(), ERRROR: " + ex.toString();
        }

        return resultado;
    }
    
}
