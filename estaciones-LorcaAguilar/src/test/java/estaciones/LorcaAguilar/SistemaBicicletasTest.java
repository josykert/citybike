package estaciones.LorcaAguilar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import estaciones.modelo.Estacion;
import estaciones.servicio.ServicioEstaciones;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.RepositorioException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class SistemaBicicletasTest {

    private static ServicioEstaciones servicioEstaciones;
    private static String estacionId;

    
    @BeforeAll
    static void initAll() throws RepositorioException {
        servicioEstaciones = new ServicioEstaciones();
        estacionId = servicioEstaciones.crear("Test", 10, "2013", 0, 0);
    }

    @Test
    void testGetEstacion() {
        Assertions.assertDoesNotThrow(() -> {
            Estacion estacion = servicioEstaciones.getEstacion(estacionId);
            Assertions.assertNotNull(estacion);
        });

        Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
            servicioEstaciones.getEstacion("idInexistente");
        });
    }

    @Test
    void testRegistrarBicicleta() throws RepositorioException {
        String idBicicleta = Assertions.assertDoesNotThrow(() -> 
            servicioEstaciones.registrarBicicleta("modelo", estacionId)
        );
        Assertions.assertNotNull(idBicicleta);

        Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
            servicioEstaciones.registrarBicicleta("modelo", "idEstacionInexistente");
        });
    }

    @Test
    void testEstacionarBicicletaConIdEstacion() {
        Assertions.assertDoesNotThrow(() -> {            
            String idBici = servicioEstaciones.registrarBicicleta("Decathlon", estacionId);
            
            servicioEstaciones.estacionarBicicleta(idBici, estacionId);
        });

    }

    @Test
    void testEstacionarBicicletaSinIdEstacion() {
        Assertions.assertDoesNotThrow(() -> {
            String nuevaBici = servicioEstaciones.registrarBicicleta("BiciCarreras", estacionId);
            servicioEstaciones.retirarBicicleta(nuevaBici);
            servicioEstaciones.estacionarBicicleta(nuevaBici);
        });
    }

    @Test
    void testRetirarBicicleta() {
        Assertions.assertDoesNotThrow(() -> {
            String idBicicleta = Assertions.assertDoesNotThrow(() -> 
                servicioEstaciones.registrarBicicleta("modelo", estacionId)
            );
            servicioEstaciones.retirarBicicleta(idBicicleta);
        });
    }

    @Test
    void testEliminarBicicleta() {
        Assertions.assertDoesNotThrow(() -> {
            String idBicicleta = Assertions.assertDoesNotThrow(() -> 
                servicioEstaciones.registrarBicicleta("modelo", estacionId)
            );
            servicioEstaciones.eliminarBicicleta(idBicicleta, "mal estacionada");
        });

    }

    @Test
    void testGetBicicletasCerca() {
        List<String> bicicletas = Assertions.assertDoesNotThrow(() -> 
            servicioEstaciones.getBicicletasCerca(0.0, 0.0)
        );
        Assertions.assertNotNull(bicicletas);
    }

    @Test
    void testGetEstacionesTuristicas() {
        List<Estacion> estaciones = Assertions.assertDoesNotThrow(() -> 
            servicioEstaciones.getEstacionesTuristicas()
        );
        Assertions.assertNotNull(estaciones);
    }
}
