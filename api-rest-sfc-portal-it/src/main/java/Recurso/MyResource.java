package Recurso;

import Control.Control_Drive;
import Control.Control_Evento;
import Control.Control_Fel_Energia;
import Control.Control_Usuario;
import java.io.Serializable;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("portal-it")
public class MyResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it (PORTAL-IT)!";
    }

    @GET
    @Path("usuario")
    public String buscar_usuarios() {
        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.buscar_usuarios();
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @GET
    @Path("usuario/{id_usuario}")
    public String buscar_usuario(
            @PathParam("id_usuario") Long id_usuario) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.buscar_usuario(id_usuario);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("usuario")
    public String crear_usuario(String JsonString) {
        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.crear_usuario(JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("usuario/{id_usuario}")
    public String modificar_usuario(
            @PathParam("id_usuario") Long id_usuario, String JsonString) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.modificar_usuario(id_usuario, JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @DELETE
    @Path("usuario/{id_usuario}")
    public String eliminar_usuario(
            @PathParam("id_usuario") Long id_usuario) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.eliminar_usuario(id_usuario);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("usuario/login")
    public String login(String JsonString) {
        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.login(JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("usuario/login2")
    public String login2(String JsonString) {
        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.login2(JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("usuario/login3")
    public String login3(String credenciales_param) {
        String resultado;

        try {
            String[] credenciales = credenciales_param.split(",");
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.login3(credenciales[0], credenciales[1]);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("usuario/reset_pass/{id_usuario}")
    public String reset_pass_usuario(
            @PathParam("id_usuario") Long id_usuario) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.reset_pass_usuario(id_usuario);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("usuario/cambiar_pass/{id_usuario}")
    public String cambiar_pass_usuario(
            @PathParam("id_usuario") Long id_usuario, String pass) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.cambiar_pass_usuario(id_usuario, pass);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("usuario/desactivar/{id_usuario}")
    public String desactivar_usuario(
            @PathParam("id_usuario") Long id_usuario) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.desactivar_usuario(id_usuario);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("usuario/activar/{id_usuario}")
    public String activar_usuario(
            @PathParam("id_usuario") Long id_usuario) {

        String resultado;

        try {
            Control_Usuario control_usuario = new Control_Usuario();
            resultado = control_usuario.activar_usuario(id_usuario);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("drive")
    public String drive(String cadenasql) {
        String resultado;

        try {
            Control_Drive control_drive = new Control_Drive();
            resultado = control_drive.drive(cadenasql);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("drive-fel-energia/{ambiente}")
    public String drive_fel_energia(
            @PathParam("ambiente") String ambiente,
            String cadenasql) {

        String resultado;

        try {
            Control_Drive control_drive = new Control_Drive();
            resultado = control_drive.drive_fel_energia(ambiente, cadenasql);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @GET
    @Path("evento")
    public String buscar_eventos() {
        String resultado;

        try {
            Control_Evento control_evento = new Control_Evento();
            resultado = control_evento.buscar_eventos();
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @GET
    @Path("evento/{id_tipo_evento}/{id_usuario}/{fecha_hora}")
    public String buscar_evento(
            @PathParam("id_tipo_evento") Long id_tipo_evento,
            @PathParam("id_usuario") Long id_usuario,
            @PathParam("fecha_hora") Long fecha_hora) {

        String resultado;

        try {
            Control_Evento control_evento = new Control_Evento();
            resultado = control_evento.buscar_evento(id_tipo_evento, id_usuario, fecha_hora);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("evento")
    public String crear_evento(String JsonString) {
        String resultado;

        try {
            Control_Evento control_evento = new Control_Evento();
            resultado = control_evento.crear_evento(JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("evento/{id_tipo_evento}/{id_usuario}/{fecha_hora}")
    public String modificar_evento(
            @PathParam("id_tipo_evento") Long id_tipo_evento,
            @PathParam("id_usuario") Long id_usuario,
            @PathParam("fecha_hora") Long fecha_hora,
            String JsonString) {

        String resultado;

        try {
            Control_Evento control_evento = new Control_Evento();
            resultado = control_evento.modificar_evento(id_tipo_evento, id_usuario, fecha_hora, JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @DELETE
    @Path("evento/{id_tipo_evento}/{id_usuario}/{fecha_hora}")
    public String eliminar_evento(
            @PathParam("id_tipo_evento") Long id_tipo_evento,
            @PathParam("id_usuario") Long id_usuario,
            @PathParam("fecha_hora") Long fecha_hora) {

        String resultado;

        try {
            Control_Evento control_evento = new Control_Evento();
            resultado = control_evento.eliminar_evento(id_tipo_evento, id_usuario, fecha_hora);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("fel-energia/jdetofel/{ambiente}/{compania}/{fecha_inicial}/{fecha_final}")
    public String jde_to_fel(
            @PathParam("ambiente") String ambiente,
            @PathParam("compania") String compania,
            @PathParam("fecha_inicial") Long fecha_inicial,
            @PathParam("fecha_final") Long fecha_final) {

        String resultado;

        try {
            Control_Fel_Energia control_fel_energia = new Control_Fel_Energia();
            resultado = control_fel_energia.Cargar_Doc_Fel(ambiente, compania, fecha_inicial, fecha_final);
            String[] result = resultado.split("♣");
            if (result[0].equals("1")) {
                throw new Exception(result[1]);
            }

            resultado = control_fel_energia.actualizar_descripcion_producto(ambiente);
            result = resultado.split("♣");
            if (result[0].equals("1")) {
                throw new Exception(result[1]);
            }

            resultado = result[1];

        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("fel-energia/desmarcar/{ambiente}/{id_dte}")
    public String desmarcar(
            @PathParam("ambiente") String ambiente,
            @PathParam("id_dte") Long id_dte) {

        String resultado;

        try {
            Control_Fel_Energia control_fel_energia = new Control_Fel_Energia();
            resultado = control_fel_energia.desmarcar_fel(ambiente, id_dte);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("fel-energia/generar-archivo/{ambiente}")
    public String generar_archivo(
            @PathParam("ambiente") String ambiente,
            String JsonString) {

        String resultado;

        try {
            Control_Fel_Energia control_fel_energia = new Control_Fel_Energia();
            resultado = control_fel_energia.generar_archivo(ambiente, JsonString);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @PUT
    @Path("fel-energia/modificar-ambiente/{ambiente}/{id_ambiente}")
    public String modificar_ambiente(
            @PathParam("ambiente") String ambiente,
            @PathParam("id_ambiente") Long id_ambiente,
            String valor) {

        String resultado;

        try {
            Control_Fel_Energia control_fel_energia = new Control_Fel_Energia();
            resultado = control_fel_energia.fel_modificar_ambiente(ambiente, id_ambiente, Integer.valueOf(valor));
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

    @POST
    @Path("fel-energia/autorizar/{ambiente}")
    public String autorizar(
            @PathParam("ambiente") String ambiente,
            String path) {

        String resultado;

        try {
            String[] param = path.split("♣");
            Control_Fel_Energia control_fel_energia = new Control_Fel_Energia();
            resultado = control_fel_energia.documento_autorizar(ambiente, param[0], param[1], param[2]);
        } catch (Exception ex) {
            resultado = "ERROR: " + ex.toString();
        }

        return resultado;
    }

}
