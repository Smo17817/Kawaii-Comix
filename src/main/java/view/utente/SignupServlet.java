package view.utente;

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

import utenteManagement.User;
import utenteManagement.UserIDS;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
			RequestDispatcher dispatcher = null;

			UserIDS userIDS = new UserIDS(ds);
			
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String indirizzo = request.getParameter("indirizzo");
			String citta = request.getParameter("citta");
			String cap = request.getParameter("cap");
			String provincia = request.getParameter("provincia");
			String nazione = request.getParameter("nazione");
			
			try {
				if (userIDS.emailExists(email)) { 
					request.setAttribute(STATUS, "Invalid_email");
					 //TODO Se la vede davidone
					dispatcher = request.getRequestDispatcher(SIGNUP);
					dispatcher.forward(request, response);
				}else {
					User user = new User(email, password, nome, cognome, indirizzo, citta, cap, provincia, nazione);				
					userIDS.doSaveUser(user);
					dispatcher = request.getRequestDispatcher(LOGIN);
					dispatcher.forward(request, response);
				}		
				
			} catch (SQLException |ServletException | IOException e) {
				logger.log(Level.ALL, ERROR, e);
			}
			
	}
	/*** MACRO ***/
	private static final String STATUS = "status";
	private static final String LOGIN = "login.jsp";
	private static final String SIGNUP = "signup.jsp";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(SignupServlet.class.getName());
	private static final String ERROR = "Errore";
}
