package Recursos;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import Controles.Ctrl_Base_Datos;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FiltroRecurso implements ContainerRequestFilter, Serializable {

    private static final long serialVersionUID = 1L;

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            Method method = resourceInfo.getResourceMethod();
            // Access allowed for all
            if (!method.isAnnotationPresent(PermitAll.class)) {
                // Access denied for all
                if (method.isAnnotationPresent(DenyAll.class)) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                            .entity("Access blocked for all users !!").build());
                    return;
                }
                // Get request headers
                final MultivaluedMap<String, String> headers = requestContext.getHeaders();
                // Fetch authorization header
                final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
                // If no authorization information present; block access
                if (authorization == null || authorization.isEmpty()) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("You cannot access this resource").build());
                    return;
                }
                // Get encoded username and password
                final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
                // Decode username and password
                String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));
                // Split username and password tokens
                final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                final String username = tokenizer.nextToken();
                final String password = tokenizer.nextToken();
                // Verify user access
                if (method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
                    // Is user valid?
                    if (!isUserAllowed(username, password, rolesSet)) {
                        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                                .entity("You cannot access this resource").build());
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build());
            return;
        }
    }

    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;

        Connection conn = null;

        try {
            Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
            conn = ctrl_base_datos.obtener_conexion_mysql();

            conn.setAutoCommit(false);

            String db_username = ctrl_base_datos.ObtenerString("SELECT A.VALOR FROM PROPIEDAD A WHERE A.NOMBRE = 'AUTH_REST_USER'", conn);
            String db_password = ctrl_base_datos.ObtenerString("SELECT A.VALOR FROM PROPIEDAD A WHERE A.NOMBRE = 'AUTH_REST_PASS'", conn);

            conn.commit();
            conn.setAutoCommit(true);

            if (username.equals(db_username) && password.equals(db_password)) {
                String userRole = "ADMIN";
                // Step 2. Verify user role
                if (rolesSet.contains(userRole)) {
                    isAllowed = true;
                }
            }
        } catch (Exception ex) {
            isAllowed = false;
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: isUserAllowed()" + " ERROR: " + ex.toString());
        } finally {
            try {
                if(conn.isClosed()) {
                    conn.close();
                    conn = null;
                }
            } catch(Exception ex) {
                System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: isUserAllowed()-Finally" + " ERROR: " + ex.toString());
            }
        }

        return isAllowed;
    }

}
