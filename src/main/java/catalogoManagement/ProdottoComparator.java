package catalogoManagement;

import java.util.Comparator;

public class ProdottoComparator implements Comparator<Prodotto>{
	
	@Override
	public int compare(Prodotto prodotto1, Prodotto prodotto2) {
        return prodotto1.getNome().compareTo(prodotto2.getNome());
    }
}
