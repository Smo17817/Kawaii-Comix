package view.utente;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import utenteManagement.User;
import utenteManagement.UserIDS;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
			HttpSession session = request.getSession();
			RequestDispatcher dispatcher = null;


			UserIDS userIDS = new UserIDS(ds);
			
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String nome = request.getParameter("nome");
			String cognome = request.getParameter("cognome");
			String indirizzo = request.getParameter("indirizzo");
			String città = request.getParameter("città");
			String comune = request.getParameter("comune");
			String cap = request.getParameter("cap");
			String provincia = request.getParameter("provincia");
			String nazione = request.getParameter("nazione");
			
			try {
				if (userIDS.EmailExists(email)) { //TODO va aggiunto anche alla registrazione
					request.setAttribute(STATUS, "Invalid_email");
					dispatcher = request.getRequestDispatcher("signup.jsp");
					dispatcher.forward(request, response);
					return;
				}
				else {
					User user = new User(email,password,nome,cognome,indirizzo, città,cap, provincia, nazione);

					userIDS.doSaveUser(user);
					dispatcher = request.getRequestDispatcher("login.jsp");
					dispatcher.forward(request, response);
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	/*** MACRO ***/
	private static final String STATUS = "status";
}
