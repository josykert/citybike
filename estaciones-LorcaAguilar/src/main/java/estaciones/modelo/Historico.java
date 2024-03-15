package estaciones.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import repositorio.Identificable;

public class Historico implements Identificable{

    /**
	 * 
	 */
	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID) 
	private String id;

	private String idBicicleta;
	
	private String idEstacion;
	private LocalDateTime fechaEstacionamiento;
	private LocalDateTime fechaSalida;
	
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
	public LocalDateTime getFechaEstacionamiento() {
		return fechaEstacionamiento;
	}
	public void setFechaEstacionamiento(LocalDateTime fechaEstacionamiento) {
		this.fechaEstacionamiento = fechaEstacionamiento;
	}
	public LocalDateTime getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(LocalDateTime fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	
	
}
