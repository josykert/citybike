package estaciones.servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import dto.BicicletaDTO;
import dto.EstacionDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.EstadoBicicleta;
import estaciones.modelo.Historico;
import estaciones.modelo.SitioTuristico;
import estaciones.repositorio.RepositorioHistoricoMongoDB;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioEstacionesAdhocMongoDB;
import repositorio.RepositorioException;
import repositorio.RepositorioHistoricoAdhocMongoDB;
import repositorio.SitiosTuristicosException;

public class ServicioEstaciones implements IServicioEstaciones {
	
	ServicioSitios servicioSitios = new ServicioSitios();
	
	private Repositorio<Bicicleta, String> repositorioBicicletas = FactoriaRepositorios.getRepositorio(Bicicleta.class);
	private RepositorioEstacionesAdhocMongoDB repositorioEstaciones = new RepositorioEstacionesAdhocMongoDB();
	private RepositorioHistoricoAdhocMongoDB repositorioHistorico = new RepositorioHistoricoAdhocMongoDB();
	
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

		String id = repositorioEstaciones.add(estacion);

		return id;
	}


	@Override
	public List<SitioTuristico> getSitiosProximos(String id) throws RepositorioException, EntidadNoEncontrada, SitiosTuristicosException {
		
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		
		Estacion estacion = repositorioEstaciones.getById(id);
		
		List<ResumenSitio> resumenes = new LinkedList<ResumenSitio>();
		
		resumenes = servicioSitios.getSitios(estacion.getLongitud(), estacion.getLatitud());
		
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

		Estacion estacion = repositorioEstaciones.getById(id);

		estacion.setSitios(sitios);

		repositorioEstaciones.update(estacion);
	}

	@Override
	public Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada {

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		return repositorioEstaciones.getById(id);
	}


	@Override
	public String registrarBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		Bicicleta bici = new Bicicleta();
		bici.setModelo(modelo);
		
		String id = repositorioBicicletas.add(bici); //Hacemos esto para poder tener el id de la bicicleta y asignarselo al historico
		
		estacionarBicicleta(id, idEstacion);
		
		return id;
	}


	@Override
	public void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		Estacion estacion = repositorioEstaciones.getById(idEstacion);
		
		
		if (estacion.getPuestos() <= 0) {
			throw new EstacionesException("La estación no tiene puestos disponibles para aparcar la bicicleta.");
		}
		
		Historico hist = new Historico();
		hist.setFechaEstacionamiento(LocalDateTime.now());
		hist.setIdEstacion(idEstacion);
		hist.setBicicleta(bici.getId());
		
		estacion.setPuestos(estacion.getPuestos() - 1);
		LinkedList<String> newBicis = estacion.getBicis();
		newBicis.add(bici.getId());
		estacion.setBicis(newBicis);
		
		repositorioHistorico.add(hist);
		repositorioBicicletas.update(bici);
		repositorioEstaciones.update(estacion);
	}
	

	@Override
	public void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);

		List<Historico> historicos = repositorioHistorico.obtenerPorIdBicicleta(idBicicleta);

		for (Historico historico : historicos) {
			if (historico.getFechaSalida() == null) {
				throw new EstacionesException("La bicicleta ya está aparcada en una estación.");
			}
		}
	
		List<Estacion> estaciones = repositorioEstaciones.getAll();
		Estacion estacionLibre = null;
		for (Estacion estacion : estaciones) {
			if (estacion.getPuestos() > 0) {
				estacionLibre = estacion;
				break;
			}
		}
		
		if (estacionLibre == null)
			throw new EstacionesException("No hay ninguna estación con puestos libres.");
		
		Historico hist = new Historico();
		hist.setFechaEstacionamiento(LocalDateTime.now());
		hist.setIdEstacion(estacionLibre.getId());
		hist.setBicicleta(bici.getId());
				
		estacionLibre.setPuestos(estacionLibre.getPuestos() - 1);
		LinkedList<String> newBicis = estacionLibre.getBicis();
		newBicis.add(bici.getId());
		estacionLibre.setBicis(newBicis);
		
		repositorioHistorico.add(hist);
		repositorioBicicletas.update(bici);
		repositorioEstaciones.update(estacionLibre);
	}
	

	@Override
	public void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		List<Historico> historicos = repositorioHistorico.obtenerPorIdBicicleta(idBicicleta);
		
		String idEstacionUsada = null;
		
		for (Historico x : historicos) {
			if (x.getFechaSalida() == null) {
				idEstacionUsada = x.getIdEstacion();
			}
		}
		
		if (idEstacionUsada == null)
			throw new EstacionesException("no hay ninguna estacion que contenga esta bici");
		
		Estacion estacionUsada = repositorioEstaciones.getById(idEstacionUsada);
		
		LinkedList<String> newBicis = estacionUsada.getBicis();
		newBicis.remove(bici.getId());
		estacionUsada.setBicis(newBicis);
		
		for (Historico hist : historicos) {
			if (hist.getFechaSalida() == null) {
				hist.setFechaSalida(LocalDateTime.now());	
				repositorioHistorico.update(hist);
			}
		}

		repositorioBicicletas.update(bici);
		repositorioEstaciones.update(estacionUsada);		
	}


	@Override
	public void eliminarBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		List<Estacion> estaciones = repositorioEstaciones.getAll();
		
		Estacion estacionBuscada = null;
		System.out.println("idBiciOficial: "+bici.getId());
		for (Estacion estacion : estaciones) {
			System.out.println(estacion.getNombre());
			System.out.println("Bicis: ");
			for(String idebici: estacion.getBicis()) {
				System.out.println("   Bici: "+ idebici);
			}
			if (estacion.getBicis().contains(bici.getId())) {
				estacionBuscada = estacion;
			}
		}
		
		if (estacionBuscada == null)
			throw new EstacionesException("no hay ninguna estacion que contenga esta bici");
		
		LinkedList<String> newBicis = estacionBuscada.getBicis();
		newBicis.remove(bici.getId());
		estacionBuscada.setBicis(newBicis);		
		
		bici.setFechaBaja(LocalDate.now());
		bici.setMotivoBaja(motivo);
		
		bici.setEstado(EstadoBicicleta.INDISPONIBLE);

		List<Historico> historicos = repositorioHistorico.obtenerPorIdBicicleta(idBicicleta);

		for (Historico hist : historicos) {
			if (hist.getFechaSalida() == null) {
				hist.setFechaSalida(LocalDateTime.now());	
				repositorioHistorico.update(hist);
			}
		}
		
		repositorioEstaciones.update(estacionBuscada);
		repositorioBicicletas.update(bici);
	}

	@Override
	public List<Estacion> getEstacionesTuristicas() throws RepositorioException {
	    List<Estacion> estaciones = repositorioEstaciones.getAll();

	    // Ordenando las estaciones basado en el número de sitios turísticos
	    Collections.sort(estaciones, new Comparator<Estacion>() {
	        @Override
	        public int compare(Estacion e1, Estacion e2) {
	            // Comparar basado en el tamaño de la lista de sitios turísticos
	            return e2.getSitios().size() - e1.getSitios().size();
	        }
	    });

	    return estaciones;
	}


	@Override
	public List<String> getBicicletasCerca(double latitud, double longitud)
			throws RepositorioException, EntidadNoEncontrada {
		
		List<Estacion> estacionesCercanas = repositorioEstaciones.findEstacionesCercanas(latitud, longitud);
		List<String> bicicletasCercanas = new ArrayList<>();
		
		for (Estacion estacion : estacionesCercanas) {
			for (String idBici : estacion.getBicis()) {
				if (repositorioBicicletas.getById(idBici).getEstado() == EstadoBicicleta.DISPONIBLE) {
					bicicletasCercanas.add(idBici);
				}
			}
		}
		
		return bicicletasCercanas;
	}
	
	private EstacionDTO transformToDTO(Estacion estacion) {
		return new EstacionDTO(estacion.getId(), estacion.getNombre(), estacion.getPuestos(),
				estacion.getCodigoPostal(), estacion.getLatitud(), estacion.getLongitud());
	}

	@Override
	public EstacionDTO getById(String idEstacion) throws EstacionesException {
		try {
			Estacion estacion = getEstacion(idEstacion);
			return transformToDTO(estacion);
		} catch (RepositorioException e) {
			e.printStackTrace();
			throw new EstacionesException(e.getMessage(), e);
		} catch (EntidadNoEncontrada e) {
			e.printStackTrace();
			throw new EstacionesException(e.getMessage(), e);
		}
	}
	
	@Override
	public BicicletaDTO getBicicletaDTO(String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
        BicicletaDTO bicidto = new BicicletaDTO(bici.getId(), bici.getModelo(), bici.getEstado());
		return bicidto;
	}
	
	@Override
	public List<BicicletaDTO> getBicisDTO() throws RepositorioException {
		List<BicicletaDTO> bicisDTO = new LinkedList<BicicletaDTO>();
		for (Bicicleta bici : repositorioBicicletas.getAll()) {
			if (bici.getEstado() == EstadoBicicleta.DISPONIBLE) {
		        BicicletaDTO bicidto = new BicicletaDTO(bici.getId(), bici.getModelo(), bici.getEstado());
				bicisDTO.add(bicidto);
			}
		}
		return bicisDTO;
	}
	
}
