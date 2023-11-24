package estaciones.repositorio;

import estaciones.modelo.Bicicleta;
import repositorio.RepositorioJPA;

public class RepositorioBicicletasJPA extends RepositorioJPA<Bicicleta>{

	@Override
	public Class<Bicicleta> getClase() {
		return Bicicleta.class;
	}

	@Override
	public String getNombre() {
		return "Bicicleta";
	}

}
