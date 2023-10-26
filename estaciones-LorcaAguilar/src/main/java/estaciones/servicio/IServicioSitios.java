package estaciones.servicio;

import java.util.List;

import estaciones.modelo.SitioTuristico;
import repositorio.RepositorioException;

public interface IServicioSitios {

	List<ResumenSitio> getSitios(String coordenadas) throws RepositorioException;

	SitioTuristico getInfoSitio(String id);

}
