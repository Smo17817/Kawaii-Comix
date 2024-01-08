package view.acquisto;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import utenteManagement.User;

@WebServlet("/CarrelloServlet")
public class CarrelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Gson json = new Gson();

		Carrello carrello = (Carrello) session.getAttribute("carrello");
		String isbn = request.getParameter("isbn");
		User user = (User) session.getAttribute("user");

		try {
			PrintWriter out = response.getWriter();

			if (isbn == null) {
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("url", "carrello.jsp");
				out.write(json.toJson(hashMap));
				return;
			}

			if (user == null)
				response.sendRedirect("login.jsp");
			else {

				ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
				Prodotto prodotto = prodottoIDS.doRetrieveByIsbn(isbn);

				if(prodotto != null){
					carrello.add(prodotto);
					session.setAttribute("carrello" , carrello);
				}
			}

			out.write(json.toJson(carrello.getListaProdotti()));

		} catch (SQLException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(CarrelloServlet.class.getName());
	private static final String ERROR = "Errore";
}
