package estaciones.servicio;

import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public interface IServicioEstaciones {

	String crear(String nombre, int puestos, String codigoPostal, double latitud, double longitud) throws RepositorioException;

	List<SitioTuristico> getSitiosProximos(String id) throws SitiosTuristicosException, RepositorioException, EntidadNoEncontrada;

	void setSitiosProximos(String id, LinkedList<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada;

	Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;
	
	String registrarBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada;
	
	void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada;
	
	void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;
	
	void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;
	
	void eliminarBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada;
	
	List<Bicicleta> getBicicletasCerca(double latitud, double longitud);
	
	List<Estacion> getEstacionesTuristicas() throws RepositorioException;

}
