package Recursos;

import java.io.Serializable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("sfc/tipo-evento")
public class Recurso_Tipo_Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	@RolesAllowed("ADMIN")
	@Path("obtener-lista")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response obtener_lista() {
		Response resultado;

		try {
			Controles.Ctrl_Tipo_Evento ctrl_tipo_evento = new Controles.Ctrl_Tipo_Evento();
			resultado = Response.ok(ctrl_tipo_evento.obtener_lista(), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("obtener-id/{id_tipo_evento}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response obtener_id(@PathParam("id_tipo_evento") Long id_tipo_evento) {
		Response resultado;

		try {
			Controles.Ctrl_Tipo_Evento ctrl_tipo_evento = new Controles.Ctrl_Tipo_Evento();
			resultado = Response.ok(ctrl_tipo_evento.obtener_id(id_tipo_evento), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_id()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_id()" + " ERROR: " + ex.toString());
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("crear")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response crear(String jsonString) {
		Response resultado;

		try {
			Controles.Ctrl_Tipo_Evento ctrl_tipo_evento = new Controles.Ctrl_Tipo_Evento();
			resultado = Response.ok(ctrl_tipo_evento.crear(jsonString), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: crear()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: crear()" + " ERROR: " + ex.toString());
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("modificar/{id_tipo_evento}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public Response modificar(@PathParam("id_tipo_evento") Long id_tipo_evento, String jsonString) {
		Response resultado;

		try {
			Controles.Ctrl_Tipo_Evento ctrl_tipo_evento = new Controles.Ctrl_Tipo_Evento();
			resultado = Response.ok(ctrl_tipo_evento.modificar(id_tipo_evento, jsonString), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: modificar()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: modificar()" + " ERROR: " + ex.toString());
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("eliminar/{id_tipo_evento}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	public Response eliminar(@PathParam("id_tipo_evento") Long id_tipo_evento) {
		Response resultado;

		try {
			Controles.Ctrl_Tipo_Evento ctrl_tipo_evento = new Controles.Ctrl_Tipo_Evento();
			resultado = Response.ok(ctrl_tipo_evento.eliminar(id_tipo_evento), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: eliminar()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: eliminar()" + " ERROR: " + ex.toString());
		}

		return resultado;
	}

}
