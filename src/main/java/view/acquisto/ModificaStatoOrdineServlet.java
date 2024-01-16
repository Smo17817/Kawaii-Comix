package view.acquisto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import acquistoManagement.OrdineIDS;

@WebServlet("/ModificaStatoOrdineServlet")
public class ModificaStatoOrdineServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		RequestDispatcher dispatcher = null;
		

		try {					
			int id = Integer.parseInt(request.getParameter("orderId"));
			String stato = request.getParameter("stato");
			int statoId = 1;
		
			if(stato.equals("Confermato"))
				statoId = 1;
			else if(stato.equals("Spedito"))
				statoId = 2;
			else if(stato.equals("Annullato"))
				statoId = 3;
			
			OrdineIDS ordineIDS = new OrdineIDS(ds);
			ordineIDS.doUpdateStatoById(id, statoId);			

			dispatcher = request.getRequestDispatcher("controllaOrdini.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException | ServletException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		} 
	}
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(ModificaStatoOrdineServlet.class.getName());
	private static final String ERROR = "Errore";
}
