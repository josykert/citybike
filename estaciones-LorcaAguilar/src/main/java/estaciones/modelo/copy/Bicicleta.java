package estaciones.modelo.copy;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import repositorio.Identificable;

@Entity
@Table(name="bicicleta")
public class Bicicleta implements Identificable{

	@Id
	private String id;
	private String modelo;
    @Convert(converter = LocalDateConverter.class)
	private LocalDate fechaAlta;
    @Convert(converter = LocalDateConverter.class)
	private LocalDate fechaBaja;
	private String motivoBaja;
	
    @OneToMany(mappedBy = "bicicleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private LinkedList<Historico> historico = new LinkedList<>();

    public Bicicleta() {
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
	public LinkedList<Historico> getHistorico() {
		return historico;
	}
	public void setHistorico(LinkedList<Historico> historico) {
		this.historico = historico;
	}
	@Override
	public String toString() {
		return "Bicicleta [id=" + id + ", modelo=" + modelo + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja
				+ ", motivoBaja=" + motivoBaja + ", historico=" + historico + "]";
	}
	
	@Converter(autoApply = true)
	class LocalDateConverter implements AttributeConverter<LocalDate, java.sql.Date> {
	    @Override
	    public java.sql.Date convertToDatabaseColumn(LocalDate localDate) {
	        return (localDate == null ? null : java.sql.Date.valueOf(localDate));
	    }

	    @Override
	    public LocalDate convertToEntityAttribute(java.sql.Date sqlDate) {
	        return (sqlDate == null ? null : sqlDate.toLocalDate());
	    }
	}

	

	
}
