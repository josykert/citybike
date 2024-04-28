package dto;

import java.time.LocalDate;


import estaciones.modelo.EstadoIncidencia;

public class IncidenciaDTO {
	
	private String id;
	private LocalDate fechaCreacion;
	private String idBicicleta;
	private EstadoIncidencia estado;
	
	public IncidenciaDTO(String id, LocalDate fechaCreacion, String idBicicleta, EstadoIncidencia estado ) {
		this.id = id;
		this.fechaCreacion = fechaCreacion;
		this.idBicicleta = idBicicleta;
		this.estado = estado;
	}

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

	public EstadoIncidencia getEstado() {
		return estado;
	}

	public void setEstado(EstadoIncidencia estado) {
		this.estado = estado;
	}

}