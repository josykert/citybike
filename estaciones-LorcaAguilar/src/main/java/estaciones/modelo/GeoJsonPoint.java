package estaciones.modelo;

public class GeoJsonPoint {
    private String type = "Point";  // Tipo fijo para un punto GeoJSON
    private double[] coordinates;  // Array de longitud 2: [longitud, latitud]

    public GeoJsonPoint(double longitude, double latitude) {
        this.coordinates = new double[]{longitude, latitude};
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double longitude, double latitude) {
        this.coordinates = new double[]{longitude, latitude};
    }
}
