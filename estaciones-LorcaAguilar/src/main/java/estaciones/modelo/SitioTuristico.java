package estaciones.modelo;

import java.util.LinkedList;

import estaciones.servicio.ResumenSitio;
import repositorio.Identificable;

public class SitioTuristico implements Identificable {

	private String id;
	private String nombre;
	private ResumenSitio resumen;
	private LinkedList<String> categorias;
	private LinkedList<String> enlaces;
	private String imagen;
	private String url;

	public SitioTuristico(String nombre, ResumenSitio resumen, String imagen, String url) {
		this.nombre = nombre;
		this.resumen = resumen;
		this.categorias = new LinkedList<String>();
		this.enlaces = new LinkedList<String>();
		this.imagen = imagen;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ResumenSitio getResumen() {
		return resumen;
	}

	public void setResumen(ResumenSitio resumen) {
		this.resumen = resumen;
	}

	public LinkedList<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(LinkedList<String> categorias) {
		this.categorias = categorias;
	}

	public LinkedList<String> getEnlaces() {
		return enlaces;
	}

	public void setEnlaces(LinkedList<String> enlaces) {
		this.enlaces = enlaces;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "SitioTuristico [nombre=" + nombre + ", resumen=" + resumen + ", categorias=" + categorias + ", enlaces="
				+ enlaces + ", imagen=" + imagen + ", url=" + url + "]";
	}

}
