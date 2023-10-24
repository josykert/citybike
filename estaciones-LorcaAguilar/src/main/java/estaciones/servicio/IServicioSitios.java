package estaciones.servicio;

import java.util.LinkedList;

import estaciones.modelo.SitioTuristico;

public interface IServicioSitios {

	LinkedList<ResumenSitio> getSitios(String coordenadas);

	SitioTuristico getInfoSitio(String id);

}
