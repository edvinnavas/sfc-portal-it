package Control;

import Entidad.Usuario;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;

public class Control_Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public String buscar_usuarios() {
        String resultado;
        List<Usuario> lista_usuario = new ArrayList<>();
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "select "
                    + "a.id_usuario, "
                    + "a.usuario, "
                    + "a.nombre_completo, "
                    + "a.correo, "
                    + "a.fecha_nacimiento, "
                    + "'-' contrasena, "
                    + "a.descripcion, "
                    + "a.activo, "
                    + "a.editable, "
                    + "a.eliminable, "
                    + "a.id_rol "
                    + "from usuario a";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getLong(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getLong(11));
                lista_usuario.add(usuario);
            }
            rs.close();
            stmt.close();

            conn.commit();

            resultado = new Gson().toJson(lista_usuario);
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_usuarios ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_usuarios ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_usuarios - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_usuarios - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_usuarios - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: buscar_usuarios - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String buscar_usuario(Long id_usuario) {
        String resultado;
        List<Usuario> lista_usuario = new ArrayList<>();
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "select "
                    + "a.id_usuario, "
                    + "a.usuario, "
                    + "a.nombre_completo, "
                    + "a.correo, "
                    + "a.fecha_nacimiento, "
                    + "'-' contrasena, "
                    + "a.descripcion, "
                    + "a.activo, "
                    + "a.editable, "
                    + "a.eliminable, "
                    + "a.id_rol "
                    + "from usuario a "
                    + "where "
                    + "a.id_usuario=" + id_usuario;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getLong(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getLong(11));
                lista_usuario.add(usuario);
            }
            rs.close();
            stmt.close();

            conn.commit();

            resultado = new Gson().toJson(lista_usuario);
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: buscar_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: buscar_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String crear_usuario(String jsonSring) {
        String resultado;
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Usuario>>() {
            }.getType();
            List<Usuario> lista_usuario = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            for (Integer i = 0; i < lista_usuario.size(); i++) {
                String cadenasql = "insert into usuario ("
                        + "usuario, "
                        + "nombre_completo, "
                        + "correo, "
                        + "fecha_nacimiento, "
                        + "contrasena, "
                        + "descripcion, "
                        + "activo, "
                        + "editable, "
                        + "eliminable, "
                        + "id_rol) values ('"
                        + lista_usuario.get(i).getUsuario() + "','"
                        + lista_usuario.get(i).getNombre_completo() + "','"
                        + lista_usuario.get(i).getCorreo() + "',"
                        + lista_usuario.get(i).getFecha_nacimiento() + ","
                        + "SHA1('" + lista_usuario.get(i).getContrasena() + "'),'"
                        + lista_usuario.get(i).getDescripcion() + "',"
                        + lista_usuario.get(i).getActivo() + ","
                        + lista_usuario.get(i).getEditable() + ","
                        + lista_usuario.get(i).getEliminable() + ","
                        + lista_usuario.get(i).getId_rol() + ")";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            conn.commit();

            resultado = "Usuario creado en la aplicación.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: crear_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: crear_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: crear_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: crear_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: crear_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: crear_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String modificar_usuario(Long id_usuario, String jsonSring) {
        String resultado;
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Usuario>>() {
            }.getType();
            List<Usuario> lista_usuario = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            for (Integer i = 0; i < lista_usuario.size(); i++) {
                String cadenasql = "update usuario set "
                        + "usuario='" + lista_usuario.get(i).getUsuario() + "', "
                        + "nombre_completo='" + lista_usuario.get(i).getNombre_completo() + "', "
                        + "correo='" + lista_usuario.get(i).getCorreo() + "', "
                        + "fecha_nacimiento=" + lista_usuario.get(i).getFecha_nacimiento() + ", "
                        + "descripcion='" + lista_usuario.get(i).getDescripcion() + "', "
                        + "activo=" + lista_usuario.get(i).getActivo() + ", "
                        + "editable=" + lista_usuario.get(i).getEditable() + ", "
                        + "eliminable=" + lista_usuario.get(i).getEliminable() + ", "
                        + "id_rol=" + lista_usuario.get(i).getId_rol() + " "
                        + "where "
                        + "id_usuario=" + id_usuario;
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();
            }

            conn.commit();

            resultado = "Usuario modificado en la aplicación.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: modificar_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: modificar_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: modificar_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: modificar_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String eliminar_usuario(Long id_usuario) {
        String resultado;
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "select e.id_usuario from evento e where e.id_usuario=" + id_usuario;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            Boolean utilizado = false;
            while (rs.next()) {
                utilizado = true;
            }
            rs.close();
            stmt.close();

            if (!utilizado) {
                cadenasql = "delete from usuario where id_usuario=" + id_usuario;
                stmt = conn.createStatement();
                stmt.executeUpdate(cadenasql);
                stmt.close();

                resultado = "Usuario eliminado en la aplicación.";
            } else {
                resultado = "El esta siendo utilizado, no es posible eliminarlo.";
            }

            conn.commit();
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: eliminar_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: eliminar_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: eliminar_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: eliminar_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String login(String jsonSring) {
        String resultado = "0,Usuario o contraseña incorrectos.";
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Usuario>>() {
            }.getType();
            List<Usuario> lista_usuario = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            for (Integer i = 0; i < lista_usuario.size(); i++) {
                String cadenasql = "select "
                        + "a.activo "
                        + "from "
                        + "usuario a "
                        + "where "
                        + "a.usuario='" + lista_usuario.get(i).getUsuario() + "' and "
                        + "a.contrasena=SHA1('" + lista_usuario.get(i).getContrasena() + "')";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cadenasql);
                Integer autenticado = 0;
                Integer activo = 0;
                while (rs.next()) {
                    autenticado = 1;
                    activo = rs.getInt(1);
                }
                rs.close();
                stmt.close();

                if (autenticado == 1) {
                    if (activo == 1) {
                        resultado = "1,Usuario: " + lista_usuario.get(i).getUsuario() + " registrado!";
                    } else {
                        resultado = "0,Usuario: " + lista_usuario.get(i).getUsuario() + " inactivo!";
                    }
                }
            }

            conn.commit();

        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: login ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: login ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: login - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: login - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: login - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: login - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String reset_pass_usuario(Long id_usuario) {
        String resultado;
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String app_usuario = "";
            String cadenasql = "select usuario from usuario where id_usuario=" + id_usuario;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                app_usuario = rs.getString(1);
            }
            rs.close();
            stmt.close();

            cadenasql = "update usuario set contrasena=SHA1('" + app_usuario + "') where id_usuario=" + id_usuario;
            stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();

            resultado = "Constraseña de usuario iniciada.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: reset_pass_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: reset_pass_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: reset_pass_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: reset_pass_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: reset_pass_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: reset_pass_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String cambiar_pass_usuario(Long id_usuario, String pass) {
        String resultado;
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "update usuario set contrasena=SHA1('" + pass + "') where id_usuario=" + id_usuario;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();

            resultado = "Constraseña de usuario modificada.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String desactivar_usuario(Long id_usuario) {
        String resultado;
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "update usuario set activo=0 where id_usuario=" + id_usuario;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();

            resultado = "Usuario desactivo.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String activar_usuario(Long id_usuario) {
        String resultado;
        Connection conn = null;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            String cadenasql = "update usuario set activo=1 where id_usuario=" + id_usuario;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            conn.commit();

            resultado = "Usuario activo.";
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: cambiar_pass_usuario - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String login2(String jsonSring) {
        String resultado = "0,Usuario o contraseña incorrectos.";
        Connection conn = null;

        try {
            Type listType = new TypeToken<ArrayList<Usuario>>() {
            }.getType();
            List<Usuario> lista_usuario = new Gson().fromJson(jsonSring, listType);

            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            for (Integer i = 0; i < lista_usuario.size(); i++) {
                String cadenasql = "select "
                        + "a.activo "
                        + "from "
                        + "usuario a "
                        + "where "
                        + "a.usuario='" + lista_usuario.get(i).getUsuario() + "'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cadenasql);
                Boolean existe = false;
                Integer activo = 0;
                while (rs.next()) {
                    existe = true;
                    activo = rs.getInt(1);
                }
                rs.close();
                stmt.close();

                if (existe) {
                    if (activo == 1) {
                        Hashtable props = new Hashtable();
                        String principalName = lista_usuario.get(i).getUsuario() + "@grupoterra.com";
                        props.put(Context.SECURITY_PRINCIPAL, principalName);
                        props.put(Context.SECURITY_CREDENTIALS, lista_usuario.get(i).getContrasena());

                        try {
                            DirContext context = com.sun.jndi.ldap.LdapCtxFactory.getLdapCtxInstance("ldap://10.253.7.205:389" + '/', props);
                            context.close();

                            resultado = "1,Usuario: " + lista_usuario.get(i).getUsuario() + " registrado!";
                        } catch (Exception ex) {
                            resultado = "0,Usuario o contraseña incorrectos. => " + ex.toString();
                        }
                    } else {
                        resultado = "0,Usuario: " + lista_usuario.get(i).getUsuario() + " inactivo!";
                    }
                }
            }

            conn.commit();

        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: login ERROR: " + ex.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: login ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: login - rollback ERROR: " + ex1.toString());
                resultado = "CLASE: " + this.getClass().getName() + " METODO: login - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: login - finally ERROR: " + ex.toString());
                resultado = "ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: login - finally ERROR: " + ex.toString();
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
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: login3 ERROR: " + ex.toString());
            resultado = "CLASE: " + this.getClass().getName() + " METODO: login3 ERROR: " + ex.toString();
        }

        return resultado;
    }

}
