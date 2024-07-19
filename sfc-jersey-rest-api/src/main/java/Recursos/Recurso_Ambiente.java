package Recursos;

import java.io.Serializable;
import java.sql.Connection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Controles.Ctrl_Base_Datos;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("sfc/ambiente")
public class Recurso_Ambiente implements Serializable {

	private static final long serialVersionUID = 1L;

	@RolesAllowed("ADMIN")
	@Path("iniciar")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response iniciar() {
		Response resultado;
		Connection conn = null;

		try {
			Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
			conn = ctrl_base_datos.obtener_conexion_mysql();

			conn.setAutoCommit(false);

			Controles.Ctrl_Ambiente ctrl_ambiente = new Controles.Ctrl_Ambiente();
			String result_iniciar_ambiente = ctrl_ambiente.iniciar(conn);

			Gson gson = new GsonBuilder().serializeNulls().create();
			resultado = Response.ok(gson.toJson(result_iniciar_ambiente), MediaType.APPLICATION_JSON).build();

			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: iniciar()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: iniciar()" + " ERROR: " + ex.toString());
		} finally {
			try {
				if (conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (Exception ex) {
				System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
						+ " ==> METODO: iniciar()-Finally" + " ERROR: " + ex.toString());
			}
		}

		return resultado;
	}

}
