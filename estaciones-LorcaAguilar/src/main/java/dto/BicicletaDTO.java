package dto;

import estaciones.modelo.EstadoBicicleta;

public class BicicletaDTO {

	private String id;
    private String modelo;
    private EstadoBicicleta estado;
    
    public BicicletaDTO(String id, String modelo, EstadoBicicleta estado) {
		super();
		this.id = id;
		this.modelo = modelo;
		this.estado = estado;
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
    
}
