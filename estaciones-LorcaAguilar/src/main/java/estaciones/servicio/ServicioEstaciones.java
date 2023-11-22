package estaciones.servicio;

import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public class ServicioEstaciones implements IServicioEstaciones {
	
	ServicioSitios servicioSitios = new ServicioSitios();
	
	private Repositorio<Estacion, String> repositorio = FactoriaRepositorios.getRepositorio(Estacion.class);

	@Override
	public String crear(String nombre, int puestos, String codigoPostal, double latitud, double longitud)
			throws RepositorioException {

		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio");

		if (puestos <= 0)
			throw new IllegalArgumentException("puestos: no debe ser 0 ni un número negativo");

		if (codigoPostal == null || codigoPostal.isEmpty())
			throw new IllegalArgumentException("codigoPostal: no debe ser nulo ni vacio");

		Estacion estacion = new Estacion(nombre, puestos, codigoPostal, latitud, longitud);

		String id = repositorio.add(estacion);

		return id;
	}


	@Override
	public List<SitioTuristico> getSitiosProximos(String id) throws RepositorioException, EntidadNoEncontrada, SitiosTuristicosException {
		
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		
		Estacion estacion = repositorio.getById(id);
		
		List<ResumenSitio> resumenes = new LinkedList<ResumenSitio>();
		
		resumenes = servicioSitios.getSitios(estacion.getLatitud(), estacion.getLongitud());
		
		List<SitioTuristico> sitios= new LinkedList<SitioTuristico>();
		
		SitioTuristico sitio;
		
		for(ResumenSitio r: resumenes) {
			sitio = servicioSitios.getInfoSitio(r.getNombre());
			sitios.add(sitio);
			System.out.println(sitio);
		}
				 
		return sitios;
	}

	@Override
	public void setSitiosProximos(String id, LinkedList<SitioTuristico> sitios)
			throws RepositorioException, EntidadNoEncontrada {

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		Estacion estacion = repositorio.getById(id);

		estacion.setSitios(sitios);

		repositorio.update(estacion);

	}

	@Override
	public Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada {

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		return repositorio.getById(id);
	}

}
