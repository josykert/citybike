package estaciones.web.bicicleta;
import estaciones.servicio.ServicioEstaciones;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;


import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;


import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import dto.BicicletaDTO;

@Named
@SessionScoped
public class BicicletasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<BicicletaDTO> bicisSeleccionadas = new LinkedList<>();
	private double latitud;
	private double longitud;
	
	private ServicioEstaciones servicio = new ServicioEstaciones();
	
	public BicicletasBean() {
	}
	
	public void buscarBicicletasCerca() throws RepositorioException, EntidadNoEncontrada {
		List<String> idBicisSeleccionadas = servicio.getBicicletasCerca(latitud, longitud);
		bicisSeleccionadas = new LinkedList<BicicletaDTO>();
		for (String idBici : idBicisSeleccionadas) {
			bicisSeleccionadas.add(servicio.getBicicletaDTO(idBici));
		}
    }
	
	public void clean() {
		bicisSeleccionadas = new LinkedList<BicicletaDTO>();
		latitud = 0;
		longitud = 0;
    }

	
	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public List<BicicletaDTO> getBicisSeleccionadas() {
		return bicisSeleccionadas;
	}

	public void setBicisSeleccionadas(List<BicicletaDTO> bicisSeleccionadas) {
		this.bicisSeleccionadas = bicisSeleccionadas;
	}

	
}
