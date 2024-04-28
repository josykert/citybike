package dto;

import java.time.LocalDate;
import java.util.LinkedList;

public class EstacionDTO {
	
	private String id;
	private String nombre;
	private int puestos;
	private String codigoPostal;
    private double latitud;
    private double longitud;
	private LinkedList<String> bicis;
    
    public EstacionDTO(String id, String nombre, int puestos, String codigoPostal, double latitud, double longitud) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.puestos = puestos;
		this.codigoPostal = codigoPostal;
		this.latitud = latitud;
		this.longitud = longitud;
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

	public LinkedList<String> getBicis() {
		return bicis;
	}

	public void setBicis(LinkedList<String> bicis) {
		this.bicis = bicis;
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
    
    
    
    

}
