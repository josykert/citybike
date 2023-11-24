package estaciones.servicio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.Historico;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public class ServicioEstaciones implements IServicioEstaciones {
	
	ServicioSitios servicioSitios = new ServicioSitios();
	
	private Repositorio<Estacion, String> repositorio = FactoriaRepositorios.getRepositorio(Estacion.class);
	private Repositorio<Bicicleta, String> repositorioBicicletas = FactoriaRepositorios.getRepositorio(Bicicleta.class);

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


	@Override
	public String registrarBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada {
		Bicicleta bici = new Bicicleta();
		bici.setModelo(modelo);
		
		String id = repositorioBicicletas.add(bici); //Hacemos esto para poder tener el id de la bicicleta y asignarselo al historico
		
		estacionarBicicleta(id, idEstacion);
		
		return id;
	}


	@Override
	public void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		Estacion estacion = repositorio.getById(idEstacion);
		
		//TODO estacionar bici
		Historico hist = new Historico();
		hist.setFechaEstacionamiento(LocalDate.now());
		hist.setIdEstacion(idEstacion);
		hist.setIdBicicleta(idBicicleta);
		
		//repositorioHistoricos.add(hist);
		
		bici.getHistorico().add(hist);
		
		estacion.setPuestos(estacion.getPuestos()-1);
		
		repositorioBicicletas.update(bici);
		repositorio.update(estacion);
		
	}


	@Override
	public void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		//Estacion estacion = repositorio.getById(idEstacion);
		
		//TODO estacionar bici
		
		repositorioBicicletas.update(bici);
		//repositorio.update(estacion);
		
	}


	@Override
	public void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		//TODO quitar estacionamiento de la bici
		
	}


	@Override
	public void eliminarBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		bici.setFechaBaja(LocalDate.now());
		
		bici.setMotivoBaja(motivo);
		
	}


	@Override
	public List<Bicicleta> getBicicletasCerca(double latitud, double longitud) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Estacion> getEstacionesTuristicas() throws RepositorioException {
		List<Estacion> estaciones = repositorio.getAll();
		return null;
	}

}
