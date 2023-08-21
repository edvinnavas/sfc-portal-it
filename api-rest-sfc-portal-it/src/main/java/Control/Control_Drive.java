package Control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.sql.ResultSetMetaData;

public class Control_Drive implements Serializable {

    private static final long serialVersionUID = 1L;

    public String drive(String cadenasql) {
        String resultado = "";
        List<String> lista_drive = new ArrayList<>();
        String linea_drive = "";
        Connection conn = null;

        Integer filas = 0;
        Integer columnas = 0;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_mysql("PY");

            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            ResultSetMetaData metadatos = rs.getMetaData();
            columnas = metadatos.getColumnCount();
            while (rs.next()) {
                filas++;
            }

            //Agrega los nombre de las columnas a la tabla.
            Integer i = 0;
            for (Integer j = 0; j < columnas; j++) {
                if (j == 0) {
                    linea_drive = metadatos.getColumnLabel(j + 1);
                } else {
                    linea_drive = linea_drive + "♣" + metadatos.getColumnLabel(j + 1);
                }
            }
            rs.close();
            stmt.close();
            lista_drive.add(linea_drive);

            //Llena tabla con la informacion de la consulta.
            i = 1;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                for (Integer j = 0; j < columnas; j++) {
                    if (rs.getString(j + 1) == null) {
                        if (j == 0) {
                            linea_drive = "-";
                        } else {
                            linea_drive = linea_drive + "♣" + "-";
                        }
                    } else {
                        char caracter_old = (char) 31;
                        char caracter_new = (char) 32;
                        if (j == 0) {
                            linea_drive = rs.getString(j + 1).replace(caracter_old, caracter_new);
                        } else {
                            linea_drive = linea_drive + "♣" + rs.getString(j + 1).replace(caracter_old, caracter_new);
                        }
                    }
                }
                lista_drive.add(linea_drive);
                i++;
            }

            conn.commit();

            resultado = new Gson().toJson(lista_drive);
        } catch (Exception ex) {
            try {
                conn.rollback();
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_menus ERROR: " + ex.toString());
                resultado = "1,CLASE: " + this.getClass().getName() + " METODO: buscar_menus ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_menus - rollback ERROR: " + ex1.toString());
                resultado = "1,CLASE: " + this.getClass().getName() + " METODO: buscar_menus - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: buscar_menus - finally ERROR: " + ex.toString());
                resultado = "1,ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: buscar_menus - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

    public String drive_fel_energia(String ambiente, String cadenasql) {
        String resultado = "";
        List<String> lista_drive = new ArrayList<>();
        String linea_drive = "";
        Connection conn = null;

        // Integer filas = 0;
        Integer columnas;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_fel_energia(ambiente);

            // INICIA TRANSACCION.
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            ResultSetMetaData metadatos = rs.getMetaData();
            columnas = metadatos.getColumnCount();
            while (rs.next()) {
                // filas++;
            }

            // Agrega los nombre de las columnas a la tabla.
            Integer i = 0;
            for (Integer j = 0; j < columnas; j++) {
                if (j == 0) {
                    linea_drive = metadatos.getColumnLabel(j + 1);
                } else {
                    linea_drive = linea_drive + "♣" + metadatos.getColumnLabel(j + 1);
                }
            }
            rs.close();
            stmt.close();
            lista_drive.add(linea_drive);

            // Llena tabla con la informacion de la consulta.
            i = 1;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                for (Integer j = 0; j < columnas; j++) {
                    if (rs.getString(j + 1) == null) {
                        if (j == 0) {
                            linea_drive = "-";
                        } else {
                            linea_drive = linea_drive + "♣" + "-";
                        }
                    } else {
                        char caracter_old = (char) 31;
                        char caracter_new = (char) 32;
                        if (j == 0) {
                            linea_drive = rs.getString(j + 1).replace(caracter_old, caracter_new);
                        } else {
                            linea_drive = linea_drive + "♣" + rs.getString(j + 1).replace(caracter_old, caracter_new);
                        }
                    }
                }
                lista_drive.add(linea_drive);
                i++;
            }

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = new Gson().toJson(lista_drive);
        } catch (Exception ex) {
            try {
                conn.rollback();

                System.out.println("CLASE: " + this.getClass().getName() + " METODO: drive ERROR: " + ex.toString());
                resultado = "1,CLASE: " + this.getClass().getName() + " METODO: drive ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: drive - rollback ERROR: " + ex1.toString());
                resultado = "1,CLASE: " + this.getClass().getName() + " METODO: drive - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: drive - finally ERROR: " + ex.toString());
                resultado = "1,ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: drive - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }
    
    public String drive_cargas_masivas(String ambiente, String cadenasql) {
        String resultado = "";
        List<String> lista_drive = new ArrayList<>();
        String linea_drive = "";
        Connection conn = null;

        // Integer filas = 0;
        Integer columnas;

        try {
            Control_Base_Datos control_base_datos = new Control_Base_Datos();
            conn = control_base_datos.obtener_conexion_cargas_masivas(ambiente);

            // INICIA TRANSACCION.
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cadenasql);
            ResultSetMetaData metadatos = rs.getMetaData();
            columnas = metadatos.getColumnCount();
            while (rs.next()) {
                // filas++;
            }

            // Agrega los nombre de las columnas a la tabla.
            Integer i = 0;
            for (Integer j = 0; j < columnas; j++) {
                if (j == 0) {
                    linea_drive = metadatos.getColumnLabel(j + 1);
                } else {
                    linea_drive = linea_drive + "♣" + metadatos.getColumnLabel(j + 1);
                }
            }
            rs.close();
            stmt.close();
            lista_drive.add(linea_drive);

            // Llena tabla con la informacion de la consulta.
            i = 1;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(cadenasql);
            while (rs.next()) {
                for (Integer j = 0; j < columnas; j++) {
                    if (rs.getString(j + 1) == null) {
                        if (j == 0) {
                            linea_drive = "-";
                        } else {
                            linea_drive = linea_drive + "♣" + "-";
                        }
                    } else {
                        char caracter_old = (char) 31;
                        char caracter_new = (char) 32;
                        if (j == 0) {
                            linea_drive = rs.getString(j + 1).replace(caracter_old, caracter_new);
                        } else {
                            linea_drive = linea_drive + "♣" + rs.getString(j + 1).replace(caracter_old, caracter_new);
                        }
                    }
                }
                lista_drive.add(linea_drive);
                i++;
            }

            // TERMINA TRANSACCION.
            conn.commit();
            conn.setAutoCommit(true);

            resultado = new Gson().toJson(lista_drive);
        } catch (Exception ex) {
            try {
                conn.rollback();

                System.out.println("CLASE: " + this.getClass().getName() + " METODO: drive ERROR: " + ex.toString());
                resultado = "1,CLASE: " + this.getClass().getName() + " METODO: drive ERROR: " + ex.toString();
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: drive - rollback ERROR: " + ex1.toString());
                resultado = "1,CLASE: " + this.getClass().getName() + " METODO: drive - rollback ERROR: " + ex1.toString();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: drive - finally ERROR: " + ex.toString());
                resultado = "1,ERROR: " + "CLASE: " + this.getClass().getName() + " METODO: drive - finally ERROR: " + ex.toString();
            }
        }

        return resultado;
    }

}
