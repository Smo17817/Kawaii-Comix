/* Servlet per il Gestore degli Ordini: gli permette di visualizzare tutti gli ordini */

package view.acquisto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

import acquistoManagement.Ordine;
import acquistoManagement.OrdineComparator;
import acquistoManagement.OrdineIDS;
import acquistoManagement.OrdineSingolo;

@WebServlet("/OrdineServlet")
public class OrdineServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private final DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;

		try {
			OrdineIDS ordineIDS = new OrdineIDS(ds);
			ArrayList<Ordine> ordini = new ArrayList<>();
			
			// Recupero tutti gli ordini dal Database (per i singoli ordini ci pensa l'IDS)
			for(Ordine ordine : ordineIDS.doRetrieveAllOrdini()) 
				ordini.add(ordine);		
			
			// Li ordino in base alla data
			Collections.sort(ordini, new OrdineComparator());
			
			session.setAttribute("ordini", ordini);
			dispatcher = request.getRequestDispatcher("ordine.jsp");
			dispatcher.forward(request, response);

		} catch (SQLException | ServletException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		} 
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doPost(request, response);
		} catch (ServletException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		}  
	}
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(OrdineServlet.class.getName());
	private static final String ERROR = "Errore";
}
