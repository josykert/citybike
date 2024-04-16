package estaciones.repositorio;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import estaciones.modelo.Historico;
import repositorio.RepositorioMongoDB;

import java.util.ArrayList;
import java.util.List;

public class RepositorioHistoricoMongoDB extends RepositorioMongoDB<Historico> {
    
    public RepositorioHistoricoMongoDB() {
        // Llamar al constructor de la clase padre
        super(); 
    }
    
    @Override
    public MongoCollection<Historico> obtenerColeccion(MongoDatabase database) {
        // Obtener la colección de historicos
        return database.getCollection("historicos", Historico.class); 
    }
}
