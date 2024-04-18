package estaciones.servicio;

import java.util.List;

import estaciones.modelo.GeoJsonPoint;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public interface IServicioSitios {
	
	List<ResumenSitio> getSitios(GeoJsonPoint location)
			throws RepositorioException, EntidadNoEncontrada, SitiosTuristicosException;

	SitioTuristico getInfoSitio(String id) throws SitiosTuristicosException, RepositorioException, EntidadNoEncontrada;

}
