package acquistoManagement;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ordine implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date data;
	private Double totale;
	private Integer userId;
	private Integer stato;
	private Integer metodoSpedizione;
	private ArrayList<OrdineSingolo> ordiniSingoli = new ArrayList<>();
	
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

	public Ordine(Integer id, Date data, Double totale, Integer userId, Integer stato, Integer metodoSpedizione,
			List<OrdineSingolo> ordiniSingoli) {
		super();
		this.id = id;
		this.data = data;
		this.totale = totale;
		this.userId = userId;
		this.stato = stato;
		this.metodoSpedizione = metodoSpedizione;
		this.ordiniSingoli = (ArrayList<OrdineSingolo>) ordiniSingoli;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	/*** ATTENZIONE: il cambiamento di formato con uno breve comporta la restituzione di una stringa ***/
	public String getData() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(this.data);
	}
	
	public Date getJavaDate() {
		return this.data;
	}

	public void setData(Date data) {
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

	public ArrayList<OrdineSingolo> getOrdiniSingoli() {
		return ordiniSingoli;
	}

	public void setOrdiniSingoli(ArrayList<OrdineSingolo> ordiniSingoli) {
		this.ordiniSingoli = ordiniSingoli;
	}

	@Override
	public String toString() {
		return "Ordine [id=" + id + ", data=" + data + ", totale=" + totale + ", userId=" + userId + ", stato=" + stato
				+ ", metodoSpedizione=" + metodoSpedizione + ", ordiniSingoli=" + ordiniSingoli + "]";
	}

}
