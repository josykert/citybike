package estaciones.repositorio;

import estaciones.modelo.SitioTuristico;
import repositorio.RepositorioJSON;
import repositorio.RepositorioXML;

public class RepositorioSitiosTuristicosJSON extends RepositorioJSON<SitioTuristico>{

	@Override
	public Class<SitioTuristico> getClase() {
		
		return SitioTuristico.class;
	}

}
