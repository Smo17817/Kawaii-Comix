package view.utente;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
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

import com.google.gson.Gson;
import utenteManagement.User;
import utenteManagement.UserIDS;

@WebServlet("/DatiPersonaliServlet")
public class DatiPersonaliServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;

		User user = (User) session.getAttribute("user");

		HashMap<String , String> responseMap = new HashMap<>();
		Gson json = new Gson();
		PrintWriter out = response.getWriter();
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");


		
		UserIDS userIDS = new UserIDS(ds);
		try {

			if((nome == null || nome.trim().isEmpty()) && (cognome == null || cognome.trim().isEmpty()) && (email == null || email.trim().isEmpty()) && (password1 == null || password1.trim().isEmpty() && (password2 == null) || password2.trim().isEmpty())){
				responseMap.put(STATUS, "Blank");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
				return;
			}

			if(!(email == null || email.trim().isEmpty())) {
				if (!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
					responseMap.put(STATUS, "Invalid_Mail");
					String jsonResponse = json.toJson(responseMap);
					response.setContentType(contentType);
					out.write(jsonResponse);
					out.flush();
					return;
				}
			}

			if(!(password1 == null || password1.trim().isEmpty())){
				if (password1.length() < 8) {
					responseMap.put(STATUS, "Invalid_Password_length");
					String jsonResponse = json.toJson(responseMap);
					response.setContentType(contentType);
					out.write(jsonResponse);
					out.flush();
					return;
				}

			}

			//Se l'utente modifica la password, le due password devono combaciare
			if (!(password1.equals(password2))) {
				responseMap.put(STATUS, "Invalid_Password");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
				return;
			}

			
			if (userIDS.emailExists(email)) {
				responseMap.put(STATUS, "Mail_Presente");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
				return;
			}
			
			
			//Aggiorna solo i valori inseriti
			user.setNotEmpty(email, password1, nome, cognome, "","","","","");
			
			boolean checkUpdate = userIDS.doUpdateUser(user);
			
			//Controlla che l'update sia riuscito
			if (checkUpdate) {
				responseMap.put(STATUS, "success");
				responseMap.put(URL , "areapersonale.jsp");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
			}
			else {
				responseMap.put(STATUS, "failed");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
			}
			session.setAttribute("user", user);
			
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	
	/*** MACRO ***/
	private static final String STATUS = "status";
	private static final String URL = "url";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(DatiPersonaliServlet.class.getName());
	private static final String ERROR = "Errore";
	private static final String contentType = "application/json";
	
}