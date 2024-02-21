package estaciones.servicio;

import java.util.List;

import estaciones.modelo.Incidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.IncidenciasException;
import repositorio.RepositorioException;

public interface IServicioIncidencias {

	String crearIncidencia(String idBicicleta, String descripcion) throws RepositorioException, IncidenciasException, EntidadNoEncontrada;
	
	void cancelarIncidencia(String idIncidencia) throws EntidadNoEncontrada, RepositorioException, IncidenciasException;
	
	void asignarIncidencia(String idIncidencia, String nombre) throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException;
	
	void resolverIncidencia(String idIncidencia, boolean reparada) throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException;
	
	List<Incidencia> getIncidenciasAbiertas() throws RepositorioException;
}
