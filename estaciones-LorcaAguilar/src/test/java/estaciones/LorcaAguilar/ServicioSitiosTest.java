package estaciones.LorcaAguilar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;


import estaciones.modelo.SitioTuristico;
import estaciones.servicio.ResumenSitio;
import estaciones.servicio.ServicioSitios;

class ServicioSitiosTest {

    ServicioSitios servicioSitios = new ServicioSitios();  // Suponiendo que ServicioSitiosImpl implementa IServicioSitios

    @Test
    void testGetSitiosAndGetInfoSitio() {
        assertDoesNotThrow(() -> {
            List<ResumenSitio> sitios = servicioSitios.getSitios("40.712776,-3.005974");
            assertNotNull(sitios);
            SitioTuristico sitio = servicioSitios.getInfoSitio("1");
            assertNotNull(sitio);
        });
    }

}
