package estaciones.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.*;

import repositorio.Identificable;

@Entity
public class Historico implements Serializable, Identificable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id = UUID.randomUUID().toString();

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
    public String getBicicleta() {
        return idBicicleta;
    }

    public void setBicicleta(String idBicicleta) {
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
