package estaciones.servicio;

import java.util.List;

import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public interface IServicioSitios {
	
	SitioTuristico getInfoSitio(String id) throws SitiosTuristicosException, RepositorioException, EntidadNoEncontrada;

	List<ResumenSitio> getSitios(double latitud, double longitud)
			throws RepositorioException, EntidadNoEncontrada, SitiosTuristicosException;
}
