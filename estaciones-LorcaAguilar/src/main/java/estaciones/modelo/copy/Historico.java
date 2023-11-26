package estaciones.modelo.copy;

import java.time.LocalDate;

import repositorio.Identificable;

public class Historico implements Identificable{

	private String id;
	private String idBicicleta;
	private String idEstacion;
	private LocalDate fechaEstacionamiento;
	private LocalDate fechaSalida;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdBicicleta() {
		return idBicicleta;
	}
	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}
	public String getIdEstacion() {
		return idEstacion;
	}
	public void setIdEstacion(String idEstacion) {
		this.idEstacion = idEstacion;
	}
	public LocalDate getFechaEstacionamiento() {
		return fechaEstacionamiento;
	}
	public void setFechaEstacionamiento(LocalDate fechaEstacionamiento) {
		this.fechaEstacionamiento = fechaEstacionamiento;
	}
	public LocalDate getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	
	
}
