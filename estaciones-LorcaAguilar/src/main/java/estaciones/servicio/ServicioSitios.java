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
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;

public class ServicioSitios implements IServicioSitios {
	
	Document documento;
	DocumentBuilder analizador;
	InputStreamReader fuente;
	
	private Repositorio<SitioTuristico, String> repositorio = FactoriaRepositorios.getRepositorio(SitioTuristico.class);
	
	private String getUrlByCoordinates(String coordinates) {
		String latitud = coordinates.split(",")[0];
		String longitud = coordinates.split(",")[1];
		String url = "http://api.geonames.org/findNearbyWikipedia?lat=" + latitud
				+ "lng" + longitud + "&country=ES&radius=10&username=arso&lang=es";
		return url;
	}

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
	
	@Override
	public List<ResumenSitio> getSitios(String coordenadas) throws RepositorioException {
		List<ResumenSitio> resumenes = new LinkedList<ResumenSitio>();

		// 1. Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

		// 2. Pedir a la factoría la construcción del analizador
		try {
			analizador = factoria.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		String url = getUrlByCoordinates(coordenadas);

		try {
			documento = analizador.parse(url);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		NodeList elementos = documento.getElementsByTagName("entry");

		for (int i = 0; i < elementos.getLength(); i++) {

			Node elemento = elementos.item(i);

			if (elemento.getNodeType() == Node.ELEMENT_NODE) {
				Element sitioElement = (Element) elemento;
				ResumenSitio resumenSitio = new ResumenSitio();
				SitioTuristico sitio = new SitioTuristico();
				sitio.setNombre(sitioElement.getElementsByTagName("title").item(0).getTextContent());
				resumenSitio.setNombre(sitioElement.getElementsByTagName("title").item(0).getTextContent());

				String wikipediaUrl = sitioElement.getElementsByTagName("wikipediaUrl").item(0).getTextContent();
				String dbpediaUrl = "https://es.dbpedia.org/data/"
						+ wikipediaUrl.substring(wikipediaUrl.lastIndexOf("/") + 1) + ".json";

				// Leer JSON desde DBpedia
				try {
					fuente = new InputStreamReader(new java.net.URL(dbpediaUrl).openStream());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JsonReader jsonReader = Json.createReader(fuente);
				JsonObject obj = jsonReader.readObject();

				String wikiResource = "http://es.dbpedia.org/resource/" + sitio.getNombre().replace(" ", "_");
				JsonObject objJSON = obj.getJsonObject(wikiResource);

				// Obtener descripcion
				String resumenProperty = "http://dbpedia.org/ontology/abstract";
				JsonArray resumenArray = getJsonArray(objJSON, resumenProperty);
				if (resumenArray != null) {
					for (JsonObject info : resumenArray.getValuesAs(JsonObject.class)) {
						String resumen = getValue(info);
						resumenSitio.setResumen(resumen);
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

				// DISTANCIA COORDENADAS DE BÚSQUEDAS - RESUMEN
				
				// URL WIKIPEDIA - RESUMEN Y SITIO EN CONCRETO
				
				repositorio.add(sitio);
				resumenes.add(resumenSitio);
			}
		}
		return resumenes;
	}

	@Override
	public SitioTuristico getInfoSitio(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
