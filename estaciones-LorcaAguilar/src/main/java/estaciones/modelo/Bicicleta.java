package estaciones.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import repositorio.Identificable;

public class Bicicleta implements Identificable{

	private String id;
	private String modelo;
	private LocalDate fechaAlta;
	private LocalDate fechaBaja;
	private String motivoBaja;
    private LinkedList<Historico> historico;
    
    public Bicicleta() {
    	this.fechaAlta= LocalDate.now();
    	this.historico= new LinkedList<Historico>();
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
	

	

	
}
