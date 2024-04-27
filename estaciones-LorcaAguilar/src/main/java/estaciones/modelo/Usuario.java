package estaciones.modelo;

public class Usuario {
	
	private String nombre;
	private String apellidos;
	private String dni;
	private String email;
	private String username;
	private String password;
	private boolean gestor;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isGestor() {
		return gestor;
	}
	public void setGestor(boolean gestor) {
		this.gestor = gestor;
	}
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellidos=" + apellidos + ", dni=" + dni + ", email=" + email
				+ ", username=" + username + ", password=" + password + ", gestor=" + gestor + "]";
	}

}
