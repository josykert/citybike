package repositorio;


import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public abstract class RepositorioMongoDB<T extends Identificable> implements RepositorioString<T> {
	MongoCollection<T> mongoCollection;

	public RepositorioMongoDB() {
		mongoCollection = generico();
	}

	public MongoCollection<T> generico() {
		/**************************************
		 * MONGODB
		 ****************************************/
		Properties properties = new Properties();
        try (InputStream input = RepositorioMongoDB.class.getClassLoader().getResourceAsStream("mongo.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null; // Or handle the error appropriately
        }
        
        String mongoUri = properties.getProperty("mongo.uri");
        String databaseName = properties.getProperty("mongo.database");

		
		ConnectionString connectionString = new ConnectionString(
				mongoUri);		// I need to configure the CodecRegistry to include a codec to handle the
		// translation to and from BSON for our POJOs.
		CodecRegistry pojoCodecRegistry = CodecRegistries
				.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		// And I need to add the default codec registry, which contains all the default
		// codecs. They can handle all the major types in Java-like Boolean, Double,
		// String, BigDecimal, etc.
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				pojoCodecRegistry);

		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.codecRegistry(codecRegistry).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
				.build();

		MongoClient mongoClient = MongoClients.create(settings);

		MongoDatabase database = mongoClient.getDatabase(databaseName);

		MongoCollection<T> mongoCollection = obtenerColeccion(database);
		
	    mongoCollection.createIndex(Indexes.geo2dsphere("location"));
	    
		return mongoCollection;
	}


	public abstract MongoCollection<T> obtenerColeccion(MongoDatabase database);

	@Override
	public String add(T entity) throws RepositorioException {
		InsertOneResult r = mongoCollection.insertOne(entity);
		BsonValue v = r.getInsertedId();
		if(v.isNull())
			return null;
		
		return v.asObjectId().getValue().toHexString();
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		// Filtro para buscar el documento por su ID
		ObjectId objectId;
        objectId = new ObjectId(entity.getId());
		Bson filter = Filters.eq("_id", objectId);
		// Reemplaza un documento en la colección según el filtro especificado
		UpdateResult result = mongoCollection.replaceOne(filter, entity);
		// Si no se encontro el documento, lanzar excepción
		if (result.getMatchedCount() == 0) {
			throw new EntidadNoEncontrada("No se encontró el documento con ID: " + entity.getId());
		}
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		// Filtro para buscar el documento por su ID
		ObjectId objectId;
        objectId = new ObjectId(entity.getId());
		Bson filter = Filters.eq("_id", objectId);		// Eliminar el documento
		DeleteResult result = mongoCollection.deleteOne(filter);
		// Si no se eliminó el documento, lanzar excepción
		if (result.getDeletedCount() == 0) {
			throw new EntidadNoEncontrada("No se encontró el documento con ID: " + entity.getId());
		}
	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		 // Convertir la cadena de texto 'id' en un ObjectId
	    ObjectId objectId;
	    try {
	        objectId = new ObjectId(id);
	    } catch (IllegalArgumentException e) {
	        throw new EntidadNoEncontrada("El ID proporcionado no es válido: " + id);
	    }
		// Filtro para buscar el documento por su ID
		Bson filter = Filters.eq("_id", objectId);
		// buscar el primer documento que satisfaga el filtro en este caso al buscar por
		// ID solo deberia haber un match
		T entity = mongoCollection.find(filter).first();
		// Si no se encontró el documento, lanzar excepción
		if (entity == null) {
			throw new EntidadNoEncontrada("No se encontró el documento con ID: " + id);
		}
		return entity;
	}

	@Override
	public List<T> getAll() throws RepositorioException {
		// Lista para almacenar los documentos
		List<T> entities = new ArrayList<>();
		// Obtener un cursor a todos los documentos
		MongoCursor<T> cursor = mongoCollection.find().iterator();
		try {
			// Iterar sobre el cursor para obtener cada documento
			while (cursor.hasNext()) { 
				T entity = cursor.next();
				entities.add(entity);
			}
		} finally {
			// Cerrar el cursor para liberar recursos
			cursor.close(); 
		}
		return entities;
	}

	@Override
	public List<String> getIds() throws RepositorioException {
	    // Obtener una lista de los IDs únicos
	    List<ObjectId> objectIds = mongoCollection.distinct("_id", ObjectId.class).into(new ArrayList<>());
	    List<String> ids = objectIds.stream().map(ObjectId::toHexString).collect(Collectors.toList());
	    return ids;
	}
}