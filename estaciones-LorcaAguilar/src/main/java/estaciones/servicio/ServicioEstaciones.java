package estaciones.servicio;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
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
		hist.setFechaEstacionamiento(LocalDate.now());
		hist.setIdEstacion(idEstacion);
		hist.setBicicleta(bici);
		
		List<Historico> newHistorico = bici.getHistorico();
		newHistorico.add(hist);
		bici.setHistorico(newHistorico);
		
		estacion.setPuestos(estacion.getPuestos() - 1);
		LinkedList<Bicicleta> newBicis = estacion.getBicis();
		newBicis.add(bici);
		estacion.setBicis(newBicis);
		
		repositorioBicicletas.update(bici);
		repositorio.update(estacion);
	}
	

	@Override
	public void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		for (Historico historico : bici.getHistorico()) {
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
		hist.setFechaEstacionamiento(LocalDate.now());
		hist.setIdEstacion(estacionLibre.getId());
		hist.setBicicleta(bici);
		
		List<Historico> newHistorico = bici.getHistorico();
		newHistorico.add(hist);
		bici.setHistorico(newHistorico);
				
		estacionLibre.setPuestos(estacionLibre.getPuestos() - 1);
		LinkedList<Bicicleta> newBicis = estacionLibre.getBicis();
		newBicis.add(bici);
		estacionLibre.setBicis(newBicis);
		
		repositorioBicicletas.update(bici);
		repositorio.update(estacionLibre);
	}
	

	@Override
	public void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		List<Historico> historico = bici.getHistorico();
		
		String idEstacionUsada = null;
		Historico histEliminar = null;
		
		for (Historico x : historico) {
			if (x.getFechaSalida() == null) {
				idEstacionUsada = x.getIdEstacion();
				histEliminar = x;
			}
		}
		
		if (idEstacionUsada == null)
			throw new EstacionesException("no hay ninguna estacion que contenga esta bici");
		
		Estacion estacionUsada = repositorio.getById(idEstacionUsada);
		
		LinkedList<Bicicleta> newBicis = estacionUsada.getBicis();
		newBicis.remove(bici);
		estacionUsada.setBicis(newBicis);
		
		Historico hist = new Historico();
		hist.setFechaSalida(LocalDate.now());
		hist.setFechaEstacionamiento(histEliminar.getFechaEstacionamiento());
		hist.setIdEstacion(histEliminar.getIdEstacion());
		hist.setBicicleta(histEliminar.getBicicleta());
		
		List<Historico> newHistorico = bici.getHistorico();
		newHistorico.remove(histEliminar);
		newHistorico.add(hist);
		bici.setHistorico(newHistorico);

		repositorioBicicletas.update(bici);
		repositorio.update(estacionUsada);		
	}


	@Override
	public void eliminarBicicleta(String idBicicleta, String motivo) throws RepositorioException, EntidadNoEncontrada, EstacionesException {
		
		Bicicleta bici = repositorioBicicletas.getById(idBicicleta);
		
		List<Estacion> estaciones = repositorio.getAll();
		
		Estacion estacionBuscada = null;
		
		for (Estacion estacion : estaciones) {
			if (estacion.getBicis().contains(bici)) {
				estacionBuscada = estacion;
			}
		}
		
		if (estacionBuscada == null)
			throw new EstacionesException("no hay ninguna estacion que contenga esta bici");
		
		LinkedList<Bicicleta> newBicis = estacionBuscada.getBicis();
		newBicis.remove(bici);
		estacionBuscada.setBicis(newBicis);		
		
		bici.setFechaBaja(LocalDate.now());
		bici.setMotivoBaja(motivo);
		
		repositorio.update(estacionBuscada);
		repositorioBicicletas.delete(bici);
	}

	@Override
	public List<Bicicleta> getBicicletasCerca(double latitud, double longitud) throws RepositorioException {
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
	    List<Bicicleta> bicicletasCercanas = new LinkedList<>();
	    for (Estacion estacion : estacionesCercanas) {
	        bicicletasCercanas.addAll(estacion.getBicis());
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
