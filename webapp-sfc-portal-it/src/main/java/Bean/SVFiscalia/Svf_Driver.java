package Bean.SVFiscalia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class Svf_Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String path;

    public Svf_Driver() {
        //this.path = "/home/edvin/rep_svf_fiscalia/";
        this.path = "/root/rep_svf_fiscalia/";
    }

    public String getPath() {
        return path;
    }

    public List<SelectItem> lista_SelectItem(String cadenasql) {
        List<SelectItem> lista = new ArrayList<>();

        try {
            Svf_Servicio servicio = new Svf_Servicio();
            List<String> resultado = servicio.reporte(cadenasql);

            Integer filas = resultado.size();
            Integer columnas = resultado.size();
            String[][] vector_result = new String[resultado.size()][columnas];
            for (Integer i = 0; i < resultado.size(); i++) {
                for (Integer j = 0; j < resultado.size(); j++) {
                    vector_result[i][j] = resultado.get(j);
                }
            }

            for (Integer i = 1; i < filas; i++) {
                lista.add(new SelectItem(vector_result[i][0], vector_result[i][1]));
            }
            lista.add(new SelectItem("TODOS", "TODOS"));
        } catch (Exception ex) {
            lista.add(new SelectItem("**ERROR**", "**ERROR**"));
        }

        return lista;
    }

    public List<SelectItem> lista_SelectItem_s(String cadenasql) {
        List<SelectItem> lista = new ArrayList<>();

        try {
            Svf_Servicio servicio = new Svf_Servicio();
            List<String> resultado = servicio.reporte(cadenasql);

            Integer filas = resultado.size();
            Integer columnas = resultado.size();
            String[][] vector_result = new String[resultado.size()][columnas];
            for (Integer i = 0; i < resultado.size(); i++) {
                for (Integer j = 0; j < resultado.size(); j++) {
                    vector_result[i][j] = resultado.get(j);
                }
            }

            for (Integer i = 1; i < filas; i++) {
                lista.add(new SelectItem(vector_result[i][0], vector_result[i][1]));
            }
        } catch (Exception ex) {
            lista.add(new SelectItem("**ERROR**", "**ERROR**"));
        }

        return lista;
    }

    public Integer getInt(String cadenasql) {
        Integer numero = 0;

        try {
            Svf_Servicio servicio = new Svf_Servicio();
            List<String> resultado = servicio.reporte(cadenasql);

            Integer filas = resultado.size();
            Integer columnas = resultado.size();
            String[][] vector_result = new String[resultado.size()][columnas];
            for (Integer i = 0; i < resultado.size(); i++) {
                for (Integer j = 0; j < resultado.size(); j++) {
                    vector_result[i][j] = resultado.get(j);
                }
            }

            for (Integer i = 1; i < filas; i++) {
                numero = Integer.valueOf(vector_result[i][0]);
            }
        } catch (Exception ex) {
            numero = 0;
        }

        return numero;
    }

    public String getString(String cadenasql) {
        String resul_string = "";

        try {
            Svf_Servicio servicio = new Svf_Servicio();
            List<String> resultado = servicio.reporte(cadenasql);

            Integer filas = resultado.size();
            Integer columnas = resultado.size();
            String[][] vector_result = new String[resultado.size()][columnas];
            for (Integer i = 0; i < resultado.size(); i++) {
                for (Integer j = 0; j < resultado.size(); j++) {
                    vector_result[i][j] = resultado.get(j);
                }
            }

            for (Integer i = 1; i < filas; i++) {
                resul_string = vector_result[i][0];
            }
        } catch (Exception ex) {
            resul_string = "";
        }

        return resul_string;
    }

    public List<SelectItem> lista_meses() {
        List<SelectItem> lst_meses = new ArrayList<>();

        try {
            lst_meses.add(new SelectItem(1, "ENERO"));
            lst_meses.add(new SelectItem(2, "FEBRERO"));
            lst_meses.add(new SelectItem(3, "MARZO"));
            lst_meses.add(new SelectItem(4, "ABRIL"));
            lst_meses.add(new SelectItem(5, "MAYO"));
            lst_meses.add(new SelectItem(6, "JUNIO"));
            lst_meses.add(new SelectItem(7, "JULIO"));
            lst_meses.add(new SelectItem(8, "AGOSTO"));
            lst_meses.add(new SelectItem(9, "SEPTIEMBRE"));
            lst_meses.add(new SelectItem(10, "OCTUBRE"));
            lst_meses.add(new SelectItem(11, "NOVIEMBRE"));
            lst_meses.add(new SelectItem(12, "DICIEMBRE"));
        } catch (Exception ex) {
            lst_meses.add(new SelectItem("**ERROR**", "**ERROR**"));
        }

        return lst_meses;
    }

    public Integer dar_mes_num(String mes) {
        Integer resultado = 0;

        if (mes.equals("ENERO")) {
            resultado = 1;
        }
        if (mes.equals("FEBRERO")) {
            resultado = 2;
        }
        if (mes.equals("MARZO")) {
            resultado = 3;
        }
        if (mes.equals("ABRIL")) {
            resultado = 4;
        }
        if (mes.equals("MAYO")) {
            resultado = 5;
        }
        if (mes.equals("JUNIO")) {
            resultado = 6;
        }
        if (mes.equals("JULIO")) {
            resultado = 7;
        }
        if (mes.equals("AGOSTO")) {
            resultado = 8;
        }
        if (mes.equals("SEPTIEMBRE")) {
            resultado = 9;
        }
        if (mes.equals("OCTUBRE")) {
            resultado = 10;
        }
        if (mes.equals("NOVIEMBRE")) {
            resultado = 11;
        }
        if (mes.equals("DICIEMBRE")) {
            resultado = 12;
        }

        return resultado;
    }

}
