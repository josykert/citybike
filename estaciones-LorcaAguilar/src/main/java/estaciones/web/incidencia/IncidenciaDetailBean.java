package estaciones.web.incidencia;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import dto.IncidenciaDTO;
import estaciones.servicio.IServicioIncidencias;
import estaciones.servicio.ServicioIncidencias;
import repositorio.IncidenciasException;

@Named
@ViewScoped
public class IncidenciaDetailBean {
	
	private static final long serialVersionUID = 1L;

	//no se si tiene que tener un dto o qué
	private IncidenciaDTO incidenciadto;
	
	private IServicioIncidencias servicio = new ServicioIncidencias();
	
	public void init() throws IncidenciasException {

		//Esto es una fumadita que me he cascao, no sé si está bien
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		incidenciadto = servicio.getById(externalContext.getRequestParameterMap().get("idIncidencia"));
		
	}
	
}
