package view.acquisto;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import acquistoManagement.Carrello;
import acquistoManagement.Ordine;
import acquistoManagement.OrdineIDS;
import acquistoManagement.OrdineSingolo;
import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;
import utenteManagement.User;

@WebServlet("/AddOrdineServlet")
public class AddOrdineServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;

		User user = (User) session.getAttribute("user");
		Carrello carrello = (Carrello) session.getAttribute("carrello");
		ArrayList<OrdineSingolo> ordiniSingoli = new ArrayList<>();

		try {
			// Serve per ottenere la data al momento dell'ordine
			Calendar c = Calendar.getInstance();
			java.util.Date javaDate = c.getTime();
			java.sql.Date sqlDate = new Date(javaDate.getTime());

			double totale = Double.parseDouble(request.getParameter("totale"));
			// Crea un ordineId randomico a 5 cifre
			SecureRandom random = new SecureRandom();
			int ordineId = 10000 + random.nextInt(90000);

			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			OrdineIDS ordineIDS = new OrdineIDS(ds);

			String[] values = request.getParameter("quantita").split(",");
			int i = 0;

			// Aggiorna i prodotti nel db e aggiunge contestualmente gli ordini singoli
			// associati a quello generale
			for (Prodotto prodotto : carrello.getListaProdotti()) {
				Integer quantitaScelta = Integer.parseInt(values[i]);
				Double totParziale = prodotto.getPrezzo() * quantitaScelta;

				// Modifica la quantità del prodotto in magazzino e le copie vendute in base
				// all'acquisto attuale
				prodotto.sommaQuantita(-quantitaScelta);
				prodotto.sommaCopieVendute(+quantitaScelta);
				prodottoIDS.doUpdateProdotto(prodotto);

				ordiniSingoli.add(new OrdineSingolo(quantitaScelta, totParziale, ordineId, prodotto));
				// Passa alla prossima quantità nella stringa con le quantità
				i++;
			}
			
			ordineIDS.doSaveOrdine(new Ordine(ordineId, sqlDate, totale, user.getId(), 1, 1, ordiniSingoli));

			carrello.empty();
			session.setAttribute("carrello", carrello);

			dispatcher = request.getRequestDispatcher("profilo.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException | ServletException | IOException | NumberFormatException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(AddOrdineServlet.class.getName());
	private static final String ERROR = "Errore";
}
