package acquistoManagement;

import java.io.Serializable;

public class GestoreOrdini implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String email;
	private String nome;
	private String cognome;
	private String password;
		
	public GestoreOrdini() {
		super();
	}

	public GestoreOrdini(String email, String nome, String cognome, String password) {
		super();
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "GestoreOrdini [email_address=" + email + ", nome=" + nome + ", cognome=" + cognome
				+ ", password=" + password + "]";
	}
}
