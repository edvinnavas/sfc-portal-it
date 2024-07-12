package Recursos;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Controles.Ctrl_Base_Datos;
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

@Path("sfc/rol")
public class Recurso_Rol implements Serializable {

	private static final long serialVersionUID = 1L;

	@RolesAllowed("ADMIN")
	@Path("obtener-lista")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response obtener_lista() {
		Response resultado;
		Connection conn = null;

		try {
			Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
			conn = ctrl_base_datos.obtener_conexion_mysql();

			conn.setAutoCommit(false);

			Controles.Ctrl_Rol ctrl_rol = new Controles.Ctrl_Rol();
			List<Entidades.Rol> lista_rol = ctrl_rol.obtener_lista(conn);

			Gson gson = new GsonBuilder().serializeNulls().create();
			resultado = Response.ok(gson.toJson(lista_rol), MediaType.APPLICATION_JSON).build();

			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_lista()" + " ERROR: " + ex.toString());
		} finally {
			try {
				if (conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (Exception ex) {
				System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
						+ " ==> METODO: obtener_lista()-Finally" + " ERROR: " + ex.toString());
			}
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("obtener-id/{id_rol}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response obtener_id(@PathParam("id_rol") Long id_rol) {
		Response resultado;
		Connection conn = null;

		try {
			Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
			conn = ctrl_base_datos.obtener_conexion_mysql();

			conn.setAutoCommit(false);

			Controles.Ctrl_Rol ctrl_rol = new Controles.Ctrl_Rol();
			Entidades.Rol rol = ctrl_rol.obtener_id(conn, id_rol);

			Gson gson = new GsonBuilder().serializeNulls().create();
			resultado = Response.ok(gson.toJson(rol), MediaType.APPLICATION_JSON).build();

			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_id()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: obtener_id()" + " ERROR: " + ex.toString());
		} finally {
			try {
				if (conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (Exception ex) {
				System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
						+ " ==> METODO: obtener_id()-Finally" + " ERROR: " + ex.toString());
			}
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("crear")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response crear(String jsonString) {
		Response resultado;
		Connection conn = null;

		try {
			Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
			conn = ctrl_base_datos.obtener_conexion_mysql();

			conn.setAutoCommit(false);

			Controles.Ctrl_Rol ctrl_rol = new Controles.Ctrl_Rol();
			Entidades.Rol rol = ctrl_rol.crear(conn, jsonString);

			Gson gson = new GsonBuilder().serializeNulls().create();
			resultado = Response.ok(gson.toJson(rol), MediaType.APPLICATION_JSON).build();

			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: crear()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: crear()" + " ERROR: " + ex.toString());
		} finally {
			try {
				if (conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (Exception ex) {
				System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
						+ " ==> METODO: crear()-Finally" + " ERROR: " + ex.toString());
			}
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("modificar/{id_rol}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	public Response modificar(@PathParam("id_rol") Long id_rol, String jsonString) {
		Response resultado;
		Connection conn = null;

		try {
			Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
			conn = ctrl_base_datos.obtener_conexion_mysql();

			conn.setAutoCommit(false);

			Controles.Ctrl_Rol ctrl_rol = new Controles.Ctrl_Rol();
			Entidades.Rol rol = ctrl_rol.modificar(conn, id_rol, jsonString);

			Gson gson = new GsonBuilder().serializeNulls().create();
			resultado = Response.ok(gson.toJson(rol), MediaType.APPLICATION_JSON).build();

			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: modificar()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: modificar()" + " ERROR: " + ex.toString());
		} finally {
			try {
				if (conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (Exception ex) {
				System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
						+ " ==> METODO: modificar()-Finally" + " ERROR: " + ex.toString());
			}
		}

		return resultado;
	}

	@RolesAllowed("ADMIN")
	@Path("eliminar/{id_rol}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	public Response eliminar(@PathParam("id_rol") Long id_rol) {
		Response resultado;
		Connection conn = null;

		try {
			Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
			conn = ctrl_base_datos.obtener_conexion_mysql();

			conn.setAutoCommit(false);

			Controles.Ctrl_Rol ctrl_rol = new Controles.Ctrl_Rol();
			Entidades.Rol rol = ctrl_rol.eliminar(conn, id_rol);

			Gson gson = new GsonBuilder().serializeNulls().create();
			resultado = Response.ok(gson.toJson(rol), MediaType.APPLICATION_JSON).build();

			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			String mensaje = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: eliminar()" + " ERROR: " + ex.toString();

			resultado = Response.status(Status.NOT_FOUND).entity(mensaje).build();

			System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
					+ " ==> METODO: eliminar()" + " ERROR: " + ex.toString());
		} finally {
			try {
				if (conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (Exception ex) {
				System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
						+ " ==> METODO: eliminar()-Finally" + " ERROR: " + ex.toString());
			}
		}

		return resultado;
	}

}
