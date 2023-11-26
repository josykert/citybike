package repositorio;

public class EstacionesException extends Exception{
	public EstacionesException(String msg, Throwable causa) {		
		super(msg, causa);
	}
	
	public EstacionesException(String msg) {
		super(msg);		
	}
}
