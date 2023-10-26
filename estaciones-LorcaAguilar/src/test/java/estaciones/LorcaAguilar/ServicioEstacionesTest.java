package estaciones.LorcaAguilar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import estaciones.servicio.ResumenSitio;
import estaciones.servicio.ServicioEstaciones;

class ServicioEstacionesTest {

    ServicioEstaciones servicioEstaciones = new ServicioEstaciones();  // Suponiendo que ServicioEstacionesImpl implementa IServicioEstaciones

    @Test
    void testCrearAndGetSitiosProximos() {
        assertDoesNotThrow(() -> {
            String id = servicioEstaciones.crear("Estacion1", 20, "28005", "40.712776,-74.005974");
            assertNotNull(id);
            
            List<ResumenSitio> sitios = servicioEstaciones.getSitiosProximos(id);
            assertNotNull(sitios);
        });
    }

}
