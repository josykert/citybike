package estaciones.modelo;

public class ResumenSitio {

	private String nombre;
	private String descripcion;
	private String distancia;
	private String urlArticulo;

	public ResumenSitio(String nombre, String descripcion, String distancia, String urlArticulo) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.distancia = distancia;
		this.urlArticulo = urlArticulo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDistancia() {
		return distancia;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}

	public String getUrlArticulo() {
		return urlArticulo;
	}

	public void setUrlArticulo(String urlArticulo) {
		this.urlArticulo = urlArticulo;
	}

	@Override
	public String toString() {
		return "ResumenSitio [nombre=" + nombre + ", descripcion=" + descripcion + ", distancia=" + distancia
				+ ", urlArticulo=" + urlArticulo + "]";
	}

	
}
