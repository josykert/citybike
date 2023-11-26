package estaciones.LorcaAguilar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import estaciones.modelo.Bicicleta;
import estaciones.modelo.Estacion;
import estaciones.servicio.ServicioEstaciones;
import repositorio.EntidadNoEncontrada;
import repositorio.EstacionesException;
import repositorio.RepositorioException;

import org.junit.jupiter.api.Assertions;
import java.util.List;

public class SistemaBicicletasTest {

    private ServicioEstaciones servicioEstaciones;

    @BeforeEach
    void setUp() throws RepositorioException {
        servicioEstaciones = new ServicioEstaciones();
    }

    @Test
    void testGetEstacion() {
        Assertions.assertDoesNotThrow(() -> {
            String id = servicioEstaciones.crear("Test", 5, "2013", 0, 0);
            Estacion estacion = servicioEstaciones.getEstacion(id);
            Assertions.assertNotNull(estacion);
        });

        Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
            servicioEstaciones.getEstacion("idInexistente");
        });
    }

    @Test
    void testRegistrarBicicleta() throws RepositorioException {
        String id = servicioEstaciones.crear("Test", 5, "2013", 0, 0);
        String idBicicleta = Assertions.assertDoesNotThrow(() -> 
            servicioEstaciones.registrarBicicleta("modelo", id)
        );
        Assertions.assertNotNull(idBicicleta);

        Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
            servicioEstaciones.registrarBicicleta("modelo", "idEstacionInexistente");
        });
    }

    @Test
    void testEstacionarBicicletaConIdEstacion() {
        Assertions.assertDoesNotThrow(() -> {
            String id = servicioEstaciones.crear("Test", 5, "2013", 0, 0);
            servicioEstaciones.registrarBicicleta("Decathlon", id);
            
            servicioEstaciones.estacionarBicicleta("Decathlon", id);
        });

        Assertions.assertThrows(RepositorioException.class, () -> {
            servicioEstaciones.estacionarBicicleta("idBicicletaInexistente", "idEstacion");
        });
    }

    @Test
    void testEstacionarBicicletaSinIdEstacion() {
        Assertions.assertDoesNotThrow(() -> {
            servicioEstaciones.estacionarBicicleta("idBicicleta");
        });

        Assertions.assertThrows(EstacionesException.class, () -> {
            servicioEstaciones.estacionarBicicleta("idBicicletaInexistente");
        });
    }

    @Test
    void testRetirarBicicleta() {
        Assertions.assertDoesNotThrow(() -> {
            servicioEstaciones.retirarBicicleta("idBicicleta");
        });

        Assertions.assertThrows(EstacionesException.class, () -> {
            servicioEstaciones.retirarBicicleta("idBicicletaInexistente");
        });
    }

    @Test
    void testEliminarBicicleta() {
        Assertions.assertDoesNotThrow(() -> {
            servicioEstaciones.eliminarBicicleta("idBicicleta", "motivo");
        });

        Assertions.assertThrows(EstacionesException.class, () -> {
            servicioEstaciones.eliminarBicicleta("idBicicletaInexistente", "motivo");
        });
    }

    @Test
    void testGetBicicletasCerca() {
        List<Bicicleta> bicicletas = Assertions.assertDoesNotThrow(() -> 
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
