package view.acquisto;

import acquistoManagement.Carrello;
import catalogoManagement.Prodotto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/RimuoviProdotto")
public class RimuoviProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		HttpSession session = request.getSession();
		try {
			Carrello carrello = (Carrello) session.getAttribute("carrello");
			String isbnProdotto = request.getParameter("isbn");

			for (Prodotto prodotto : carrello.getListaProdotti()) {
				if (isbnProdotto != null && prodotto.getIsbn().equals(isbnProdotto)) {

					carrello.getListaProdotti().remove(prodotto);
					break;

				}
			}
			carrello.setListaProdotti(carrello.getListaProdotti());
			session.setAttribute("carrello", carrello);
		} catch (Exception e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(RimuoviProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
}
