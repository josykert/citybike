package repositorio;

@SuppressWarnings("serial")
public class SitiosTuristicosException extends Exception {

	public SitiosTuristicosException(String msg, Throwable causa) {		
		super(msg, causa);
	}
	
	public SitiosTuristicosException(String msg) {
		super(msg);		
	}
	
		
}