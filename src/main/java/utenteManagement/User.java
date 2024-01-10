package utenteManagement;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String email;
	private String password;
	private String nome;
	private String cognome;
	private String indirizzo;
	private String città;
	private String comune;
	private String cap;
	private String provincia;
	private String nazione;

	public User() {
		super();
	}

	public User(Integer id, String email, String password, String nome, String cognome, String indirizzo, String città, String comune,
			String cap, String provincia, String nazione) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.indirizzo = indirizzo;
		this.città=città;
		this.comune = comune;
		this.cap = cap;
		this.provincia = provincia;
		this.nazione = nazione;
	}
	
	public User(String email, String password, String nome, String cognome, String indirizzo, String città, String comune,
			String cap, String provincia, String nazione) {
		super();
		
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.indirizzo = indirizzo;
		this.città = città;
		this.comune = comune;
		this.cap = cap;
		this.provincia = provincia;
		this.nazione = nazione;
	}

	public void setNotEmpty(String email, String password, String nome, String cognome, String indirizzo, String città, String comune, String cap, String provincia, String nazione) {
		if(!email.equals(""))
			this.email = email;
		if(!password.equals(""))
			this.password = password;
		if(!nome.equals(""))
			this.nome = nome;
		if(!cognome.equals(""))
			this.cognome = cognome;
		if(!indirizzo.equals(""))
			this.indirizzo = indirizzo;
		if(!città.equals(""))
			this.città = città;
		if(!comune.equals(""))
			this.comune = comune;
		if(!cap.equals(""))
			this.cap = cap;
		if(!provincia.equals(""))
			this.provincia = provincia;
		if(!nazione.equals(""))
			this.nazione = nazione;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public String getCittà() {
		return città;
	}
	
	public void setCittà(String città) {
		this.città=città;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNazione() {
		return nazione;
	}

	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", nome=" + nome + ", cognome="
				+ cognome + ", indirizzo=" + indirizzo + ", città=" + città +", comune=" + comune + ", cap=" + cap + ", provincia="
				+ provincia + ", nazione=" + nazione + "]";
	}

}
