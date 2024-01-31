package view.acquisto;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.Gson;

import acquistoManagement.Carrello;
import catalogoManagement.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utenteManagement.User;

@WebServlet("/CarrelloServlet")
public class CarrelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	

	public CarrelloServlet() {}
		

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		HttpSession session = request.getSession();
		Gson json = new Gson();

		Carrello carrello = (Carrello) session.getAttribute("carrello");
		String isbn = request.getParameter("isbn");
		User user = (User) session.getAttribute("user");

		String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		String quantita = requestBody.split("=")[1];

		try {
			PrintWriter out = response.getWriter();

			if (isbn == null) {
				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty("url", "carrello.jsp");
				response.setContentType("application/json");
				out.write(jsonResponse.toString());
				return;
			}

			if (user == null) {
				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty("url", "login.jsp");
				jsonResponse.add("user", json.toJsonTree(user));
				response.setContentType("application/json");
				out.write(jsonResponse.toString());
			}
			else if(Integer.valueOf(quantita) == 0){
				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty("quantita", "Invalid_quantita");
				response.setContentType("application/json");
				out.write(jsonResponse.toString());
			}else {

				ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
				Prodotto prodotto = prodottoIDS.doRetrieveByIsbn(isbn);

				if(prodotto != null){
					carrello.add(prodotto);
					session.setAttribute("carrello" , carrello);
				}


				int numeroProdottiNelCarrello = carrello.getListaProdotti().size();

				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty("numeroProdotti", numeroProdottiNelCarrello);
				jsonResponse.add("listaProdotti", json.toJsonTree(carrello.getListaProdotti())); // Converti la lista di prodotti in un JsonArray
				response.setContentType("application/json");
				out.write(jsonResponse.toString());
			}


		} catch (SQLException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(CarrelloServlet.class.getName());
	private static final String ERROR = "Errore";
}
