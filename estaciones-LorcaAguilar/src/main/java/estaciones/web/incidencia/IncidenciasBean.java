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
import estaciones.servicio.ServicioEstaciones;
import estaciones.servicio.ServicioIncidencias;
import estaciones.modelo.EstadoIncidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.IncidenciasException;
import repositorio.RepositorioException;

@Named
@ViewScoped
public class IncidenciasBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<IncidenciaDTO> incidencias;

	private ServicioIncidencias servicio = new ServicioIncidencias();
	
	private ServicioEstaciones servicioEstaciones = new ServicioEstaciones();

	public IncidenciasBean() throws RepositorioException, IncidenciasException{
		System.out.println("\n\nFunciono\n\n");
		String idEstacion = servicioEstaciones.crear("Catedral", 4, "30001", 37.98, -1.12);
		try {
			String idBicicleta = servicioEstaciones.registrarBicicleta("BMX", idEstacion);
			System.out.println("\n\nFunciono\n\n");
			String idIncidencia=servicio.crearIncidencia(idBicicleta, "No funciona");
			System.out.println("\n\nFunciono\n\n");
		} catch (RepositorioException | EntidadNoEncontrada | EstacionesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.incidencias = servicio.getIncidenciasAbiertasDTO();	
		
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

	public List<IncidenciaDTO> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(List<IncidenciaDTO> incidencias) {
		this.incidencias = incidencias;
	}
}
