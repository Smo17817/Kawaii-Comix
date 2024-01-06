package acquistoManagement;

import java.io.Serializable;

import catalogoManagement.Prodotto;

public class OrdineSingolo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer quantita;
	private Double totParziale;
	private Integer ordineId;
	private Prodotto prodotto;

	public OrdineSingolo() {
		super();
	}

	public OrdineSingolo(Integer id, Integer quantita, Double totParziale, Integer ordineId, Prodotto prodotto) {
		super();
		this.id = id;
		this.quantita = quantita;
		this.totParziale = totParziale;
		this.ordineId = ordineId;
		this.prodotto = prodotto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

	public Double getTotParziale() {
		return totParziale;
	}

	public void setTotParziale(Double totParziale) {
		this.totParziale = totParziale;
	}

	public Integer getOrdineId() {
		return ordineId;
	}

	public void setOrdineId(Integer ordineId) {
		this.ordineId = ordineId;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	@Override
	public String toString() {
		return "OrdineSingolo [id=" + id + ", quantita=" + quantita + ", totParziale=" + totParziale + ", ordineId="
				+ ordineId + ", prodotto=" + prodotto + "]";
	}

}
