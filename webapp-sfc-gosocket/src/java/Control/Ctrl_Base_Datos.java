package Control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Ctrl_Base_Datos implements Serializable {

    private static final long serialVersionUID = 1L;

    public Ctrl_Base_Datos() {
    }
    
    public Connection obtener_conexion_felcr(String ambiente) {
        Connection resultado;

        try {
            String host_interfase_db = "10.252.7.204";
            String usuario_db;
            String contrasena_db;

            if (ambiente.equals("PY")) {
                usuario_db = "FACT_CR_TEST";
                contrasena_db = "cr2019";
            } else {
                usuario_db = "FACT_CR";
                contrasena_db = ")_YJMC52B(U%\\QK}";
            }

            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            resultado = DriverManager.getConnection("jdbc:oracle:thin:@//" + host_interfase_db + ":1521/unopetrol", usuario_db, contrasena_db);
            // System.out.println("Conexión satisfactoria: " + usuario_db);
        } catch (Exception ex) {
            resultado = null;
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_conexion_felcr(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public Connection obtener_conexion_jde_petroleos(String ambiente) {
        Connection resultado;

        try {
            String host_interfase_db;
            String db_name;
            String usuario_db;
            String contrasena_db;

            if (ambiente.equals("PY")) {
                host_interfase_db = "10.252.7.207";
                db_name = "jdepy";
                usuario_db = "CRPDTA";
                contrasena_db = "CRPDTA";
            } else {
                host_interfase_db = "10.252.7.201";
                db_name = "jdepd";
                usuario_db = "PRODDTA";
                contrasena_db = "PRODDTA";
            }

            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            resultado = DriverManager.getConnection("jdbc:oracle:thin:@//" + host_interfase_db + ":1521/" + db_name, usuario_db, contrasena_db);
            // System.out.println("Conexión satisfactoria: " + usuario_db);
        } catch (Exception ex) {
            resultado = null;
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: obtener_conexion_felcr(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public String AmbienteEsquemaJde(String ambiente) {
        String resultado;

        if (ambiente.equals("PY")) {
            resultado = "CRPDTA";
        } else {
            resultado = "PRODDTA";
        }

        return resultado;
    }

    public String AmbienteDBLinkJde(String ambiente) {
        String resultado;

        if (ambiente.equals("PY")) {
            resultado = "JDEPY";
        } else {
            resultado = "JDEPD";
        }

        return resultado;
    }

    public String ObtenerString(String cadenasql, Connection conn) {
        String resultado = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado = rs.getString(1);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = "PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerString(), ERRROR: " + ex.toString();
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerString(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public Integer ObtenerEntero(String cadenasql, Connection conn) {
        Integer resultado = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado = rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = -1;
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerEntero(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public Long ObtenerLong(String cadenasql, Connection conn) {
        Long resultado = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado = rs.getLong(1);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = Long.valueOf(-1);
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerLong(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public Double ObtenerDouble(String cadenasql, Connection conn) {
        Double resultado = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado = rs.getDouble(1);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = -1.00;
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerDouble(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public List<String> ObtenerVectorString(String cadenasql, Connection conn) {
        List<String> resultado = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = new ArrayList<>();
            resultado.add(ex.toString());
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerVectorString(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public List<Integer> ObtenerVectorEntero(String cadenasql, Connection conn) {
        List<Integer> resultado = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado.add(rs.getInt(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = new ArrayList<>();
            resultado.add(-1);
            System.out.println("PROYECTO: api-rest-sfc-portal-it, CLASE: " + this.getClass().getName() + ", METODO: ObtenerVectorEntero(), ERRROR: " + ex.toString());
        }

        return resultado;
    }

    public Integer ExecuteStatementDB(String cadenasql, Connection conn) {
        Integer resultado = 1;

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(cadenasql);
            stmt.close();

            resultado = 0;
        } catch (Exception ex) {
            resultado = -1;
        }

        return resultado;
    }

}
