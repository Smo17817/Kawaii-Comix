package acquistoManagement;

import java.io.Serializable;
import java.util.Date;

public class Ordine implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date data;
	private Double totale;
	private Integer userId;
	private Integer stato;
	private Integer metodoSpedizione;
	
	public Ordine() {
		super();
	}

	public Ordine(Integer id, Date data, Double totale, Integer userId, Integer stato, Integer metodoSpedizione) {
		super();
		this.id = id;
		this.data = data;
		this.totale = totale;
		this.userId = userId;
		this.stato = stato;
		this.metodoSpedizione = metodoSpedizione;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public java.util.Date getData() {
		return data;
	}

	public void setData(java.util.Date data) {
		this.data = data;
	}

	public Double getTotale() {
		return totale;
	}

	public void setTotale(Double totale) {
		this.totale = totale;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStato() {
		return stato;
	}

	public void setStato(Integer stato) {
		this.stato = stato;
	}

	public Integer getMetodoSpedizione() {
		return metodoSpedizione;
	}

	public void setMetodoSpedizione(Integer metodoSpedizione) {
		this.metodoSpedizione = metodoSpedizione;
	}

	@Override
	public String toString() {
		return "Ordine [id=" + id + ", data=" + data + ", totale=" + totale + ", userId=" + userId + ", stato=" + stato
				+ ", metodoSpedizione=" + metodoSpedizione + "]";
	}	
}
