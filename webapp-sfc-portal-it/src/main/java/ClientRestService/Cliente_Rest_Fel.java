package ClientRestService;

import com.google.gson.Gson;
import Entidad.Dte_Lista;
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

public class Cliente_Rest_Fel implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String BASE_URI = "http://API-REST-SFC-PORTAL-IT:8015/portal-it/";
    private ClientConfig clientConfig;
    private Client client;

    public Cliente_Rest_Fel(String usuario, String pass) {
        try {
            this.clientConfig = new ClientConfig();
            this.clientConfig.register(String.class);
            this.client = ClientBuilder.newClient(this.clientConfig);
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Cliente_Rest_Drive ERROR: " + ex.toString());
        }
    }

    public String jdetofel(String ambiente, String compania, Long fecha_inicial, Long fecha_final) {
        String resultado;

        try {
            WebTarget webTarget = client.target(BASE_URI).path("fel-energia/jdetofel/" + ambiente + "/" + compania + "/" + fecha_inicial + "/" + fecha_final);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.text(""));
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

    public String desmarcar(String ambiente, Long id_dte) {
        String resultado;

        try {
            WebTarget webTarget = client.target(BASE_URI).path("fel-energia/desmarcar/" + ambiente + "/" + id_dte);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.text(""));
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

    public String generar_archivo(String ambiente, List<Dte_Lista> lista_documentos) {
        String resultado;

        try {
            WebTarget webTarget = client.target(BASE_URI).path("fel-energia/generar-archivo/" + ambiente);
            String data = new Gson().toJson(lista_documentos);
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

    public String modificar_ambiente(String ambiente, Long id_ambiente, Integer valor) {
        String resultado;

        try {
            WebTarget webTarget = client.target(BASE_URI).path("fel-energia/modificar-ambiente/" + ambiente + "/" + id_ambiente);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.text(valor.toString()));
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

    public String autorizar(String ambiente_conn, String path, String ambiente, String kcoo) {
        String resultado;

        try {
            WebTarget webTarget = client.target(BASE_URI).path("fel-energia/autorizar/" + ambiente_conn);
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.text(path + "♣" + ambiente + "♣" + kcoo));
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
