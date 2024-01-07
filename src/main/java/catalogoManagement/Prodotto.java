package catalogoManagement;

import java.io.Serializable;

public class Prodotto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String isbn;
	private String nome;
	private String autore;
	private String descrizione;
	private String immagine;
	private Double prezzo;
	private Integer quantita;
	private String genere;
	private String categoria;
	private Integer copieVendute;
	
	public Prodotto() {
		super();
	}
	
	// La differenzasta nell'assenza di copieVendute
	public Prodotto(String isbn, String nome, String autore, String descrizione, String immagine, Double prezzo,
			Integer quantita, String genere, String categoria) {
		super();
		this.isbn = isbn;
		this.nome = nome;
		this.autore = autore;
		this.descrizione = descrizione;
		this.immagine = immagine;
		this.prezzo = prezzo;
		this.quantita = quantita;
		this.genere = genere;
		this.categoria = categoria;
		this.copieVendute = 0;
	}

	public Prodotto(String isbn, String nome, String autore, String descrizione, String immagine, Double prezzo,
			Integer quantita, String genere, String categoria, Integer copieVendute) {
		super();
		this.isbn = isbn;
		this.nome = nome;
		this.autore = autore;
		this.descrizione = descrizione;
		this.immagine = immagine;
		this.prezzo = prezzo;
		this.quantita = quantita;
		this.genere = genere;
		this.categoria = categoria;
		this.copieVendute = copieVendute;
	}
	
	public void setNotEmpty(String nome, String autore, String descrizione, String immagine, Double prezzo,
			Integer quantita, String genere, String categoria) {
		if(!nome.equals(""))
			this.nome = nome;
		if(!autore.equals(""))
			this.autore = autore;
		if(!descrizione.equals(""))
			this.descrizione = descrizione;
		if(!immagine.equals(""))
			this.immagine = immagine;
		if(prezzo >= 0.0)
			this.prezzo = prezzo;
		if(quantita >= 0)
			this.quantita = quantita;
		if(!genere.equals(""))
			this.genere = genere;
		if(!categoria.equals(""))
			this.categoria = categoria;	
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Integer getCopieVendute() {
		return copieVendute;
	}

	public void setCopieVendute(Integer copieVendute) {
		this.copieVendute = copieVendute;
	}

	@Override
	public String toString() {
		return "Prodotto [isbn=" + isbn + ", nome=" + nome + ", autore=" + autore + ", descrizione=" + descrizione
				+ ", immagine=" + immagine + ", prezzo=" + prezzo + ", quantita=" + quantita + ", genere=" + genere
				+ ", categoria=" + categoria + ", copieVendute=" + copieVendute + "]";
	}

}
