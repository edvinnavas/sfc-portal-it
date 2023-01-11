package ClientRestService;

import com.google.gson.Gson;
import Entidad.Rol;
import java.io.Serializable;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

public class Cliente_Rest_Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String BASE_URI = "http://API-REST-SFC-PORTAL-IT:8015/portal-it/";
    private ClientConfig clientConfig;
    private Client client;

    public Cliente_Rest_Rol(String usuario, String pass) {
        try {
            this.clientConfig = new ClientConfig();
            this.clientConfig.register(String.class);
            this.client = ClientBuilder.newClient(this.clientConfig);
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Cliente_Rest_Rol ERROR: " + ex.toString());
        }
    }

    public String get_roles() {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("rol");
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            resultado = ex.toString();
        }

        return resultado;
    }

    public String get_rol(Long id_rol) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("rol/" + id_rol);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            resultado = ex.toString();
        }

        return resultado;
    }

    public String crear_rol(List<Rol> listado_roles) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("rol");
            String data = new Gson().toJson(listado_roles);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(data));
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            resultado = ex.toString();
        }

        return resultado;
    }

    public String modificar_rol(Long id_rol, List<Rol> listado_roles) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("rol/" + id_rol);
            String data = new Gson().toJson(listado_roles);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.text(data));
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            resultado = ex.toString();
        }

        return resultado;
    }

    public String eliminar_rol(Long id_rol) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("rol/" + id_rol);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.delete();
            if (response.getStatus() == 200) {
                resultado = response.readEntity(String.class);
            } else {
                resultado = response.getStatus() + ": " + response.getStatusInfo();
            }
        } catch (Exception ex) {
            resultado = ex.toString();
        }

        return resultado;
    }

}
