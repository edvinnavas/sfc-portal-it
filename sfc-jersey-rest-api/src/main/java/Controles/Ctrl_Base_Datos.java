package Controles;

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

    public Connection obtener_conexion_mysql() {
        Connection resultado;

        try {
            String mysql_db_host = "";
            String mysql_db_port = "";
            String mysql_db_name = "";
            String mysql_db_user = "";
            String mysql_db_pass = "";

            Ctrl_Archivos ctrl_archivos = new Ctrl_Archivos();
            List<String> lineas_archivos = ctrl_archivos
                    .lineas_archivo("C:/VolumeDocker/SFC_PORTAL_IT/Configuracion/properties.conf");

            for (Integer i = 0; i < lineas_archivos.size(); i++) {
                String[] param_db = lineas_archivos.get(i).trim().split(":");

                if (param_db[0].trim().equals("mysql-db-host")) {
                    mysql_db_host = param_db[1];
                    // System.out.println("mysql-db-host:" + mysql_db_host);
                }
                if (param_db[0].trim().equals("mysql-db-port")) {
                    mysql_db_port = param_db[1];
                    // System.out.println("mysql-db-port:" + mysql_db_port);
                }
                if (param_db[0].trim().equals("mysql-db-name")) {
                    mysql_db_name = param_db[1];
                    // System.out.println("mysql-db-name:" + mysql_db_name);
                }
                if (param_db[0].trim().equals("mysql-db-user")) {
                    mysql_db_user = param_db[1];
                    // System.out.println("mysql-db-user:" + mysql_db_user);
                }
                if (param_db[0].trim().equals("mysql-db-pass")) {
                    mysql_db_pass = param_db[1];
                    // System.out.println("mysql-db-pass:" + mysql_db_pass);
                }
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            resultado = DriverManager.getConnection(
                    "jdbc:mysql://" + mysql_db_host + ":" + mysql_db_port + "/" + mysql_db_name, mysql_db_user,
                    mysql_db_pass);

            // System.out.println("Conexión MySQL satisfactoria!!!");
        } catch (Exception ex) {
            resultado = null;
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: obtener_conexion_mysql()" + " ERROR: " + ex.toString());
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
            resultado = "PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerString()" + " ERROR: " + ex.toString();
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerString()" + " ERROR: " + ex.toString());
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
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerEntero()" + " ERROR: " + ex.toString());
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
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerLong()" + " ERROR: " + ex.toString());
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
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerDouble()" + " ERROR: " + ex.toString());
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
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerVectorString()" + " ERROR: " + ex.toString());
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
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerVectorEntero()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

    public List<Long> ObtenerVectorLong(String cadenasql, Connection conn) {
        List<Long> resultado = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                resultado.add(rs.getLong(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            resultado = new ArrayList<>();
            resultado.add(Long.valueOf("-1"));
            System.out.println("PROYECTO: SFC-JERSEY-REST-API ==> CLASE: " + this.getClass().getName()
                    + " ==> METODO: ObtenerVectorLong()" + " ERROR: " + ex.toString());
        }

        return resultado;
    }

}
