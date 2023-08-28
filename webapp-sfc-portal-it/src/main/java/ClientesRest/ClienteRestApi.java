package ClientesRest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import org.glassfish.jersey.client.ClientConfig;

public class ClienteRestApi implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String BASE_URI = "http://10.253.7.250:8015/portal-it/";
    private static final String BASE_URI_GOSOCKET = "http://10.253.7.250:8019/webapp-sfc-gosocket/portal-it/";
    private ClientConfig clientConfig;
    private Client client;

    public ClienteRestApi() {
        try {
            this.clientConfig = new ClientConfig();
            this.clientConfig.register(String.class);
            this.client = ClientBuilder.newClient(this.clientConfig);
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ClienteRestApi(), ERRROR: " + ex.toString());
        }
    }

    public String autenticar(String usuario, String contrasena) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("autenticar/" + usuario + "/" + contrasena);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String lista_dtes(String ambiente, String fecha) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/lista_dtes/" + ambiente + "/" + fecha);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: lista_dte_cr(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String cargar_docs(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/cargar_docs");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: cargar_docs(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String gosocket(String parametros) {
        String resultado = "";

        try {
            System.out.println("PARAMETROS-CLIENTE-WS: " + parametros);
            WebTarget webTarget = this.client.target(BASE_URI_GOSOCKET).path("gosocket/gosocket");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN);
            Response response = invocationBuilder.post(Entity.text(data));
            System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: gosocket(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String modificar_referencia(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/modificar_referencia");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_referencia(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String anular_documento(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/anular_documento");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: anular_documento(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String re_facturar(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/re_facturar");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: re_facturar(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String modificar_receptor(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/modificar_receptor");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_receptor(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String modificar_exoneracion(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/modificar_exoneracion");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_exoneracion(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
    
    public String modificar_otros_cargos(String parametros) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("felcr/modificar_otros_cargos");
            String data = parametros;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            // System.out.println("CONEXION REST-API-PORTAL-TI: " + response.getStatus());
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            System.out.println("PROYECTO: webapp-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: modificar_otros_cargos(), ERRROR: " + ex.toString());
        }

        return resultado;
    }
}
