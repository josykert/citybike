package estaciones.servicio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import estaciones.modelo.Estado;
import estaciones.modelo.Incidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.FactoriaRepositorios;
import repositorio.IncidenciasException;
import repositorio.Repositorio;
import repositorio.RepositorioException;

public class ServicioIncidencias implements IServicioIncidencias{
	
	private Repositorio<Incidencia, String> repositorioIncidencias = FactoriaRepositorios.getRepositorio(Incidencia.class);
	
	ServicioEstaciones servicioEstaciones = new ServicioEstaciones();

	@Override
	public String crearIncidencia(String idBicicleta, String descripcion) throws RepositorioException, IncidenciasException {
		
        List<Incidencia> incidenciasAbiertas = getIncidenciasAbiertas();
        for (Incidencia incidencia : incidenciasAbiertas) {
            if (incidencia.getIdBicicleta().equals(idBicicleta)) {
                throw new IncidenciasException("Ya existe una incidencia abierta para esta bicicleta.");
            }
        }

		Incidencia incidencia = new Incidencia();
		incidencia.setEstado(Estado.PENDIENTE);
		incidencia.setFechaCreacion(LocalDate.now());
		incidencia.setIdBicicleta(idBicicleta);
		
		return repositorioIncidencias.add(incidencia);
	}

	@Override
	public List<Incidencia> getIncidenciasAbiertas() throws RepositorioException {
		List<Incidencia> incidencias= repositorioIncidencias.getAll();
		List<Incidencia> incidenciasAbiertas = new LinkedList<Incidencia>();
		
		for(Incidencia i: incidencias) {
			if(i.getEstado()==Estado.PENDIENTE || i.getEstado()==Estado.ASIGNADA) {
				incidenciasAbiertas.add(i);
			}
		}
		return incidenciasAbiertas;
	}

	@Override
	public void cancelarIncidencia(String idIncidencia) throws EntidadNoEncontrada, RepositorioException, IncidenciasException {
		Incidencia i = repositorioIncidencias.getById(idIncidencia);
		if (i.getEstado() == Estado.PENDIENTE) {
			i.setEstado(Estado.CANCELADO);
			repositorioIncidencias.update(i);
		} else {
			throw new IncidenciasException("no hay ninguna estacion que contenga esta bici");
		}
	}

	@Override
	public void asignarIncidencia(String idIncidencia, String nombre) throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException {
		Incidencia i = repositorioIncidencias.getById(idIncidencia);
		if (i.getEstado() == Estado.PENDIENTE) {
			i.setEstado(Estado.ASIGNADA);
			i.setNombreOperario(nombre);
			repositorioIncidencias.update(i);
			
			// Aquí asumimos que la bicicleta deja de ocupar un sitio en la estación
			// Se llama al método para manejar la lógica de retirar la bicicleta de la estación
			servicioEstaciones.retirarBicicleta(i.getIdBicicleta());
		} else {
			throw new IncidenciasException("La incidencia no se encuentra en estado pendiente o ya no hay una estación que contenga esta bicicleta.");
		}
	}	

	@Override
	public void resolverIncidencia(String idIncidencia, boolean reparada)
			throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException {
		
		Incidencia i = repositorioIncidencias.getById(idIncidencia);
		if (i.getEstado() == Estado.PENDIENTE) {
			i.setEstado(Estado.RESUELTA);
			if(reparada) {
				//Se estaciona
				servicioEstaciones.estacionarBicicleta(idIncidencia);
			}
			else {
				servicioEstaciones.eliminarBicicleta(i.getIdBicicleta(), "No ha podido ser reparada");
			}
			repositorioIncidencias.update(i);
		} else {
			throw new IncidenciasException("no hay ninguna estacion que contenga esta bici");
		}
	}

}
