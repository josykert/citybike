package estaciones.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import repositorio.Identificable;

@Entity
@Table(name = "bicicleta")
public class Bicicleta implements Serializable, Identificable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    private String id = UUID.randomUUID().toString();
    
    private String modelo;

    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    @Lob
    private String motivoBaja;
    private EstadoBicicleta estado;

    public Bicicleta() {
    	this.estado = EstadoBicicleta.DISPONIBLE;
        this.fechaAlta = LocalDate.now();
    }
    
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
	public EstadoBicicleta getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoBicicleta estado) {
		this.estado = estado;
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
	public String getMotivoBaja() {
		return motivoBaja;
	}
	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}
	@Override
	public String toString() {
		return "Bicicleta [id=" + id + ", modelo=" + modelo + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja
				+ ", motivoBaja=" + motivoBaja + "]";
	}
	
}
