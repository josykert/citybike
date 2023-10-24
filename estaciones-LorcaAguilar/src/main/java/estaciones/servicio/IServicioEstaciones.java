package estaciones.servicio;

import java.util.LinkedList;

import estaciones.modelo.Estacion;
import estaciones.modelo.SitioTuristico;

public interface IServicioEstaciones {

	String crear(String nombre, int puestos, String codigoPostal, String coordenadas);

	LinkedList<SitioTuristico> getSitiosProximos(String id);

	void setSitiosProximos(String id, LinkedList<SitioTuristico> sitios);

	Estacion getEstacion(String id);

}
