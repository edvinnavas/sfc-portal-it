package Bean.Usuario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Evento;
import Entidad.Usuario;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import ClientRestService.Cliente_Rest_Drive;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Usuario;

@Named(value = "UsuariosDialog")
@ViewScoped
public class UsuariosDialog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private Long id_usuario;
    private String usuario_dialog;
    private String nombre_completo;
    private String correo;
    private Date fecha_nacimiento;
    private String contrasena;
    private String recontrasena;
    private Long id_rol;
    private Integer activo;
    private Integer editable;
    private Integer eliminable;
    private String descripcion;

    private List<SelectItem> lst_rol;

    private String opcion;

    private Boolean txt_usuario;
    private Boolean txt_nombre_completo;
    private Boolean txt_correo;
    private Boolean cal_fecha_nacimiento;
    private Boolean pass_contrasena;
    private Boolean pass_recontrasena;
    private Boolean som_rol;
    private Boolean are_descripcion;

    private Boolean btn_aceptar;

    public UsuariosDialog() {
        this.lst_rol = new ArrayList<>();
    }

    public void mostrar_dialog_crear_usuario(Usuario usuario) {
        try {
            this.usuario = usuario;
            this.opcion = "CREAR";

            this.id_usuario = Long.valueOf(0);
            this.usuario_dialog = "";
            this.nombre_completo = "";
            this.correo = "";
            this.fecha_nacimiento = new Date();
            this.contrasena = "";
            this.recontrasena = "";
            this.id_rol = Long.valueOf(0);
            this.activo = 1;
            this.editable = 1;
            this.eliminable = 1;
            this.descripcion = "";

            this.lst_rol = new ArrayList<>();
            String cadenasql = "select r.id_rol, r.nombre from rol r order by r.nombre";
            Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_drive.drive(cadenasql);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] rol = lista_drive.get(i).split("♣");
                this.lst_rol.add(new SelectItem(Long.valueOf(rol[0]), rol[1]));
            }

            this.txt_usuario = false;
            this.txt_nombre_completo = false;
            this.txt_correo = false;
            this.cal_fecha_nacimiento = false;
            this.pass_contrasena = false;
            this.pass_recontrasena = false;
            this.som_rol = false;
            this.are_descripcion = false;
            this.btn_aceptar = false;

            PrimeFaces.current().executeScript("PF('dialogUsuariosVar').show();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_crear_usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void mostrar_dialog_modificar_usuario(Usuario usuario, Long id_usuario) {
        try {
            if (id_usuario != null) {
                this.usuario = usuario;
                this.id_usuario = id_usuario;
                this.opcion = "MODIFICAR";

                this.lst_rol = new ArrayList<>();
                String cadenasql = "select r.id_rol, r.nombre from rol r order by r.nombre";
                Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                String jsonString = cliente_rest_drive.drive(cadenasql);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] rol = lista_drive.get(i).split("♣");
                    this.lst_rol.add(new SelectItem(Long.valueOf(rol[0]), rol[1]));
                }

                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                jsonString = cliente_rest_usuario.get_usuario(this.id_usuario);
                listType = new TypeToken<ArrayList<Entidad.Usuario>>() {
                }.getType();
                List<Entidad.Usuario> lista_usuario = new Gson().fromJson(jsonString, listType);
                this.id_usuario = lista_usuario.get(0).getId_usuario();
                this.usuario_dialog = lista_usuario.get(0).getUsuario();
                this.nombre_completo = lista_usuario.get(0).getNombre_completo();
                this.correo = lista_usuario.get(0).getCorreo();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                this.fecha_nacimiento = dateFormat.parse(lista_usuario.get(0).getFecha_nacimiento().toString());
                this.contrasena = lista_usuario.get(0).getContrasena();
                this.recontrasena = lista_usuario.get(0).getContrasena();
                this.id_rol = lista_usuario.get(0).getId_rol();
                this.activo = lista_usuario.get(0).getActivo();
                this.editable = lista_usuario.get(0).getEditable();
                this.eliminable = lista_usuario.get(0).getEliminable();
                this.descripcion = lista_usuario.get(0).getDescripcion();

                if (this.editable == 1) {
                    this.txt_usuario = false;
                    this.txt_nombre_completo = false;
                    this.txt_correo = false;
                    this.cal_fecha_nacimiento = false;
                    this.som_rol = false;
                    this.are_descripcion = false;
                    this.btn_aceptar = false;
                } else {
                    this.txt_usuario = true;
                    this.txt_nombre_completo = true;
                    this.txt_correo = true;
                    this.cal_fecha_nacimiento = true;
                    this.som_rol = true;
                    this.are_descripcion = true;
                    this.btn_aceptar = true;
                }

                this.pass_contrasena = true;
                this.pass_recontrasena = true;

                PrimeFaces.current().executeScript("PF('dialogUsuariosVar').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un usuario del listado."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_modificar_usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void mostrar_dialog_eliminar_usuario(Usuario usuario, Long id_usuario) {
        try {
            if (id_usuario != null) {
                this.usuario = usuario;
                this.id_usuario = id_usuario;
                this.opcion = "ELIMINAR";

                this.lst_rol = new ArrayList<>();
                String cadenasql = "select r.id_rol, r.nombre from rol r order by r.nombre";
                Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                String jsonString = cliente_rest_drive.drive(cadenasql);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] rol = lista_drive.get(i).split("♣");
                    this.lst_rol.add(new SelectItem(Long.valueOf(rol[0]), rol[1]));
                }

                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                jsonString = cliente_rest_usuario.get_usuario(this.id_usuario);
                listType = new TypeToken<ArrayList<Entidad.Usuario>>() {
                }.getType();
                List<Entidad.Usuario> lista_usuario = new Gson().fromJson(jsonString, listType);
                this.id_usuario = lista_usuario.get(0).getId_usuario();
                this.usuario_dialog = lista_usuario.get(0).getUsuario();
                this.nombre_completo = lista_usuario.get(0).getNombre_completo();
                this.correo = lista_usuario.get(0).getCorreo();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                this.fecha_nacimiento = dateFormat.parse(lista_usuario.get(0).getFecha_nacimiento().toString());
                this.contrasena = "1";
                this.recontrasena = "2";
                this.id_rol = lista_usuario.get(0).getId_rol();
                this.activo = lista_usuario.get(0).getActivo();
                this.editable = lista_usuario.get(0).getEditable();
                this.eliminable = lista_usuario.get(0).getEliminable();
                this.descripcion = lista_usuario.get(0).getDescripcion();

                this.txt_usuario = true;
                this.txt_nombre_completo = true;
                this.txt_correo = true;
                this.cal_fecha_nacimiento = true;
                this.pass_contrasena = true;
                this.pass_recontrasena = true;
                this.som_rol = true;
                this.are_descripcion = true;

                if (this.eliminable == 1) {
                    this.btn_aceptar = false;
                } else {
                    this.btn_aceptar = true;
                }

                PrimeFaces.current().executeScript("PF('dialogUsuariosVar').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un usuario del listado."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_modificar_usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void aceptar() {
        try {
            switch (this.opcion) {
                case "CREAR": {
                    this.Crear_Usuario();
                    break;
                }
                case "MODIFICAR": {
                    this.Modificar_Usuario();
                    break;
                }
                case "ELIMINAR": {
                    this.Eliminar_Usuario();
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: aceptar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void Crear_Usuario() {
        try {
            if (this.contrasena.equals(this.recontrasena)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                List<Usuario> lista_usuario = new ArrayList<>();
                Usuario usuario = new Usuario(Long.valueOf(0), this.usuario_dialog, this.nombre_completo, this.correo, Long.valueOf(dateFormat.format(this.fecha_nacimiento)), this.contrasena, this.descripcion, 1, 1, 1, this.id_rol);
                lista_usuario.add(usuario);

                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                String resultado = cliente_rest_usuario.crear_usuario(lista_usuario);

                PrimeFaces.current().executeScript("PF('dialogUsuariosVar').hide();");
                PrimeFaces.current().executeScript("PF('varTblUsuarios').clearFilters();");

                // OBTENER FECHA ACTUAL.
                Date fecha_actual = new Date();
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                // REGISTRAR EVENTO.
                Evento evento = new Evento(Long.valueOf(6), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Crear usuario: " + new Gson().toJson(lista_usuario));
                List<Evento> lista_eventos = new ArrayList<>();
                lista_eventos.add(evento);
                Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
                cliente_rest_evento.crear_evento(lista_eventos);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "El campo contraseña y recontraseña deben se iguales."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Crear_Usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void Modificar_Usuario() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            List<Usuario> lista_usuario = new ArrayList<>();
            Usuario usuario = new Usuario(Long.valueOf(0), this.usuario_dialog, this.nombre_completo, this.correo, Long.valueOf(dateFormat.format(this.fecha_nacimiento)), this.contrasena, this.descripcion, 1, 1, 1, this.id_rol);
            lista_usuario.add(usuario);

            Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_usuario.modificar_usuario(this.id_usuario, lista_usuario);

            PrimeFaces.current().executeScript("PF('dialogUsuariosVar').hide();");
            PrimeFaces.current().executeScript("PF('varTblUsuarios').clearFilters();");

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(Long.valueOf(7), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Modificar usuario: " + new Gson().toJson(lista_usuario));
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Modificar_Usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void Eliminar_Usuario() {
        try {
            Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_usuario.eliminar_usuario(this.id_usuario);

            PrimeFaces.current().executeScript("PF('dialogUsuariosVar').hide();");
            PrimeFaces.current().executeScript("PF('varTblUsuarios').clearFilters();");

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(Long.valueOf(8), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Eliminar usuario: " + this.id_usuario);
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Modificar_Usuario ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario_dialog() {
        return usuario_dialog;
    }

    public void setUsuario_dialog(String usuario_dialog) {
        this.usuario_dialog = usuario_dialog;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRecontrasena() {
        return recontrasena;
    }

    public void setRecontrasena(String recontrasena) {
        this.recontrasena = recontrasena;
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }

    public Integer getEliminable() {
        return eliminable;
    }

    public void setEliminable(Integer eliminable) {
        this.eliminable = eliminable;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SelectItem> getLst_rol() {
        return lst_rol;
    }

    public void setLst_rol(List<SelectItem> lst_rol) {
        this.lst_rol = lst_rol;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public Boolean getTxt_usuario() {
        return txt_usuario;
    }

    public void setTxt_usuario(Boolean txt_usuario) {
        this.txt_usuario = txt_usuario;
    }

    public Boolean getTxt_nombre_completo() {
        return txt_nombre_completo;
    }

    public void setTxt_nombre_completo(Boolean txt_nombre_completo) {
        this.txt_nombre_completo = txt_nombre_completo;
    }

    public Boolean getTxt_correo() {
        return txt_correo;
    }

    public void setTxt_correo(Boolean txt_correo) {
        this.txt_correo = txt_correo;
    }

    public Boolean getCal_fecha_nacimiento() {
        return cal_fecha_nacimiento;
    }

    public void setCal_fecha_nacimiento(Boolean cal_fecha_nacimiento) {
        this.cal_fecha_nacimiento = cal_fecha_nacimiento;
    }

    public Boolean getPass_contrasena() {
        return pass_contrasena;
    }

    public void setPass_contrasena(Boolean pass_contrasena) {
        this.pass_contrasena = pass_contrasena;
    }

    public Boolean getPass_recontrasena() {
        return pass_recontrasena;
    }

    public void setPass_recontrasena(Boolean pass_recontrasena) {
        this.pass_recontrasena = pass_recontrasena;
    }

    public Boolean getSom_rol() {
        return som_rol;
    }

    public void setSom_rol(Boolean som_rol) {
        this.som_rol = som_rol;
    }

    public Boolean getAre_descripcion() {
        return are_descripcion;
    }

    public void setAre_descripcion(Boolean are_descripcion) {
        this.are_descripcion = are_descripcion;
    }

    public Boolean getBtn_aceptar() {
        return btn_aceptar;
    }

    public void setBtn_aceptar(Boolean btn_aceptar) {
        this.btn_aceptar = btn_aceptar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
