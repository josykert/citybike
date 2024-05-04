package estaciones.web.incidencia;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import dto.IncidenciaDTO;
import estaciones.servicio.IServicioIncidencias;
import estaciones.servicio.ServicioIncidencias;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.IncidenciasException;
import repositorio.RepositorioException;

@Named
@ViewScoped
public class IncidenciaDetailBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private IncidenciaDTO incidenciadto;

	private String idIncidencia;

	private String nombreOperador;

	private String motivo;

	private boolean reparada;

	private IServicioIncidencias servicio = new ServicioIncidencias();

	public void init() throws IncidenciasException {

		incidenciadto = servicio.getById(idIncidencia);

	}

	public boolean asignarIncidencia()
			throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException, IOException {
		if (nombreOperador == null || nombreOperador == "") {
			PrimeFaces.current().executeScript("PF('widgetErrorDialogAsig').show()");
			return false;
		}
		servicio.asignarIncidencia(incidenciadto.getId(), nombreOperador);
		nombreOperador = null;
		FacesContext.getCurrentInstance().getExternalContext().redirect("incidencias.xhtml");
		return true;
	}

	public boolean cancelarIncidencia()
			throws EntidadNoEncontrada, RepositorioException, IncidenciasException, IOException {
		if (motivo == null || motivo == "") {
			PrimeFaces.current().executeScript("PF('widgetErrorDialogCan').show()");
			return false;
		}
		servicio.cancelarIncidencia(incidenciadto.getId());
		motivo = null;
		FacesContext.getCurrentInstance().getExternalContext().redirect("incidencias.xhtml");
		return true;

	}

	public boolean resolverIncidencia()
			throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException, IOException {
		if (motivo == null || motivo == "") {
			PrimeFaces.current().executeScript("PF('widgetErrorDialogoResolverIncidencia').show()");
			return false;
		}

		servicio.resolverIncidencia(incidenciadto.getId(), reparada);
		motivo = null;
		FacesContext.getCurrentInstance().getExternalContext().redirect("incidencias.xhtml");
		return true;

	}

	public IncidenciaDTO getIncidenciadto() {
		return incidenciadto;
	}

	public void setIncidenciadto(IncidenciaDTO incidenciadto) {
		this.incidenciadto = incidenciadto;
	}

	public String getIdIncidencia() {
		return idIncidencia;
	}

	public void setIdIncidencia(String idIncidencia) {
		this.idIncidencia = idIncidencia;
	}

	public String getNombreOperador() {
		return nombreOperador;
	}

	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public boolean isReparada() {
		return reparada;
	}

	public void setReparada(boolean reparada) {
		this.reparada = reparada;
	}

	public IServicioIncidencias getServicio() {
		return servicio;
	}

	public void setServicio(IServicioIncidencias servicio) {
		this.servicio = servicio;
	}

}