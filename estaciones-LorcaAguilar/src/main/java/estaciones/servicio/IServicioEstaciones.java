package estaciones.servicio;

import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioEstaciones {

	String crear (String nombre, int puestos, String codigoPostal, String coordenadas) throws RepositorioException;

	List<ResumenSitio> getSitiosProximos(String id) throws RepositorioException, EntidadNoEncontrada;

	void setSitiosProximos(String id, LinkedList<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada;

	Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;

}
