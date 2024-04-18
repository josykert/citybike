package estaciones.LorcaAguilar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import estaciones.modelo.GeoJsonPoint;
import estaciones.modelo.SitioTuristico;
import estaciones.servicio.ResumenSitio;
import estaciones.servicio.ServicioSitios;

class ServicioSitiosTest {

    ServicioSitios servicioSitios = new ServicioSitios();  // Suponiendo que ServicioSitiosImpl implementa IServicioSitios

    @Test
    void testGetSitiosAndGetInfoSitio() {
        assertDoesNotThrow(() -> {
        	GeoJsonPoint location = new GeoJsonPoint(40.712776, -3.005974);
            List<ResumenSitio> sitios = servicioSitios.getSitios(location);
            System.out.println(sitios);
            assertNotNull(sitios);
            SitioTuristico sitio = servicioSitios.getInfoSitio("Catedral_de_Murcia");
            System.out.println(sitio);
            assertNotNull(sitio);
            sitio = servicioSitios.getInfoSitio("Valdegrudas");
            System.out.println(sitio);
            assertNotNull(sitio);
        });
    }

}
