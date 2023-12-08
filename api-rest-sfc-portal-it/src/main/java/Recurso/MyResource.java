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
    @Path("felcr/lista_dtes/{ambiente}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public String lista_dtes(
            @PathParam("ambiente") String ambiente, 
            @PathParam("fecha") String fecha) {
        
        String resultado;

        try {
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.lista_dtes(ambiente, fecha);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: lista_dtes(), ERRROR: " + ex.toString();
        }

        return resultado;
    }
    
    @GET
    @Path("felcr/obtener_document/{ambiente}/{id_convert_document}")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtener_document(
            @PathParam("ambiente") String ambiente,
            @PathParam("id_convert_document") Long id_convert_document) {

        String resultado;

        try {
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.obtener_document(ambiente, id_convert_document);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_document(), ERRROR: " + ex.toString();
        }

        return resultado;
    }
    
    @GET
    @Path("felcr/obtener_referencia/{ambiente}/{id_document}")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtener_referencia(
            @PathParam("ambiente") String ambiente,
            @PathParam("id_document") Long id_document) {

        String resultado;

        try {
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.obtener_referencia(ambiente, id_document);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_referencia(), ERRROR: " + ex.toString();
        }

        return resultado;
    }
    
    @GET
    @Path("felcr/obtener_cat_codigo_ref/{ambiente}")
    public String obtener_cat_codigo_ref(
            @PathParam("ambiente") String ambiente) {
        
        String resultado = "";
        
        try {
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.obtener_cat_codigo_ref(ambiente);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_cat_codigo_ref(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_cat_codigo_ref(), ERRROR: " + ex.toString());
        }
        
        return resultado;
    }
    
    @POST
    @Path("felcr/cargar_docs")
    public String cargar_docs(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.cargar_docs(credenciales[0], credenciales[1], Integer.valueOf(credenciales[2]), Integer.valueOf(credenciales[3]), Integer.valueOf(credenciales[4]), credenciales[5]);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: cargar_docs(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: cargar_docs(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    @POST
    @Path("felcr/modificar_referencia")
    public String modificar_referencia(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.modificar_referencia(credenciales[0], credenciales[1], Integer.valueOf(credenciales[2]), credenciales[3], credenciales[4], credenciales[5], Integer.valueOf(credenciales[6]), credenciales[7]);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_referencia(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_referencia(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    @POST
    @Path("felcr/anular_documento")
    public String anular_documento(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.anular_documento(credenciales[0], credenciales[1], Integer.valueOf(credenciales[2]), credenciales[3]);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: anular_documento(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: anular_documento(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    @POST
    @Path("felcr/re_facturar")
    public String re_facturar(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.re_facturar(credenciales[0], credenciales[1], Integer.valueOf(credenciales[2]), credenciales[3]);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: re_facturar(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: re_facturar(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    @POST
    @Path("felcr/modificar_receptor")
    public String modificar_receptor(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.modificar_receptor(credenciales[0], credenciales[1], Integer.valueOf(credenciales[2]), Integer.valueOf(credenciales[3]), credenciales[4], credenciales[5], credenciales[6], credenciales[7], credenciales[8]);
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_receptor(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_receptor(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    @POST
    @Path("felcr/modificar_exoneracion")
    public String modificar_exoneracion(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.modificar_exoneracion(credenciales[0], Integer.valueOf(credenciales[1]), Integer.valueOf(credenciales[2]), credenciales[3], Integer.valueOf(credenciales[4]), credenciales[5], credenciales[6], credenciales[7], Double.valueOf(credenciales[8]), Double.valueOf(credenciales[9]));
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_exoneracion(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_exoneracion(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    @POST
    @Path("felcr/modificar_otros_cargos")
    public String modificar_otros_cargos(String parametros) {
        String resultado;

        try {
            // System.out.println("PARAMETROS: " + parametros);
            String[] credenciales = parametros.split("♣");
            Control.Ctrl_FelCr ctrl_felcr = new Control.Ctrl_FelCr();
            resultado = ctrl_felcr.modificar_otros_cargos(credenciales[0], Integer.valueOf(credenciales[1]), Integer.valueOf(credenciales[2]), credenciales[3], Integer.valueOf(credenciales[4]), credenciales[5], credenciales[6], credenciales[7], Double.valueOf(credenciales[8]), Double.valueOf(credenciales[9]));
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_otros_cargos(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_otros_cargos(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
}
