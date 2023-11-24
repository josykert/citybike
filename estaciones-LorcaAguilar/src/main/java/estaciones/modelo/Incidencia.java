package estaciones.modelo;

import java.time.LocalDate;

import repositorio.Identificable;

public class Incidencia implements Identificable{

	private String id;
	private LocalDate fechaCreacion;
	private LocalDate fechaCierre;
	private String idBicicleta;
	private String nombreOperario;
	private Estado estado;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getIdBicicleta() {
		return idBicicleta;
	}
	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public LocalDate getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(LocalDate fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public String getNombreOperario() {
		return nombreOperario;
	}
	public void setNombreOperario(String nombreOperario) {
		this.nombreOperario = nombreOperario;
	}
	
}
