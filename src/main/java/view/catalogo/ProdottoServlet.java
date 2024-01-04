package view.catalogo;

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
import javax.servlet.http.HttpSession;

import catalogoManagement.ProdottoIDS;

@WebServlet("/ProdottoServlet")
public class ProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProdottoServlet.class.getName());
	private static final String error = "Errore";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String isbn = request.getParameter("isbn");
		HttpSession session = request.getSession();
		//TODO aggiungere DataSource
		ProdottoIDS prodottoIDS = new ProdottoIDS(null); 
		
		try {
			request.setAttribute("prodotto", prodottoIDS.doRetrieveByIsbn(isbn));
			session.setAttribute("prodotto", prodottoIDS.doRetrieveByIsbn(isbn)); // velocizza le cose in caso di acquisto
			//TODO aggiungere la jsp di destinazione
			RequestDispatcher dispatcher = request.getRequestDispatcher(""); 
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}	
	}

}
