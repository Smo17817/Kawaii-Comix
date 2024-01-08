package acquistoManagement;

import java.util.Comparator;

public class OrdineComparator implements Comparator<Ordine> {

	@Override
	public int compare(Ordine ordine1, Ordine ordine2) {
		return ordine1.getJavaDate().compareTo(ordine2.getJavaDate());
	}

}
