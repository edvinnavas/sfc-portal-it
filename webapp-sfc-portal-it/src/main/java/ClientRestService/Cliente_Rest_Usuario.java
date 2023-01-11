package ClientRestService;

import Entidad.Usuario;
import com.google.gson.Gson;
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

public class Cliente_Rest_Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String BASE_URI = "http://API-REST-SFC-PORTAL-IT:8015/portal-it/";
    private ClientConfig clientConfig;
    private Client client;

    public Cliente_Rest_Usuario(String usuario, String pass) {
        try {
            this.clientConfig = new ClientConfig();
            this.clientConfig.register(String.class);
            this.client = ClientBuilder.newClient(this.clientConfig);
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Cliente_Rest_Usuario ERROR: " + ex.toString());
        }
    }

    public String get_usuarios() {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario");
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

    public String get_usuario(Long id_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/" + id_usuario);
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

    public String login(List<Usuario> lista_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = client.target(BASE_URI).path("usuario/login");
            String data = new Gson().toJson(lista_usuario);
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

    public String login2(List<Usuario> lista_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = client.target(BASE_URI).path("usuario/login2");
            String data = new Gson().toJson(lista_usuario);
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

    public String crear_usuario(List<Usuario> listado_usuarios) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario");
            String data = new Gson().toJson(listado_usuarios);
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

    public String modificar_usuario(Long id_usuario, List<Usuario> listado_usuarios) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/" + id_usuario);
            String data = new Gson().toJson(listado_usuarios);
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

    public String eliminar_usuario(Long id_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/" + id_usuario);
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

    public String reset_pass_usuario(Long id_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/reset_pass/" + id_usuario);
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

    public String cambiar_pass_usuario(Long id_usuario, String pass) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/cambiar_pass/" + id_usuario);
            String data = pass;
            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.text(pass));
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

    public String desactivar(Long id_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/desactivar/" + id_usuario);
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

    public String activar(Long id_usuario) {
        String resultado = "";

        try {
            WebTarget webTarget = this.client.target(BASE_URI).path("usuario/activar/" + id_usuario);
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

}
