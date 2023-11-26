package repositorio;

@SuppressWarnings("serial")
public class IncidenciasException extends Exception {
	public IncidenciasException(String msg, Throwable causa) {		
		super(msg, causa);
	}
	
	public IncidenciasException(String msg) {
		super(msg);		
	}
}
