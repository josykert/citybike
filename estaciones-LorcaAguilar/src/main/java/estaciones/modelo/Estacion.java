package estaciones.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import repositorio.Identificable;

public class Estacion implements Identificable{

	private String id;
	private String nombre;
	private int puestos;
	private String codigoPostal;
	private String coordenadas;
	private LocalDate fechaAlta;
	private LinkedList<SitioTuristico> sitios;

	public Estacion(String nombre, int puestos, String codigoPostal, String coordenadas) {
		this.nombre = nombre;
		this.puestos = puestos;
		this.codigoPostal = codigoPostal;
		this.coordenadas = coordenadas;
		this.fechaAlta = LocalDate.now();
		this.sitios = new LinkedList<SitioTuristico>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPuestos() {
		return puestos;
	}

	public void setPuestos(int puestos) {
		this.puestos = puestos;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public List<SitioTuristico> getSitios() {
		return sitios;
	}

	public void setSitios(LinkedList<SitioTuristico> sitios) {
		this.sitios = sitios;
	}

	@Override
	public String toString() {
		return "Estacion [id=" + id + ", nombre=" + nombre + ", puestos=" + puestos + ", codigoPostal=" + codigoPostal
				+ ", coordenadas=" + coordenadas + ", fechaAlta=" + fechaAlta + ", sitios=" + sitios + "]";
	}

	
}
