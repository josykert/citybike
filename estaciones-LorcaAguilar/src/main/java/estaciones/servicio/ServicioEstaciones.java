package estaciones.servicio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.EstadoBicicleta;
import estaciones.modelo.Historico;
import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public class ServicioEstaciones implements IServicioEstaciones {
	
	ServicioSitios servicioSitios = new ServicioSitios();
	
	private Repositorio<Estacion, String> repositorio = FactoriaRepositorios.getRepositorio(Estacion.class);
	private Repositorio<Bicicleta, String> repositorioBicicletas = FactoriaRepositorios.getRepositorio(Bicicleta.class);
	private Repositorio<Historico, String> repositorioHistorico = FactoriaRepositorios.getRepositorio(Historico.class);

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
		Estacion estacion = repositorio.getById(idEstacion);
		
		if (estacion.getPuestos() <= 0) {
			throw new EstacionesException("La estación no tiene puestos disponibles para aparcar la bicicleta.");
		}
		
		Historico hist = new Historico();
		hist.setFechaEstacionamiento(LocalDateTime.now());
		hist.setIdEstacion(idEstacion);
		hist.setBicicleta(bici.getId());
		
		List<String> newHistorico = bici.getHistorico();
		newHistorico.add(hist.getId());
		bici.setHistorico(newHistorico);
		
		estacion.setPuestos(estacion.getPuestos() - 1);
		LinkedList<String> newBicis = estacion.getBicis();
		newBicis.add(bici.getId());
		estacion.setBicis(newBicis);
		
		repositorioHistorico.add(hist);
		repositorioBicicletas.update(bici);
		repositorio.update(estacion);
	}
	

	@Override
	public void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		for (String idHistorico : bici.getHistorico()) {
			Historico historico = repositorioHistorico.getById(idHistorico);
			if (historico.getFechaSalida() == null) {
				throw new EstacionesException("La bicicleta ya está aparcada en una estación.");
			}
		}
	
		List<Estacion> estaciones = repositorio.getAll();
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
		
		List<String> newHistorico = bici.getHistorico();
		newHistorico.add(hist.getId());
		bici.setHistorico(newHistorico);
				
		estacionLibre.setPuestos(estacionLibre.getPuestos() - 1);
		LinkedList<String> newBicis = estacionLibre.getBicis();
		newBicis.add(bici.getId());
		estacionLibre.setBicis(newBicis);
		
		repositorioHistorico.add(hist);
		repositorioBicicletas.update(bici);
		repositorio.update(estacionLibre);
	}
	

	@Override
	public void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		List<String> historicos = bici.getHistorico();
		
		String idEstacionUsada = null;
		Historico histEliminar = null;
		
		for (String x : historicos) {
			Historico historico = repositorioHistorico.getById(x);
			if (historico.getFechaSalida() == null) {
				idEstacionUsada = historico.getIdEstacion();
				histEliminar = historico;
			}
		}
		
		if (idEstacionUsada == null)
			throw new EstacionesException("no hay ninguna estacion que contenga esta bici");
		
		Estacion estacionUsada = repositorio.getById(idEstacionUsada);
		
		LinkedList<String> newBicis = estacionUsada.getBicis();
		newBicis.remove(bici.getId());
		estacionUsada.setBicis(newBicis);
		
		Historico hist = new Historico();
		hist.setFechaSalida(LocalDateTime.now());
		hist.setFechaEstacionamiento(histEliminar.getFechaEstacionamiento());
		hist.setIdEstacion(histEliminar.getIdEstacion());
		hist.setBicicleta(histEliminar.getBicicleta());
		
		List<String> newHistorico = bici.getHistorico();
		newHistorico.remove(histEliminar.getId());
		newHistorico.add(hist.getId());
		bici.setHistorico(newHistorico);

		repositorioHistorico.update(hist);
		repositorioBicicletas.update(bici);
		repositorio.update(estacionUsada);		
	}


	@Override
	public void eliminarBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		List<Estacion> estaciones = repositorio.getAll();
		
		Estacion estacionBuscada = null;
		
		for (Estacion estacion : estaciones) {
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
		
		Historico hist = new Historico();
		hist.setFechaSalida(LocalDateTime.now());
		hist.setIdEstacion(estacionBuscada.getId());
		hist.setBicicleta(bici.getId());
		
		List<String> newHistorico = bici.getHistorico();
		newHistorico.add(hist.getId());
		bici.setHistorico(newHistorico);
		
		repositorioHistorico.update(hist);
		repositorio.update(estacionBuscada);
		repositorioBicicletas.update(bici);
	}

	@Override
	public List<String> getBicicletasCerca(double latitud, double longitud) throws RepositorioException, EntidadNoEncontrada {
	    List<Estacion> estaciones = repositorio.getAll();

	    // Ordenar las estaciones por proximidad a las coordenadas dadas
	    Collections.sort(estaciones, new Comparator<Estacion>() {
	        @Override
	        public int compare(Estacion e1, Estacion e2) {
	            double dist1 = haversine(latitud, longitud, e1.getLatitud(), e1.getLongitud());
	            double dist2 = haversine(latitud, longitud, e2.getLatitud(), e2.getLongitud());
	            return Double.compare(dist1, dist2);
	        }
	    });

	    // Seleccionar las 3 estaciones más cercanas
	    List<Estacion> estacionesCercanas = estaciones.subList(0, Math.min(3, estaciones.size()));

	    // Combinar las listas de bicicletas de estas estaciones
	    List<String> bicicletasCercanas = new LinkedList<>();
	    for (Estacion estacion : estacionesCercanas) {
	    	for (String idBici : estacion.getBicis()) {
	    		Bicicleta bici = repositorioBicicletas.getById(idBici);
	    		if (bici.getEstado() == EstadoBicicleta.DISPONIBLE) {
	    			bicicletasCercanas.add(idBici);	    		}
	    	}
	    }

	    return bicicletasCercanas;
	}

	// Método auxiliar para calcular la distancia Haversine
	private double haversine(double lat1, double lon1, double lat2, double lon2) {
	    // Radio de la tierra en kilómetros
	    double R = 6371; 
	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);
	    lat1 = Math.toRadians(lat1);
	    lat2 = Math.toRadians(lat2);

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	               Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    return R * c;
	}

	@Override
	public List<Estacion> getEstacionesTuristicas() throws RepositorioException {
	    List<Estacion> estaciones = repositorio.getAll();

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

}
