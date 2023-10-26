package estaciones.servicio;

public class ResumenSitio {

	private String nombre;
	private String resumen;
	private String distancia;
	private String urlArticulo;

	public ResumenSitio() {
		// TODO Auto-generated constructor stub
	}	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
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
		return "ResumenSitio [nombre=" + nombre + ", resumen=" + resumen + ", distancia=" + distancia
				+ ", urlArticulo=" + urlArticulo + "]";
	}

	
}
