package estaciones.repositorio;

import estaciones.modelo.Incidencia;
import repositorio.RepositorioJPA;

public class RepositorioIncidenciasJPA extends RepositorioJPA<Incidencia>{

	@Override
	public Class<Incidencia> getClase() {
		return Incidencia.class;
	}

	@Override
	public String getNombre() {
		
		return "Incidencia";
	}

}
