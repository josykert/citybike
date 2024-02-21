package estaciones.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import estaciones.modelo.Historico;
import repositorio.RepositorioMongoDB;

public class RepositorioHistoricoMongoDB extends RepositorioMongoDB<Historico> {
	
    public RepositorioHistoricoMongoDB() {
    	// Llamar al constructor de la clase padre
    	super(); 
    }
    
    @Override
    public MongoCollection<Historico> obtenerColeccion(MongoDatabase database) {
    	// Obtener la colección de restaurantes
        MongoCollection<Historico> collection = database.getCollection("historicos", Historico.class); 
        return collection;
    }

}
