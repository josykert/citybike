package estaciones.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import estaciones.modelo.Estacion;
import repositorio.RepositorioMongoDB;

public class RepositorioEstacionesMongoDB extends RepositorioMongoDB<Estacion> {
	
    public RepositorioEstacionesMongoDB() {
    	// Llamar al constructor de la clase padre
    	super(); 
    }
    
    @Override
    public MongoCollection<Estacion> obtenerColeccion(MongoDatabase database) {
    	// Obtener la colección de restaurantes
        MongoCollection<Estacion> collection = database.getCollection("estaciones", Estacion.class); 
        return collection;
    }

}
