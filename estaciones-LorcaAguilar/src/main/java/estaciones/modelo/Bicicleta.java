package estaciones.modelo;

import java.time.LocalDate;
import java.util.LinkedList;

import repositorio.Identificable;

public class Bicicleta implements Identificable{

	private String id;
	private String modelo;
	private LocalDate fechaAlta;
	private LocalDate fechaBaja;
	private LocalDate motivoBaja;
	private LinkedList<Estacion> estaciones;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public LocalDate getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public LocalDate getMotivoBaja() {
		return motivoBaja;
	}
	public void setMotivoBaja(LocalDate motivoBaja) {
		this.motivoBaja = motivoBaja;
	}
	public LinkedList<Estacion> getEstaciones() {
		return estaciones;
	}
	public void setEstaciones(LinkedList<Estacion> estaciones) {
		this.estaciones = estaciones;
	}
	@Override
	public String toString() {
		return "Bicicleta [id=" + id + ", modelo=" + modelo + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja
				+ ", motivoBaja=" + motivoBaja + ", estaciones=" + estaciones + "]";
	}

	
}
