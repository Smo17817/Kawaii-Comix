package view.utente;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import utenteManagement.User;
import utenteManagement.UserIDS;

@WebServlet("/DatiPersonaliServlet")
public class DatiPersonaliServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DatiPersonaliServlet.class.getName());
	private static final String error = "Errore";
	private static final String STATUS = "status";
	
	//TODO AGGIUNGERE LA JSP
	private static final String url = "";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;

		User user = (User) session.getAttribute("user");

		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String indirizzo = request.getParameter("indirizzo");
		String comune = request.getParameter("comune");
		String cap = request.getParameter("cap");
		String provincia = request.getParameter("provincia");
		String nazione = request.getParameter("nazione");
		
		//TODO AGGIUNGERE DATASOURCE
		UserIDS userIDS = new UserIDS(null);

		try {			
			//Se l'utente modifica la password, le due password devono combaciare
			if (!password1.equals("") && !(password1.equals(password2))) {
				request.setAttribute(STATUS, "Invalid_password");
				dispatcher = request.getRequestDispatcher(url);
				dispatcher.forward(request, response);
				return;
			}

			//Aggiorna solo i valori inseriti
			user.setNotEmpty(email, password1, nome, cognome, indirizzo, comune, cap, provincia, nazione);
			
			boolean checkUpdate = userIDS.doUpdateUser(user);
			
			//Controlla che l'update sia riuscito
			if (checkUpdate)
				request.setAttribute(STATUS, "success");
			else
				request.setAttribute(STATUS, "failed");
			
			session.setAttribute("user", user);

			dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);
			
		} catch (ServletException | IOException | SQLException e) {
			logger.log(Level.ALL, error, e);
		}
	}
}