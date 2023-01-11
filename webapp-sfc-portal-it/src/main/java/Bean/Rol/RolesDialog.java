package Bean.Rol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Entidad.Evento;
import Entidad.Rol;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;
import ClientRestService.Cliente_Rest_Drive;
import ClientRestService.Cliente_Rest_Menu;
import ClientRestService.Cliente_Rest_Rol;
import Entidad.Rol_Menu;
import Entidad.Usuario;
import java.text.SimpleDateFormat;
import java.util.Date;
import ClientRestService.Cliente_Rest_Evento;

@Named(value = "RolesDialog")
@ViewScoped
public class RolesDialog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private Long id_rol;
    private String nombre;
    private Integer activo;
    private Integer editable;
    private Integer eliminable;
    private String descripcion;

    private DualListModel<String> menus;

    private String opcion;

    private Boolean txt_nombre;
    private Boolean are_descripcion;
    private Boolean pck_list_menu;
    private Boolean btn_aceptar;

    public RolesDialog() {
        List<String> menusDisponibles = new ArrayList<>();
        List<String> menusAplicados = new ArrayList<>();

        this.menus = new DualListModel(menusDisponibles, menusAplicados);
    }

    public void mostrar_dialog_crear_rol(Usuario usuario) {
        try {
            this.usuario = usuario;
            this.opcion = "CREAR";

            this.id_rol = Long.valueOf(0);
            this.nombre = "";
            this.activo = 1;
            this.editable = 1;
            this.eliminable = 1;
            this.descripcion = "";

            List<String> menusDisponibles = new ArrayList<>();
            String cadenasql = "select m.nombre from menu m order by m.nombre";
            Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_drive.drive(cadenasql);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> lista_drive = new Gson().fromJson(jsonString, listType);
            for (Integer i = 1; i < lista_drive.size(); i++) {
                String[] menu = lista_drive.get(i).split("♣");
                menusDisponibles.add(menu[0]);
            }

            List<String> menusAplicados = new ArrayList<>();

            this.menus = new DualListModel(menusDisponibles, menusAplicados);

            this.txt_nombre = false;
            this.are_descripcion = false;
            this.pck_list_menu = false;

            PrimeFaces.current().executeScript("PF('dialogRolesVar').show();");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_crear_rol ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void mostrar_dialog_modificar_rol(Usuario usuario, Long id_rol) {
        try {
            if (id_rol != null) {
                this.usuario = usuario;
                this.id_rol = id_rol;
                this.opcion = "MODIFICAR";

                Cliente_Rest_Rol cliente_rest_rol = new Cliente_Rest_Rol("UserTerraRest", "R3st-T3rR@");
                String jsonString = cliente_rest_rol.get_rol(this.id_rol);
                Type listType = new TypeToken<ArrayList<Entidad.Rol>>() {
                }.getType();
                List<Entidad.Rol> lista_rol = new Gson().fromJson(jsonString, listType);
                this.id_rol = lista_rol.get(0).getId_rol();
                this.nombre = lista_rol.get(0).getNombre();
                this.editable = lista_rol.get(0).getEditable();
                this.activo = lista_rol.get(0).getActivo();
                this.eliminable = lista_rol.get(0).getEliminable();
                this.descripcion = lista_rol.get(0).getDescripcion();

                List<String> menusAplicados = new ArrayList<>();
                for (Integer i = 0; i < lista_rol.get(0).getLst_menu().size(); i++) {
                    menusAplicados.add(lista_rol.get(0).getLst_menu().get(i).getNombre_menu());
                }

                String cadenasql = "select m.nombre from menu m where m.id_menu not in (select rm.id_menu from rol_menu rm where rm.id_rol=" + this.id_rol + ")";
                List<String> menusDisponibles = new ArrayList<>();
                Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                jsonString = cliente_rest_drive.drive(cadenasql);
                listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] menu = lista_drive.get(i).split("♣");
                    menusDisponibles.add(menu[0]);
                }
                this.menus = new DualListModel(menusDisponibles, menusAplicados);

                if (this.editable == 1) {
                    this.txt_nombre = false;
                    this.are_descripcion = false;
                    this.pck_list_menu = false;
                    this.btn_aceptar = false;
                } else {
                    this.txt_nombre = true;
                    this.are_descripcion = true;
                    this.pck_list_menu = true;
                    this.btn_aceptar = true;
                }

                PrimeFaces.current().executeScript("PF('dialogRolesVar').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un rol del listado."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_modificar_rol ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void mostrar_dialog_eliminar_rol(Usuario usuario, Long id_rol) {
        try {
            if (id_rol != null) {
                this.usuario = usuario;
                this.id_rol = id_rol;
                this.opcion = "ELIMINAR";

                Cliente_Rest_Rol cliente_rest_rol = new Cliente_Rest_Rol("UserTerraRest", "R3st-T3rR@");
                String jsonString = cliente_rest_rol.get_rol(this.id_rol);
                Type listType = new TypeToken<ArrayList<Entidad.Rol>>() {
                }.getType();
                List<Entidad.Rol> lista_rol = new Gson().fromJson(jsonString, listType);
                this.id_rol = lista_rol.get(0).getId_rol();
                this.nombre = lista_rol.get(0).getNombre();
                this.activo = lista_rol.get(0).getActivo();
                this.eliminable = lista_rol.get(0).getEliminable();
                this.descripcion = lista_rol.get(0).getDescripcion();

                List<String> menusAplicados = new ArrayList<>();
                for (Integer i = 0; i < lista_rol.get(0).getLst_menu().size(); i++) {
                    menusAplicados.add(lista_rol.get(0).getLst_menu().get(i).getNombre_menu());
                }

                String cadenasql = "select m.nombre from menu m where m.id_menu not in (select rm.id_menu from rol_menu rm where rm.id_rol=" + this.id_rol + ")";
                List<String> menusDisponibles = new ArrayList<>();
                Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                jsonString = cliente_rest_drive.drive(cadenasql);
                listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] menu = lista_drive.get(i).split("♣");
                    menusDisponibles.add(menu[0]);
                }
                this.menus = new DualListModel(menusDisponibles, menusAplicados);

                this.txt_nombre = true;
                this.are_descripcion = true;
                this.pck_list_menu = true;

                if (this.eliminable == 1) {
                    this.btn_aceptar = false;
                } else {
                    this.btn_aceptar = true;
                }

                PrimeFaces.current().executeScript("PF('dialogRolesVar').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje del sistema...", "Debe seleccionar un rol del listado."));
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: mostrar_dialog_modificar_rol ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void aceptar() {
        try {
            switch (this.opcion) {
                case "CREAR": {
                    this.Crear_Rol();
                    break;
                }
                case "MODIFICAR": {
                    this.Modificar_Rol();
                    break;
                }
                case "ELIMINAR": {
                    this.Eliminar_Rol();
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: aceptar ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void Crear_Rol() {
        try {
            Cliente_Rest_Menu cliente_rest_menu = new Cliente_Rest_Menu("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_menu.get_menus();
            Type listType = new TypeToken<ArrayList<Entidad.Menu>>() {
            }.getType();
            List<Entidad.Menu> lista_menu = new Gson().fromJson(jsonString, listType);
            List<Rol_Menu> lst_menu = new ArrayList<>();
            for (Integer i = 0; i < this.menus.getTarget().size(); i++) {
                for (Integer j = 0; j < lista_menu.size(); j++) {
                    if (this.menus.getTarget().get(i).equals(lista_menu.get(j).getNombre())) {
                        Rol_Menu rol_menu = new Rol_Menu(Long.valueOf(0), lista_menu.get(j).getId_menu(), this.menus.getTarget().get(i));
                        lst_menu.add(rol_menu);
                    }
                }
            }

            List<Rol> lista_rol = new ArrayList<>();
            Rol rol = new Rol(Long.valueOf(0), this.nombre, 1, 1, 1, this.descripcion, lst_menu);
            lista_rol.add(rol);

            Cliente_Rest_Rol cliente_rest_rol = new Cliente_Rest_Rol("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_rol.crear_rol(lista_rol);

            PrimeFaces.current().executeScript("PF('dialogRolesVar').hide();");
            PrimeFaces.current().executeScript("PF('varTblRoles').clearFilters();");

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(Long.valueOf(3), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Crear rol: " + new Gson().toJson(lista_rol));
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Crear_Rol ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void Modificar_Rol() {
        try {
            Cliente_Rest_Menu cliente_rest_menu = new Cliente_Rest_Menu("UserTerraRest", "R3st-T3rR@");
            String jsonString = cliente_rest_menu.get_menus();
            Type listType = new TypeToken<ArrayList<Entidad.Menu>>() {
            }.getType();
            List<Entidad.Menu> lista_menu = new Gson().fromJson(jsonString, listType);
            List<Rol_Menu> lst_menu = new ArrayList<>();
            for (Integer i = 0; i < this.menus.getTarget().size(); i++) {
                for (Integer j = 0; j < lista_menu.size(); j++) {
                    if (this.menus.getTarget().get(i).equals(lista_menu.get(j).getNombre())) {
                        Rol_Menu rol_menu = new Rol_Menu(this.id_rol, lista_menu.get(j).getId_menu(), this.menus.getTarget().get(i));
                        lst_menu.add(rol_menu);
                    }
                }
            }

            List<Rol> lista_rol = new ArrayList<>();
            Rol rol = new Rol(this.id_rol, this.nombre, 1, 1, 1, this.descripcion, lst_menu);
            lista_rol.add(rol);

            Cliente_Rest_Rol cliente_rest_rol = new Cliente_Rest_Rol("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_rol.modificar_rol(this.id_rol, lista_rol);

            PrimeFaces.current().executeScript("PF('dialogRolesVar').hide();");
            PrimeFaces.current().executeScript("PF('varTblRoles').clearFilters();");

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(new Long(4), this.usuario.getId_usuario(), Long.parseLong(dataFormat.format(fecha_actual)), "Modificar rol: " + new Gson().toJson(lista_rol));
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Modificar_Rol ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public void Eliminar_Rol() {
        try {
            Cliente_Rest_Rol cliente_rest_rol = new Cliente_Rest_Rol("UserTerraRest", "R3st-T3rR@");
            String resultado = cliente_rest_rol.eliminar_rol(this.id_rol);

            PrimeFaces.current().executeScript("PF('dialogRolesVar').hide();");
            PrimeFaces.current().executeScript("PF('varTblRoles').clearFilters();");

            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(Long.valueOf(5), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Eliminar rol: " + this.id_rol);
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje del sistema...", resultado));
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: Modificar_Rol ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public Long getId_rol() {
        return id_rol;
    }

    public void setId_rol(Long id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
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

    public DualListModel<String> getMenus() {
        return menus;
    }

    public void setMenus(DualListModel<String> menus) {
        this.menus = menus;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public Boolean getTxt_nombre() {
        return txt_nombre;
    }

    public void setTxt_nombre(Boolean txt_nombre) {
        this.txt_nombre = txt_nombre;
    }

    public Boolean getAre_descripcion() {
        return are_descripcion;
    }

    public void setAre_descripcion(Boolean are_descripcion) {
        this.are_descripcion = are_descripcion;
    }

    public Boolean getPck_list_menu() {
        return pck_list_menu;
    }

    public void setPck_list_menu(Boolean pck_list_menu) {
        this.pck_list_menu = pck_list_menu;
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

    public Integer getEditable() {
        return editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }

}
