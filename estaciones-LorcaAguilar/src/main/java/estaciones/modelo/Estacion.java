package estaciones.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import repositorio.Identificable;

public class Estacion implements Identificable{
	
	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID) 
	private String id;
	private String nombre;
	private int puestos;
	private String codigoPostal;
	private double latitud;
	private double longitud;
	private LocalDate fechaAlta;
	private LinkedList<SitioTuristico> sitios;
	private LinkedList<String> bicis;

	public Estacion() {
		// TODO Auto-generated constructor stub
	}
	
	public Estacion(String nombre, int puestos, String codigoPostal, double latitud, double longitud) {
		this.nombre = nombre;
		this.puestos = puestos;
		this.codigoPostal = codigoPostal;
		this.latitud = latitud;
		this.longitud = longitud;
		this.fechaAlta = LocalDate.now();
		this.sitios = new LinkedList<SitioTuristico>();
		this.bicis = new LinkedList<String>();
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

	public LinkedList<String> getBicis() {
		return bicis;
	}

	public void setBicis(LinkedList<String> bicis) {
		this.bicis = bicis;
	}

	@Override
	public String toString() {
		return "Estacion [id=" + id + ", nombre=" + nombre + ", puestos=" + puestos + ", codigoPostal=" + codigoPostal
				+ ", latitud=" + latitud + ", longitud=" + longitud + ", fechaAlta=" + fechaAlta + ", sitios=" + sitios
				+ ", bicis=" + bicis + "]";
	}

}
