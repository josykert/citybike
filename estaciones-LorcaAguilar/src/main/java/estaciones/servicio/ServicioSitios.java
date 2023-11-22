package estaciones.servicio;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import estaciones.modelo.SitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositorio.SitiosTuristicosException;

public class ServicioSitios implements IServicioSitios {
	
	Document documento;
	DocumentBuilder analizador;
	InputStreamReader fuente;
	
	private Repositorio<SitioTuristico, String> repositorio = FactoriaRepositorios.getRepositorio(SitioTuristico.class);

	private JsonArray getJsonArray(JsonObject obj, String property) {
		if (obj.containsKey(property) && !obj.isNull(property)) {
			return obj.getJsonArray(property);
		} else {
			return null;
		}
	}

	private String getValue(JsonObject info) {
		JsonValue.ValueType type = info.get("value").getValueType();
		if (type == JsonValue.ValueType.STRING) {
			return info.getString("value");
		} else if (type == JsonValue.ValueType.NUMBER) {
			return String.valueOf(info.getInt("value"));
		} else {
			return "";
		}
	}
	
	public static String formatearUrl(String nombre) {
        return nombre.replace(" ", "_");
    }
	
	@Override
	public List<ResumenSitio> getSitios(double latitud, double longitud) throws RepositorioException, EntidadNoEncontrada, SitiosTuristicosException {
		List<ResumenSitio> resumenes = new LinkedList<ResumenSitio>();

		// 1. Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

		// 2. Pedir a la factoría la construcción del analizador
		try {
			analizador = factoria.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		String url = "http://api.geonames.org/findNearbyWikipedia?lat=" + latitud
				+ "&lng=" + longitud + "&country=ES&radius=10&username=arso&lang=es";;
		System.out.println(url);

		try {
			documento = analizador.parse(url);
		} catch (SAXException e) {
			throw new SitiosTuristicosException("Excepcion SAX", e);
		} catch (IOException e) {
			throw new SitiosTuristicosException("Excepcion entrada y salida", e);
		}
		

		NodeList elementos = documento.getElementsByTagName("entry");

		for (int i = 0; i < elementos.getLength(); i++) {

			Node elemento = elementos.item(i);
			
			if (elemento.getNodeType() == Node.ELEMENT_NODE) {
				Element sitioElement = (Element) elemento;
				ResumenSitio resumenSitio = new ResumenSitio();								
				
				resumenSitio.setUrlArticulo(sitioElement.getElementsByTagName("wikipediaUrl").item(0).getTextContent());
				
				resumenSitio.setDistancia(sitioElement.getElementsByTagName("distance").item(0).getTextContent());

				resumenSitio.setNombre(formatearUrl(sitioElement.getElementsByTagName("title").item(0).getTextContent()));
				
				resumenSitio.setResumen(sitioElement.getElementsByTagName("summary").item(0).getTextContent());
				
				resumenes.add(resumenSitio);
			}
		}
		return resumenes;
	}

	@Override
	public SitioTuristico getInfoSitio(String id) throws  RepositorioException, EntidadNoEncontrada, SitiosTuristicosException {
		
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");
		
		try {
			repositorio.getById(id);
		} catch (Exception e) {
			SitioTuristico sitio = new SitioTuristico();
			
			String dbpediaUrl = "https://es.dbpedia.org/data/"
					+ id + ".json";
			// Leer JSON desde DBpedia
			try {
				fuente = new InputStreamReader(new java.net.URL(dbpediaUrl).openStream());
			} catch (MalformedURLException e1) {
				throw new SitiosTuristicosException("Url mal formada " + id, e);
			} catch (IOException e1) {
				throw new SitiosTuristicosException("Excepcion entrada y salida " + id, e);
			}
			
			JsonReader jsonReader = Json.createReader(fuente);
			JsonObject obj = jsonReader.readObject();
			
			String wikiResource = "http://es.dbpedia.org/resource/" + id;
			JsonObject objJSON = obj.getJsonObject(wikiResource);
			
			sitio.setId(id);

			sitio.setUrlArticulo(wikiResource);
			
			// Obtener descripcion
			String resumenProperty = "http://dbpedia.org/ontology/abstract";
			JsonArray resumenArray = getJsonArray(objJSON, resumenProperty);
			if (resumenArray != null) {
				for (JsonObject info : resumenArray.getValuesAs(JsonObject.class)) {
					String resumen = getValue(info);
					sitio.setResumen(resumen);
				}
			}
			
			// Obtener categorias
			String categoriaProperty = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
			JsonArray categoriaArray = getJsonArray(objJSON, categoriaProperty);
			if (categoriaArray != null) {
				List<String> categorias = new LinkedList<String>();
				for (JsonObject info : categoriaArray.getValuesAs(JsonObject.class)) {
					String categoria = getValue(info);
					categorias.add(categoria);
				}
				sitio.setCategorias(categorias);
			}

			// Obtener enlaces externos
			String externalLinkProperty = "http://dbpedia.org/ontology/wikiPageExternalLink";
			JsonArray externalLinkArray = getJsonArray(objJSON, externalLinkProperty);
			if (externalLinkArray != null) {
				List<String> enlacesExternos = new LinkedList<String>();
				for (JsonObject info : externalLinkArray.getValuesAs(JsonObject.class)) {
					String enlaceExterno = getValue(info);
					enlacesExternos.add(enlaceExterno);
				}
				sitio.setEnlaces(enlacesExternos);
			}

			// Obtener imagen
			String imagenProperty = "http://es.dbpedia.org/property/imagen";
			JsonArray imagenArray = getJsonArray(objJSON, imagenProperty);
			if (imagenArray != null) {
				List<String> imagenes = new LinkedList<String>();
				for (JsonObject info : imagenArray.getValuesAs(JsonObject.class)) {
					String imagen = getValue(info).replace(" ", "_");
					imagenes.add(imagen);
				}
				sitio.setImagenes(imagenes);
			}
			
			repositorio.add(sitio/*, id*/);

			return sitio;
		}
		
		return repositorio.getById(id);
	}
}
