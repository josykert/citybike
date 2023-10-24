package estaciones.servicio;

import java.util.LinkedList;

import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioEstaciones {

	String crear (String nombre, int puestos, String codigoPostal, String coordenadas) throws RepositorioException;

	LinkedList<SitioTuristico> getSitiosProximos(String id);

	void setSitiosProximos(String id, LinkedList<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada;

	Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;

}
