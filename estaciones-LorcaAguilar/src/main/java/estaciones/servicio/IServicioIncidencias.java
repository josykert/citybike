package estaciones.servicio;

import java.util.List;

import estaciones.modelo.Incidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioIncidencias {

	String crearIncidencia(String idBicicleta, String descripcion) throws RepositorioException;
	
	void cancelarIncidencia(String idIncidencia) throws EntidadNoEncontrada, RepositorioException;
	
	void asignarIncidencia(String idIncidencia, String nombre) throws EntidadNoEncontrada, RepositorioException;
	
	void resolverIncidencia(String idIncidencia, boolean reparada) throws EntidadNoEncontrada, RepositorioException;
	
	List<Incidencia> getIncidenciasAbiertas() throws RepositorioException;
}
