package estaciones.web.incidencia;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import dto.IncidenciaDTO;
import estaciones.modelo.Incidencia;
import estaciones.servicio.ServicioIncidencias;
import estaciones.modelo.EstadoIncidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.IncidenciasException;
import repositorio.RepositorioException;

@Named
@ViewScoped
public class IncidenciasBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LinkedList<IncidenciaDTO> incidencias;

	private ServicioIncidencias servicio;

	public IncidenciasBean() throws RepositorioException, IncidenciasException{
		
		List<Incidencia> incidenciasAbiertas = servicio.getIncidenciasAbiertas();

		for(Incidencia i: incidenciasAbiertas) {
			IncidenciaDTO dto = servicio.getById(i.getId());
			this.incidencias.add(dto);
		}
		
	}
	
	public void goToDetail() throws RepositorioException, EntidadNoEncontrada, IOException, IncidenciasException {
		//TODO deberiamos cambiar los throws por try/catch (?)
			// Obtener el parámetro de la solicitud
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String idIncidencia = externalContext.getRequestParameterMap().get("idIncidencia");
			IncidenciaDTO i = servicio.getById(idIncidencia);
			
			if (i.getEstado() == EstadoIncidencia.PENDIENTE) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("detailIncidenciaPendiente.xhtml?idIncidencia=" + idIncidencia);
			} else if (i.getEstado() == EstadoIncidencia.ASIGNADA){
				FacesContext.getCurrentInstance().getExternalContext().redirect("detailIncidenciaAsignada.xhtml?idIncidencia=" + idIncidencia);
			}
		
	}

	public LinkedList<IncidenciaDTO> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(LinkedList<IncidenciaDTO> incidencias) {
		this.incidencias = incidencias;
	}
}
