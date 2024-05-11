package estaciones.servicio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import dto.EstacionDTO;
import dto.IncidenciaDTO;
import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.modelo.EstadoBicicleta;
import estaciones.modelo.EstadoIncidencia;
import estaciones.modelo.Incidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.FactoriaRepositorios;
import repositorio.IncidenciasException;
import repositorio.Repositorio;
import repositorio.RepositorioException;

public class ServicioIncidencias implements IServicioIncidencias{
	
	private Repositorio<Incidencia, String> repositorioIncidencias = FactoriaRepositorios.getRepositorio(Incidencia.class);
	private Repositorio<Bicicleta, String> repositorioBicicleta = FactoriaRepositorios.getRepositorio(Bicicleta.class);

	ServicioEstaciones servicioEstaciones = new ServicioEstaciones();

	@Override
	public String crearIncidencia(String idBicicleta, String descripcion) throws RepositorioException, IncidenciasException, EntidadNoEncontrada {
		
        List<Incidencia> incidenciasAbiertas = getIncidenciasAbiertas();
        for (Incidencia incidencia : incidenciasAbiertas) {
            if (incidencia.getIdBicicleta().equals(idBicicleta)) {
                throw new IncidenciasException("Ya existe una incidencia abierta para esta bicicleta.");
            }
        }
        
        Bicicleta bici = repositorioBicicleta.getById(idBicicleta);
        bici.setEstado(EstadoBicicleta.INDISPONIBLE);
        
		Incidencia incidencia = new Incidencia();
		incidencia.setDescripcion(descripcion);
		incidencia.setEstado(EstadoIncidencia.PENDIENTE);
		incidencia.setFechaCreacion(LocalDate.now());
		incidencia.setIdBicicleta(idBicicleta);
		
		repositorioBicicleta.update(bici);
		return repositorioIncidencias.add(incidencia);
	}
	
	@Override
	public Incidencia getIncidencia(String idIncidencia) throws RepositorioException, EntidadNoEncontrada {
		return repositorioIncidencias.getById(idIncidencia);
	}

	@Override
	public List<Incidencia> getIncidenciasAbiertas() throws RepositorioException {
		List<Incidencia> incidencias= repositorioIncidencias.getAll();
		List<Incidencia> incidenciasAbiertas = new LinkedList<Incidencia>();
		
		for(Incidencia i: incidencias) {
			if(i.getEstado()==EstadoIncidencia.PENDIENTE || i.getEstado()==EstadoIncidencia.ASIGNADA) {
				incidenciasAbiertas.add(i);
			}
		}
		return incidenciasAbiertas;
	}

	@Override
	public void cancelarIncidencia(String idIncidencia) throws EntidadNoEncontrada, RepositorioException, IncidenciasException {
		Incidencia i = repositorioIncidencias.getById(idIncidencia);
		if (i.getEstado() == EstadoIncidencia.PENDIENTE) {
			i.setEstado(EstadoIncidencia.CANCELADO);
			repositorioIncidencias.update(i);
		} else {
			throw new IncidenciasException("no hay ninguna estacion que contenga esta bici");
		}
		
        Bicicleta bici = repositorioBicicleta.getById(i.getIdBicicleta());
        bici.setEstado(EstadoBicicleta.DISPONIBLE);
        repositorioBicicleta.update(bici);
	}

	@Override
	public void asignarIncidencia(String idIncidencia, String nombre) throws EntidadNoEncontrada, RepositorioException, IncidenciasException, EstacionesException {
		Incidencia i = repositorioIncidencias.getById(idIncidencia);
		if (i.getEstado() == EstadoIncidencia.PENDIENTE) {
			i.setEstado(EstadoIncidencia.ASIGNADA);
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
		if (i.getEstado() == EstadoIncidencia.ASIGNADA) {
			i.setEstado(EstadoIncidencia.RESUELTA);
			if(reparada) {
		        Bicicleta bici = repositorioBicicleta.getById(i.getIdBicicleta());
		        bici.setEstado(EstadoBicicleta.DISPONIBLE);
				servicioEstaciones.estacionarBicicleta(bici.getId());
		        repositorioBicicleta.update(bici);
			}
			else {
				servicioEstaciones.eliminarBicicleta(i.getIdBicicleta(), "No ha podido ser reparada");
			}
			repositorioIncidencias.update(i);
		} else {
			throw new IncidenciasException("no hay ninguna estacion que contenga esta bici");
		}
	}
	
	private IncidenciaDTO transformToDTO(Incidencia incidencia) {
		return new IncidenciaDTO(incidencia.getId(), incidencia.getFechaCreacion(), incidencia.getIdBicicleta(), incidencia.getEstado());
	}

	@Override
	public IncidenciaDTO getById(String idIncidencia) throws IncidenciasException {
		try {
			Incidencia incidencia = getIncidencia(idIncidencia);
			return transformToDTO(incidencia);
		} catch (RepositorioException e) {
			e.printStackTrace();
			throw new IncidenciasException(e.getMessage(), e);
		} catch (EntidadNoEncontrada e) {
			e.printStackTrace();
			throw new IncidenciasException(e.getMessage(), e);
		}
	}

	@Override
	public List<IncidenciaDTO> getIncidenciasAbiertasDTO() throws RepositorioException {
		
		List<IncidenciaDTO> incidenciasAbiertasDTO = new LinkedList<IncidenciaDTO>();
		
		List<Incidencia> incidenciasAbiertas = this.getIncidenciasAbiertas();
		
		for(Incidencia i: incidenciasAbiertas) {
			IncidenciaDTO dto = this.transformToDTO(i);
			incidenciasAbiertasDTO.add(dto);
		}
		
		return incidenciasAbiertasDTO;
	}
	

}
