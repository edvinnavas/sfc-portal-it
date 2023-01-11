package Bean;

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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import ClientRestService.Cliente_Rest_Drive;
import ClientRestService.Cliente_Rest_Evento;
import ClientRestService.Cliente_Rest_Usuario;

@Named(value = "MenuApp")
@ViewScoped
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pagina_seleccionada;
    private String ambiente;
    private Usuario usuario;

    /* OPCIONES DE MENU */
    private Boolean roles;
    private Boolean usuarios;
    private Boolean cambiar_contrasena;
    private Boolean fel_configuracion;
    private Boolean fel_carga_documentos_dte;
    private Boolean ecsa_cr_documento_fiscales;
    private Boolean rba_estaciones_servicio;
    private Boolean svf_rpt_diario_efectivo_uno;
    private Boolean svf_rpt_mensual_efectivo_uno;
    private Boolean svf_rpt_mensual_otros_medios_uno;
    private Boolean svf_rpt_mensual_medios_electronicos_uno;
    private Boolean svf_rpt_diario_efectivo_ecsa;
    private Boolean svf_rpt_mensual_efectivo_ecsa;
    private Boolean svf_rpt_mensual_otros_medios_ecsa;
    private Boolean svf_rpt_mensual_medios_electronicos_ecsa;
    private Boolean cface_uno_guatemala;
    private Boolean cnce_uno_guatemala;
    private Boolean cnde_uno_guatemala;
    private Boolean cface_unopetrol_guatemala;
    private Boolean cnce_unopetrol_guatemala;
    private Boolean cnde_unopetrol_guatemala;
    private Boolean fel_carga_documentos_elgua;
    private Boolean fel_carga_documentos_tresa;
    private Boolean fel_carga_documentos_comercia;
    private Boolean fel_carga_documentos_mersa;

    /* MENUS - MODULOS DE LA APLICACION */
    private Boolean menu_seguridad;
    private Boolean menu_cr_gosocket;
    private Boolean menu_hn_rba;
    private Boolean menu_sv_fiscalia_unopetrol;
    private Boolean menu_sv_fiscalia_ecsa;
    private Boolean menu_gt_uno_cface;
    private Boolean menu_gt_unopetrol_cface;

    public Menu() {
        this.pagina_seleccionada = "inicial";

        this.roles = true;
        this.usuarios = true;
        this.cambiar_contrasena = true;
        this.fel_configuracion = true;
        this.fel_carga_documentos_dte = true;
        this.ecsa_cr_documento_fiscales = true;
        this.rba_estaciones_servicio = true;
        this.svf_rpt_diario_efectivo_uno = true;
        this.svf_rpt_mensual_efectivo_uno = true;
        this.svf_rpt_mensual_otros_medios_uno = true;
        this.svf_rpt_mensual_medios_electronicos_uno = true;
        this.svf_rpt_diario_efectivo_ecsa = true;
        this.svf_rpt_mensual_efectivo_ecsa = true;
        this.svf_rpt_mensual_otros_medios_ecsa = true;
        this.svf_rpt_mensual_medios_electronicos_ecsa = true;
        this.cface_uno_guatemala = true;
        this.cnce_uno_guatemala = true;
        this.cnde_uno_guatemala = true;
        this.cface_unopetrol_guatemala = true;
        this.cnce_unopetrol_guatemala = true;
        this.cnde_unopetrol_guatemala = true;
        this.fel_carga_documentos_elgua = true;
        this.fel_carga_documentos_tresa = true;
        this.fel_carga_documentos_comercia = true;
        this.fel_carga_documentos_mersa = true;

        this.menu_seguridad = true;
        this.menu_cr_gosocket = false;
        this.menu_hn_rba = false;
        this.menu_sv_fiscalia_unopetrol = false;
        this.menu_sv_fiscalia_ecsa = false;
        this.menu_gt_uno_cface = false;
        this.menu_gt_unopetrol_cface = false;
    }

    public void onload(String ambiente, String nombre_usuario) {
        try {
            if (this.usuario == null) {
                this.ambiente = ambiente;
                Long id_usuario = Long.valueOf("0");
                String cadenasql = "select u.id_usuario from usuario u where u.usuario='" + nombre_usuario + "'";
                Cliente_Rest_Drive cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                String jsonString = cliente_rest_drive.drive(cadenasql);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] usuario_rest = lista_drive.get(i).split("♣");
                    id_usuario = Long.valueOf(usuario_rest[0]);
                }

                Cliente_Rest_Usuario cliente_rest_usuario = new Cliente_Rest_Usuario("UserTerraRest", "R3st-T3rR@");
                jsonString = cliente_rest_usuario.get_usuario(id_usuario);
                listType = new TypeToken<ArrayList<Entidad.Usuario>>() {
                }.getType();
                List<Entidad.Usuario> lista_usuario = new Gson().fromJson(jsonString, listType);
                this.usuario = lista_usuario.get(0);

                this.roles = false;
                this.usuarios = false;
                this.cambiar_contrasena = false;
                this.fel_configuracion = false;
                this.fel_carga_documentos_dte = false;
                this.ecsa_cr_documento_fiscales = false;
                this.rba_estaciones_servicio = false;
                this.svf_rpt_diario_efectivo_uno = false;
                this.svf_rpt_mensual_efectivo_uno = false;
                this.svf_rpt_mensual_otros_medios_uno = false;
                this.svf_rpt_mensual_medios_electronicos_uno = false;
                this.svf_rpt_diario_efectivo_ecsa = false;
                this.svf_rpt_mensual_efectivo_ecsa = false;
                this.svf_rpt_mensual_otros_medios_ecsa = false;
                this.svf_rpt_mensual_medios_electronicos_ecsa = false;
                this.cface_uno_guatemala = false;
                this.cnce_uno_guatemala = false;
                this.cnde_uno_guatemala = false;
                this.cface_unopetrol_guatemala = false;
                this.cnce_unopetrol_guatemala = false;
                this.cnde_unopetrol_guatemala = false;
                this.fel_carga_documentos_elgua = false;
                this.fel_carga_documentos_tresa = false;
                this.fel_carga_documentos_comercia = false;
                this.fel_carga_documentos_mersa = false;

                cadenasql = "select "
                        + "u.id_usuario, m.nombre "
                        + "from "
                        + "usuario u "
                        + "left join rol_menu rm on (u.id_rol=rm.id_rol) "
                        + "left join menu m on (rm.id_menu=m.id_menu) "
                        + "where "
                        + "u.id_usuario=" + usuario.getId_usuario();
                cliente_rest_drive = new Cliente_Rest_Drive("UserTerraRest", "R3st-T3rR@");
                jsonString = cliente_rest_drive.drive(cadenasql);
                listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                lista_drive = new Gson().fromJson(jsonString, listType);
                for (Integer i = 1; i < lista_drive.size(); i++) {
                    String[] menu_rest = lista_drive.get(i).split("♣");
                    if (menu_rest[1].equals("roles")) {
                        this.roles = true;
                    }
                    if (menu_rest[1].equals("usuarios")) {
                        this.usuarios = true;
                    }
                    if (menu_rest[1].equals("cambiar_contrasena")) {
                        this.cambiar_contrasena = true;
                    }
                    if (menu_rest[1].equals("fel_configuracion")) {
                        this.fel_configuracion = true;
                    }
                    if (menu_rest[1].equals("fel_carga_documentos_dte")) {
                        this.fel_carga_documentos_dte = true;
                    }
                    if (menu_rest[1].equals("ecsa_cr_documento_fiscales")) {
                        this.ecsa_cr_documento_fiscales = true;
                    }
                    if (menu_rest[1].equals("rba_estaciones_servicio")) {
                        this.rba_estaciones_servicio = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_diario_efectivo_uno")) {
                        this.svf_rpt_diario_efectivo_uno = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_mensual_efectivo_uno")) {
                        this.svf_rpt_mensual_efectivo_uno = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_mensual_otros_medios_uno")) {
                        this.svf_rpt_mensual_otros_medios_uno = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_mensual_medios_electronicos_uno")) {
                        this.svf_rpt_mensual_medios_electronicos_uno = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_diario_efectivo_ecsa")) {
                        this.svf_rpt_diario_efectivo_ecsa = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_mensual_efectivo_ecsa")) {
                        this.svf_rpt_mensual_efectivo_ecsa = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_mensual_otros_medios_ecsa")) {
                        this.svf_rpt_mensual_otros_medios_ecsa = true;
                    }
                    if (menu_rest[1].equals("svf_rpt_mensual_medios_electronicos_ecsa")) {
                        this.svf_rpt_mensual_medios_electronicos_ecsa = true;
                    }
                    if (menu_rest[1].equals("cface_uno_guatemala")) {
                        this.cface_uno_guatemala = true;
                    }
                    if (menu_rest[1].equals("cnce_uno_guatemala")) {
                        this.cnce_uno_guatemala = true;
                    }
                    if (menu_rest[1].equals("cnde_uno_guatemala")) {
                        this.cnde_uno_guatemala = true;
                    }
                    if (menu_rest[1].equals("cface_unopetrol_guatemala")) {
                        this.cface_unopetrol_guatemala = true;
                    }
                    if (menu_rest[1].equals("cnce_unopetrol_guatemala")) {
                        this.cnce_unopetrol_guatemala = true;
                    }
                    if (menu_rest[1].equals("cnde_unopetrol_guatemala")) {
                        this.cnde_unopetrol_guatemala = true;
                    }
                    if (menu_rest[1].equals("fel_carga_documentos_elgua")) {
                        this.fel_carga_documentos_elgua = true;
                    }
                    if (menu_rest[1].equals("fel_carga_documentos_tresa")) {
                        this.fel_carga_documentos_tresa = true;
                    }
                    if (menu_rest[1].equals("fel_carga_documentos_comercia")) {
                        this.fel_carga_documentos_comercia = true;
                    }
                    if (menu_rest[1].equals("fel_carga_documentos_mersa")) {
                        this.fel_carga_documentos_mersa = true;
                    }
                }

                if (!this.ecsa_cr_documento_fiscales) {
                    this.menu_cr_gosocket = false;
                } else {
                    this.menu_cr_gosocket = true;
                }
                if (!this.rba_estaciones_servicio) {
                    this.menu_hn_rba = false;
                } else {
                    this.menu_hn_rba = true;
                }
                if (!this.svf_rpt_diario_efectivo_uno && !this.svf_rpt_mensual_efectivo_uno && !this.svf_rpt_mensual_otros_medios_uno && !this.svf_rpt_mensual_medios_electronicos_uno) {
                    this.menu_sv_fiscalia_unopetrol = false;
                } else {
                    this.menu_sv_fiscalia_unopetrol = true;
                }
                if (!this.svf_rpt_diario_efectivo_ecsa && !this.svf_rpt_mensual_efectivo_ecsa && !this.svf_rpt_mensual_otros_medios_ecsa && !this.svf_rpt_mensual_medios_electronicos_ecsa) {
                    this.menu_sv_fiscalia_ecsa = false;
                } else {
                    this.menu_sv_fiscalia_ecsa = true;
                }
                if (!this.cface_uno_guatemala && !this.cnce_uno_guatemala && !this.cnde_uno_guatemala) {
                    this.menu_gt_uno_cface = false;
                } else {
                    this.menu_gt_uno_cface = true;
                }
                if (!this.cface_unopetrol_guatemala && !this.cnce_unopetrol_guatemala && !this.cnde_unopetrol_guatemala) {
                    this.menu_gt_unopetrol_cface = false;
                } else {
                    this.menu_gt_unopetrol_cface = true;
                }
            }
        } catch (Exception ex) {
            try {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: onload ERROR: " + ex.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (Exception ex1) {
                System.out.println("CLASE: " + this.getClass().getName() + " METODO: onload Exception ex1 ERROR: " + ex1.toString());
            }
        }
    }

    public void salir() {
        try {
            // OBTENER FECHA ACTUAL.
            Date fecha_actual = new Date();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            // REGISTRAR EVENTO.
            Evento evento = new Evento(Long.valueOf(2), this.usuario.getId_usuario(), Long.valueOf(dataFormat.format(fecha_actual)), "Salir del sistema.");
            List<Evento> lista_eventos = new ArrayList<>();
            lista_eventos.add(evento);
            Cliente_Rest_Evento cliente_rest_evento = new Cliente_Rest_Evento("UserTerraRest", "R3st-T3rR@");
            cliente_rest_evento.crear_evento(lista_eventos);

            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("faces/index.xhtml");
        } catch (Exception ex) {
            System.out.println("CLASE: " + this.getClass().getName() + " METODO: salir ERROR: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje del sistema...", ex.toString()));
        }
    }

    public String getPagina_seleccionada() {
        return pagina_seleccionada + ".xhtml";
    }

    public void setPagina_seleccionada(String pagina_seleccionada) {
        this.pagina_seleccionada = pagina_seleccionada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getRoles() {
        return roles;
    }

    public void setRoles(Boolean roles) {
        this.roles = roles;
    }

    public Boolean getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Boolean usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getCambiar_contrasena() {
        return cambiar_contrasena;
    }

    public void setCambiar_contrasena(Boolean cambiar_contrasena) {
        this.cambiar_contrasena = cambiar_contrasena;
    }

    public Boolean getFel_configuracion() {
        return fel_configuracion;
    }

    public void setFel_configuracion(Boolean fel_configuracion) {
        this.fel_configuracion = fel_configuracion;
    }

    public Boolean getFel_carga_documentos_dte() {
        return fel_carga_documentos_dte;
    }

    public void setFel_carga_documentos_dte(Boolean fel_carga_documentos_dte) {
        this.fel_carga_documentos_dte = fel_carga_documentos_dte;
    }

    public Boolean getCface_uno_guatemala() {
        return cface_uno_guatemala;
    }

    public void setCface_uno_guatemala(Boolean cface_uno_guatemala) {
        this.cface_uno_guatemala = cface_uno_guatemala;
    }

    public Boolean getCface_unopetrol_guatemala() {
        return cface_unopetrol_guatemala;
    }

    public void setCface_unopetrol_guatemala(Boolean cface_unopetrol_guatemala) {
        this.cface_unopetrol_guatemala = cface_unopetrol_guatemala;
    }

    public Boolean getEcsa_cr_documento_fiscales() {
        return ecsa_cr_documento_fiscales;
    }

    public void setEcsa_cr_documento_fiscales(Boolean ecsa_cr_documento_fiscales) {
        this.ecsa_cr_documento_fiscales = ecsa_cr_documento_fiscales;
    }

    public Boolean getRba_estaciones_servicio() {
        return rba_estaciones_servicio;
    }

    public void setRba_estaciones_servicio(Boolean rba_estaciones_servicio) {
        this.rba_estaciones_servicio = rba_estaciones_servicio;
    }

    public Boolean getSvf_rpt_diario_efectivo_uno() {
        return svf_rpt_diario_efectivo_uno;
    }

    public void setSvf_rpt_diario_efectivo_uno(Boolean svf_rpt_diario_efectivo_uno) {
        this.svf_rpt_diario_efectivo_uno = svf_rpt_diario_efectivo_uno;
    }

    public Boolean getSvf_rpt_mensual_efectivo_uno() {
        return svf_rpt_mensual_efectivo_uno;
    }

    public void setSvf_rpt_mensual_efectivo_uno(Boolean svf_rpt_mensual_efectivo_uno) {
        this.svf_rpt_mensual_efectivo_uno = svf_rpt_mensual_efectivo_uno;
    }

    public Boolean getSvf_rpt_mensual_otros_medios_uno() {
        return svf_rpt_mensual_otros_medios_uno;
    }

    public void setSvf_rpt_mensual_otros_medios_uno(Boolean svf_rpt_mensual_otros_medios_uno) {
        this.svf_rpt_mensual_otros_medios_uno = svf_rpt_mensual_otros_medios_uno;
    }

    public Boolean getSvf_rpt_mensual_medios_electronicos_uno() {
        return svf_rpt_mensual_medios_electronicos_uno;
    }

    public void setSvf_rpt_mensual_medios_electronicos_uno(Boolean svf_rpt_mensual_medios_electronicos_uno) {
        this.svf_rpt_mensual_medios_electronicos_uno = svf_rpt_mensual_medios_electronicos_uno;
    }

    public Boolean getSvf_rpt_diario_efectivo_ecsa() {
        return svf_rpt_diario_efectivo_ecsa;
    }

    public void setSvf_rpt_diario_efectivo_ecsa(Boolean svf_rpt_diario_efectivo_ecsa) {
        this.svf_rpt_diario_efectivo_ecsa = svf_rpt_diario_efectivo_ecsa;
    }

    public Boolean getSvf_rpt_mensual_efectivo_ecsa() {
        return svf_rpt_mensual_efectivo_ecsa;
    }

    public void setSvf_rpt_mensual_efectivo_ecsa(Boolean svf_rpt_mensual_efectivo_ecsa) {
        this.svf_rpt_mensual_efectivo_ecsa = svf_rpt_mensual_efectivo_ecsa;
    }

    public Boolean getSvf_rpt_mensual_otros_medios_ecsa() {
        return svf_rpt_mensual_otros_medios_ecsa;
    }

    public void setSvf_rpt_mensual_otros_medios_ecsa(Boolean svf_rpt_mensual_otros_medios_ecsa) {
        this.svf_rpt_mensual_otros_medios_ecsa = svf_rpt_mensual_otros_medios_ecsa;
    }

    public Boolean getSvf_rpt_mensual_medios_electronicos_ecsa() {
        return svf_rpt_mensual_medios_electronicos_ecsa;
    }

    public void setSvf_rpt_mensual_medios_electronicos_ecsa(Boolean svf_rpt_mensual_medios_electronicos_ecsa) {
        this.svf_rpt_mensual_medios_electronicos_ecsa = svf_rpt_mensual_medios_electronicos_ecsa;
    }

    public Boolean getMenu_seguridad() {
        return menu_seguridad;
    }

    public void setMenu_seguridad(Boolean menu_seguridad) {
        this.menu_seguridad = menu_seguridad;
    }

    public Boolean getMenu_cr_gosocket() {
        return menu_cr_gosocket;
    }

    public void setMenu_cr_gosocket(Boolean menu_cr_gosocket) {
        this.menu_cr_gosocket = menu_cr_gosocket;
    }

    public Boolean getMenu_gt_uno_cface() {
        return menu_gt_uno_cface;
    }

    public void setMenu_gt_uno_cface(Boolean menu_gt_uno_cface) {
        this.menu_gt_uno_cface = menu_gt_uno_cface;
    }

    public Boolean getMenu_gt_unopetrol_cface() {
        return menu_gt_unopetrol_cface;
    }

    public void setMenu_gt_unopetrol_cface(Boolean menu_gt_unopetrol_cface) {
        this.menu_gt_unopetrol_cface = menu_gt_unopetrol_cface;
    }

    public Boolean getMenu_hn_rba() {
        return menu_hn_rba;
    }

    public void setMenu_hn_rba(Boolean menu_hn_rba) {
        this.menu_hn_rba = menu_hn_rba;
    }

    public Boolean getMenu_sv_fiscalia_unopetrol() {
        return menu_sv_fiscalia_unopetrol;
    }

    public void setMenu_sv_fiscalia_unopetrol(Boolean menu_sv_fiscalia_unopetrol) {
        this.menu_sv_fiscalia_unopetrol = menu_sv_fiscalia_unopetrol;
    }

    public Boolean getMenu_sv_fiscalia_ecsa() {
        return menu_sv_fiscalia_ecsa;
    }

    public void setMenu_sv_fiscalia_ecsa(Boolean menu_sv_fiscalia_ecsa) {
        this.menu_sv_fiscalia_ecsa = menu_sv_fiscalia_ecsa;
    }

    public Boolean getCnce_uno_guatemala() {
        return cnce_uno_guatemala;
    }

    public void setCnce_uno_guatemala(Boolean cnce_uno_guatemala) {
        this.cnce_uno_guatemala = cnce_uno_guatemala;
    }

    public Boolean getCnde_uno_guatemala() {
        return cnde_uno_guatemala;
    }

    public void setCnde_uno_guatemala(Boolean cnde_uno_guatemala) {
        this.cnde_uno_guatemala = cnde_uno_guatemala;
    }

    public Boolean getCnce_unopetrol_guatemala() {
        return cnce_unopetrol_guatemala;
    }

    public void setCnce_unopetrol_guatemala(Boolean cnce_unopetrol_guatemala) {
        this.cnce_unopetrol_guatemala = cnce_unopetrol_guatemala;
    }

    public Boolean getCnde_unopetrol_guatemala() {
        return cnde_unopetrol_guatemala;
    }

    public void setCnde_unopetrol_guatemala(Boolean cnde_unopetrol_guatemala) {
        this.cnde_unopetrol_guatemala = cnde_unopetrol_guatemala;
    }

    public Boolean getFel_carga_documentos_elgua() {
        return fel_carga_documentos_elgua;
    }

    public void setFel_carga_documentos_elgua(Boolean fel_carga_documentos_elgua) {
        this.fel_carga_documentos_elgua = fel_carga_documentos_elgua;
    }

    public Boolean getFel_carga_documentos_tresa() {
        return fel_carga_documentos_tresa;
    }

    public void setFel_carga_documentos_tresa(Boolean fel_carga_documentos_tresa) {
        this.fel_carga_documentos_tresa = fel_carga_documentos_tresa;
    }

    public Boolean getFel_carga_documentos_comercia() {
        return fel_carga_documentos_comercia;
    }

    public void setFel_carga_documentos_comercia(Boolean fel_carga_documentos_comercia) {
        this.fel_carga_documentos_comercia = fel_carga_documentos_comercia;
    }

    public Boolean getFel_carga_documentos_mersa() {
        return fel_carga_documentos_mersa;
    }

    public void setFel_carga_documentos_mersa(Boolean fel_carga_documentos_mersa) {
        this.fel_carga_documentos_mersa = fel_carga_documentos_mersa;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

}
