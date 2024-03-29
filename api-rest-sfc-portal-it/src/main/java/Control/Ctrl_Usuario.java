package Control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;

public class Ctrl_Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public String autenticar(String usuario, String contrasena) {
        String resultado = "";
        Connection conn = null;

        try {
            Hashtable props = new Hashtable();
            String principalName = usuario + "@grupoterra.com";
            props.put(Context.SECURITY_PRINCIPAL, principalName);
            props.put(Context.SECURITY_CREDENTIALS, contrasena);

            // AUTENTICAR USUARIO EN EL DOMINIO LDAP.
            Boolean autenticado;
            try {
                DirContext context = com.sun.jndi.ldap.LdapCtxFactory.getLdapCtxInstance("ldap://10.253.7.205:389" + '/', props);
                context.close();
                autenticado = true;
            } catch (Exception ex) {
                autenticado = false;
                resultado = "Usuario no autenticado.";
            }

            if (autenticado) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

                Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
                conn = ctrl_base_datos.obtener_conexion_mysql();

                conn.setAutoCommit(false);

                Long id_usuario = Long.valueOf("0");
                Long id_rol = Long.valueOf("0");
                String cadenasql = "SELECT "
                        + "U.ID_USUARIO, "
                        + "U.ID_ROL "
                        + "FROM "
                        + "USUARIO U "
                        + "WHERE "
                        + "U.NOMBRE_USUARIO='" + usuario + "' AND "
                        + "TRIM(CONVERT(U.CONTRASENA USING UTF8MB4))=TRIM(SHA2('admin2023',512))";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    id_usuario = rs.getLong(1);
                    id_rol = rs.getLong(2);
                }
                rs.close();
                stmt.close();

                List<Entidad.Menu> lista_menu = new ArrayList<>();
                cadenasql = "SELECT "
                        + "RM.ID_ROL, "
                        + "M.ID_MENU, "
                        + "M.NOMBRE "
                        + "FROM "
                        + "ROL_MENU RM LEFT JOIN MENU M ON (RM.ID_MENU=M.ID_MENU) "
                        + "WHERE "
                        + "RM.ID_ROL=" + id_rol;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    Entidad.Menu entidad_menu = new Entidad.Menu();
                    entidad_menu.setId_menu(rs.getLong(2));
                    entidad_menu.setNombre(rs.getString(3));
                    lista_menu.add(entidad_menu);
                }
                rs.close();
                stmt.close();

                Entidad.Rol entidad_rol = new Entidad.Rol();
                cadenasql = "SELECT "
                        + "R.ID_ROL, "
                        + "R.NOMBRE, "
                        + "R.ACTIVO, "
                        + "R.FECHA_HORA "
                        + "FROM "
                        + "ROL R "
                        + "WHERE "
                        + "R.ID_ROL=" + id_rol;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    entidad_rol.setId_rol(rs.getLong(1));
                    entidad_rol.setNombre(rs.getString(2));
                    entidad_rol.setActivo(rs.getInt(3));
                    entidad_rol.setFecha_hora(dateFormat.format(rs.getDate(4)));
                }
                rs.close();
                stmt.close();
                entidad_rol.setLista_menu(lista_menu);

                cadenasql = "SELECT "
                        + "U.ID_USUARIO, "
                        + "U.NOMBRE_COMPLETO, "
                        + "U.NOMBRE_USUARIO, "
                        + "'SECRETO' CONTRASENA, "
                        + "U.CORREO_ELECTRONICO, "
                        + "U.ACTIVO, "
                        + "U.DESCRIPCION, "
                        + "U.FECHA_HORA "
                        + "FROM "
                        + "USUARIO U "
                        + "WHERE "
                        + "U.ID_USUARIO=" + id_usuario;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                Entidad.Usuario entidad_usuario = null;
                while (rs.next()) {
                    entidad_usuario = new Entidad.Usuario();
                    entidad_usuario.setId_usuario(rs.getLong(1));
                    entidad_usuario.setNombre_completo(rs.getString(2));
                    entidad_usuario.setNombre_usuario(rs.getString(3));
                    entidad_usuario.setContrasena(rs.getString(4));
                    entidad_usuario.setCorreo_electronico(rs.getString(5));
                    entidad_usuario.setActivo(rs.getInt(6));
                    entidad_usuario.setDescripcion(rs.getString(7));
                    entidad_usuario.setFecha_hora(dateFormat.format(rs.getDate(8)));
                    entidad_usuario.setRol(entidad_rol);
                }
                rs.close();
                stmt.close();

                conn.commit();
                conn.setAutoCommit(true);

                if (entidad_usuario == null) {
                    resultado = "Usuario no autenticado.";
                } else {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    resultado = gson.toJson(entidad_usuario);
                }
            }
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar(), ERRROR: " + ex.toString();
                    System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar(), ERRROR: " + ex.toString());
                }
            } catch (Exception ex1) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-autenticar(), ERRROR: " + ex.toString();
                System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-autenticar(), ERRROR: " + ex.toString());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-autenticar(), ERRROR: " + ex.toString();
                System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-autenticar(), ERRROR: " + ex.toString());
            }
        }

        return resultado;
    }
    
    public String autenticar_local(String usuario, String contrasena) {
        String resultado = "";
        Connection conn = null;

        try {
            System.out.println("AUTENTICACION ==> [" + usuario + ", " + contrasena + "]");
            Boolean autenticado = true;
            if (autenticado) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

                Ctrl_Base_Datos ctrl_base_datos = new Ctrl_Base_Datos();
                conn = ctrl_base_datos.obtener_conexion_mysql();

                conn.setAutoCommit(false);

                Long id_usuario = Long.valueOf("0");
                Long id_rol = Long.valueOf("0");
                String cadenasql = "SELECT "
                        + "U.ID_USUARIO, "
                        + "U.ID_ROL "
                        + "FROM "
                        + "USUARIO U "
                        + "WHERE "
                        + "U.NOMBRE_USUARIO='" + usuario + "' AND "
                        + "TRIM(CONVERT(U.CONTRASENA USING UTF8MB4))=TRIM(SHA2('admin2023',512))";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    id_usuario = rs.getLong(1);
                    id_rol = rs.getLong(2);
                }
                rs.close();
                stmt.close();

                List<Entidad.Menu> lista_menu = new ArrayList<>();
                cadenasql = "SELECT "
                        + "RM.ID_ROL, "
                        + "M.ID_MENU, "
                        + "M.NOMBRE "
                        + "FROM "
                        + "ROL_MENU RM LEFT JOIN MENU M ON (RM.ID_MENU=M.ID_MENU) "
                        + "WHERE "
                        + "RM.ID_ROL=" + id_rol;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    Entidad.Menu entidad_menu = new Entidad.Menu();
                    entidad_menu.setId_menu(rs.getLong(2));
                    entidad_menu.setNombre(rs.getString(3));
                    lista_menu.add(entidad_menu);
                }
                rs.close();
                stmt.close();

                Entidad.Rol entidad_rol = new Entidad.Rol();
                cadenasql = "SELECT "
                        + "R.ID_ROL, "
                        + "R.NOMBRE, "
                        + "R.ACTIVO, "
                        + "R.FECHA_HORA "
                        + "FROM "
                        + "ROL R "
                        + "WHERE "
                        + "R.ID_ROL=" + id_rol;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                while (rs.next()) {
                    entidad_rol.setId_rol(rs.getLong(1));
                    entidad_rol.setNombre(rs.getString(2));
                    entidad_rol.setActivo(rs.getInt(3));
                    entidad_rol.setFecha_hora(dateFormat.format(rs.getDate(4)));
                }
                rs.close();
                stmt.close();
                entidad_rol.setLista_menu(lista_menu);

                cadenasql = "SELECT "
                        + "U.ID_USUARIO, "
                        + "U.NOMBRE_COMPLETO, "
                        + "U.NOMBRE_USUARIO, "
                        + "'SECRETO' CONTRASENA, "
                        + "U.CORREO_ELECTRONICO, "
                        + "U.ACTIVO, "
                        + "U.DESCRIPCION, "
                        + "U.FECHA_HORA "
                        + "FROM "
                        + "USUARIO U "
                        + "WHERE "
                        + "U.ID_USUARIO=" + id_usuario;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(cadenasql);
                Entidad.Usuario entidad_usuario = null;
                while (rs.next()) {
                    entidad_usuario = new Entidad.Usuario();
                    entidad_usuario.setId_usuario(rs.getLong(1));
                    entidad_usuario.setNombre_completo(rs.getString(2));
                    entidad_usuario.setNombre_usuario(rs.getString(3));
                    entidad_usuario.setContrasena(rs.getString(4));
                    entidad_usuario.setCorreo_electronico(rs.getString(5));
                    entidad_usuario.setActivo(rs.getInt(6));
                    entidad_usuario.setDescripcion(rs.getString(7));
                    entidad_usuario.setFecha_hora(dateFormat.format(rs.getDate(8)));
                    entidad_usuario.setRol(entidad_rol);
                }
                rs.close();
                stmt.close();

                conn.commit();
                conn.setAutoCommit(true);

                if (entidad_usuario == null) {
                    resultado = "Usuario no autenticado.";
                } else {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    resultado = gson.toJson(entidad_usuario);
                }
            }
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn = null;
                    resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar_local(), ERRROR: " + ex.toString();
                    System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: autenticar_local(), ERRROR: " + ex.toString());
                }
            } catch (Exception ex1) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-autenticar_local(), ERRROR: " + ex.toString();
                System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: rollback-autenticar_local(), ERRROR: " + ex.toString());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-autenticar_local(), ERRROR: " + ex.toString();
                System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: finally-autenticar_local(), ERRROR: " + ex.toString());
            }
        }

        return resultado;
    }

    public String login3(String user, String pass) {
        String resultado;

        try {
            Hashtable props = new Hashtable();
            String principalName = user + "@grupoterra.com";
            props.put(Context.SECURITY_PRINCIPAL, principalName);
            props.put(Context.SECURITY_CREDENTIALS, pass);

            try {
                DirContext context = com.sun.jndi.ldap.LdapCtxFactory.getLdapCtxInstance("ldap://10.253.7.205:389" + '/', props);
                context.close();
                resultado = "1,Usuario " + user + " autenticado.";
            } catch (Exception ex) {
                resultado = "0,Usuario " + user + " no autenticado.";
            }
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: login3(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: login3(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

}
