package estaciones.servicio;

import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public interface IServicioEstaciones {

	String crear(String nombre, int puestos, String codigoPostal, double latitud, double longitud) throws RepositorioException;

	List<SitioTuristico> getSitiosProximos(String id) throws SitiosTuristicosException, RepositorioException, EntidadNoEncontrada;

	void setSitiosProximos(String id, LinkedList<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada;

	Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;
	
	String registrarBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada, EstacionesException;
	
	void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada, EstacionesException;
	
	void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException;
	
	void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException;
	
	void eliminarBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada, EstacionesException;
	
	List<String> getBicicletasCerca(double latitud, double longitud) throws RepositorioException, EntidadNoEncontrada;
	
	List<Estacion> getEstacionesTuristicas() throws RepositorioException;

}
