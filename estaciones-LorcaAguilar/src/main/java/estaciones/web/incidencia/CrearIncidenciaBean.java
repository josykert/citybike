package estaciones.web.incidencia;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dto.BicicletaDTO;

import org.primefaces.PrimeFaces;

import estaciones.servicio.ServicioEstaciones;
import estaciones.servicio.ServicioIncidencias;
import estaciones.web.locale.ActiveLocale;
import repositorio.EntidadNoEncontrada;
import repositorio.IncidenciasException;
import repositorio.RepositorioException;

@Named
@SessionScoped
public class CrearIncidenciaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idBici;
	private List<BicicletaDTO> bicicletas;
	private String descripcion;
	private String message;
	private String header;
	private String clase;

	private ServicioIncidencias servicio = new ServicioIncidencias();
	private ServicioEstaciones servicioEstaciones = new ServicioEstaciones();
	
	@Inject
    private ActiveLocale activeLocale;

    private Locale currentLocale;
    private ResourceBundle resourceBundle;

	public CrearIncidenciaBean() {
		actualizar();
	}

	public boolean crearIncidencia() throws RepositorioException, IncidenciasException, EntidadNoEncontrada {
		currentLocale = activeLocale.getActual();
        try {
            resourceBundle = ResourceBundle.getBundle("i18n.text", currentLocale);
        } catch (MissingResourceException e) {
            System.out.println("No se pudo cargar el archivo de propiedades.");
            e.printStackTrace();
        }
		String id = null;
		
		id = servicio.crearIncidencia(idBici, descripcion);
		
		if (id != null) {
			header = resourceBundle.getString("creada");
			message = resourceBundle.getString("creadaCorrecto");
			setClase("ui-button-info");
			PrimeFaces.current().ajax().update("dialogoCrearIncidencia");
			actualizar();
			return true;
		} else {
			header = "ERROR";
			message = resourceBundle.getString("creadaMal");
			setClase("ui-button-danger");
			PrimeFaces.current().ajax().update("dialogoCrearIncidencia");
			actualizar();
			return false;
		}
	}

	private void actualizar() {
		try {
			bicicletas = servicioEstaciones.getBicisDTO();
		} catch (RepositorioException e) {
			e.printStackTrace();
		}
	}
	
	public List<BicicletaDTO> getBicis() {
		return bicicletas;
	}

	public void setBicis(List<BicicletaDTO> bicicletas) {
		this.bicicletas = bicicletas;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getIdBici() {
		return idBici;
	}

	public void setIdBici(String idBici) {
		this.idBici = idBici;
	}
}

