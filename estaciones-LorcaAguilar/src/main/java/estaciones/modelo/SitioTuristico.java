package estaciones.modelo;

import java.util.List;

import repositorio.Identificable;

public class SitioTuristico implements Identificable {

	private String nombre;
	private String resumen;
	private List<String> categorias;
	private List<String> enlaces;
	private List<String> imagenes;
	private String urlArticulo;

	public String getId() {
		return nombre;
	}

	public void setId(String nombre) {
		this.nombre = nombre;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias2) {
		this.categorias = categorias2;
	}

	public List<String> getEnlaces() {
		return enlaces;
	}

	public void setEnlaces(List<String> enlaces) {
		this.enlaces = enlaces;
	}

	public List<String> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<String> imagenes) {
		this.imagenes = imagenes;
	}

	public String getUrlArticulo() {
		return urlArticulo;
	}

	public void setUrlArticulo(String url) {
		this.urlArticulo = url;
	}

	@Override
	public String toString() {
		return "SitioTuristico [nombre=" + nombre + ", resumen=" + resumen + ", categorias=" + categorias + ", enlaces="
				+ enlaces + ", imagenes=" + imagenes + ", urlArticulo=" + urlArticulo + "]";
	}

}
