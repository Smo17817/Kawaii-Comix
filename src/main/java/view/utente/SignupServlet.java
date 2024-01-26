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
import javax.sql.DataSource;

import com.google.gson.Gson;
import utenteManagement.User;
import utenteManagement.UserIDS;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		RequestDispatcher dispatcher = null;
		HashMap<String , String> responseMap = new HashMap<>();
		Gson json = new Gson();
		PrintWriter out = response.getWriter();
		UserIDS userIDS = new UserIDS(ds);


		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String indirizzo = request.getParameter("indirizzo");
		String citta = request.getParameter("citta");
		String cap = request.getParameter("cap");
		String provincia = request.getParameter("provincia");
		String nazione = request.getParameter("nazione");


		if((nome == null || nome.trim().isEmpty()) || (cognome == null || cognome.trim().isEmpty()) || (email == null || email.trim().isEmpty()) || (password == null || password.trim().isEmpty()) || (indirizzo == null || indirizzo.trim().isEmpty()) || (citta == null || citta.trim().isEmpty()) || (cap == null || cap.trim().isEmpty()) || (provincia == null || provincia.trim().isEmpty()) ||(nazione == null || nazione.trim().isEmpty())){
			responseMap.put(STATUS, "Blank");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}

		if(!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))){
			responseMap.put(STATUS, "Invalid_Mail");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}

		if(password.length() < 8){
			responseMap.put(STATUS, "Invalid_Password_length");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}


		// Verifica che l'indirizzo contenga solo lettere, numeri e spazi
		if (!(indirizzo.matches("[a-zA-Z0-9\\s]+"))) {
			responseMap.put(STATUS, "Invalid_Indirizzo");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}

		// Verifica che ci siano anche caratteri alfabetici nell'indirizzo quando ci sono solo numeri e caratteri speciali
		if (indirizzo.matches("[0-9.\\s]+") && !indirizzo.matches(".*[a-zA-Z].*")) {
			responseMap.put(STATUS, "Indirizzo_Solo_Numeri");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}

		// Verifica che l'indirizzo contenga almeno un numero (numero civico)
		if (!indirizzo.matches(".*\\d+.*")) {
			responseMap.put(STATUS, "Numero_Civico_Mancante");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}



		if(!(citta.matches("[a-zA-Z]+"))){
			responseMap.put(STATUS, "Invalid_Citta");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}

		if (!(cap.matches("\\d{5}"))){
			responseMap.put(STATUS, "Invalid_Cap");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}

		if(!(provincia.matches("[a-zA-Z]{2}"))){
			responseMap.put(STATUS, "Invalid_Provincia");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}


		if(nazione.equals("-effettua una scelta-")){
			responseMap.put(STATUS, "Invalid_Nazione");
			String jsonResponse = json.toJson(responseMap);
			response.setContentType(contentType);
			out.write(jsonResponse);
			out.flush();
			return;
		}



		try {
			if (userIDS.emailExists(email)) {
				responseMap.put(STATUS, "Duplicate");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
			}else {
				User user = new User(email, password, nome, cognome, formattedIndirizzo(indirizzo), formattedCitta(citta), cap, provincia.toUpperCase(), nazione);
				userIDS.doSaveUser(user);
				System.out.println(user);
				responseMap.put(STATUS, "success");
				responseMap.put(URL, "login.jsp");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(contentType);
				out.write(jsonResponse);
				out.flush();
			}

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
			
	}

	private String formattedIndirizzo(String indirizzo){
		// Rimuovi eventuali spazi bianchi all'inizio e alla fine dell'indirizzo
		indirizzo = indirizzo.trim();

		// Converte la prima lettera di ogni parola in maiuscolo
		indirizzo = indirizzo.substring(0, 1).toUpperCase() + indirizzo.substring(1).toLowerCase();

		// Se l'indirizzo non inizia con "Via"
		if (!indirizzo.startsWith("Via")) {
			// Aggiungi "Via" all'inizio dell'indirizzo
			indirizzo = "Via " + indirizzo;
		}

		// Se l'indirizzo non contiene "N." prima del numero civico
		if (!indirizzo.matches(".*\\bN\\.[0-9]+")) {
			// Trova il numero civico nella stringa dell'indirizzo
			String numeroCivico = indirizzo.replaceAll("[^0-9]", "");

			// Aggiungi "N." prima del numero civico
			indirizzo = indirizzo.replace(numeroCivico, "N." + numeroCivico);
		}

		return indirizzo;
	}

	private String formattedCitta(String citta){
		citta = citta.trim();
		// Imposta la prima lettera della città in maiuscolo
		citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase();
		return citta;
	}



	/*** MACRO ***/
	private static final String STATUS = "status";
	private static final String LOGIN = "login.jsp";
	private static final String SIGNUP = "signup.jsp";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(SignupServlet.class.getName());
	private static final String ERROR = "Errore";

	private static final String URL = "url";
	private static final String contentType = "application/json";
}
