package view.catalogo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;

@WebServlet("/UpdateProdottoServlet")
public class UpdateProdottoServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private final DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Gson json = new Gson();

		String prodottoScelto = request.getParameter("scelta");
		String nome = request.getParameter("nome");
		String autore = request.getParameter("autore");
		String descrizione = request.getParameter("descrizione");
		String immagine = request.getParameter("immagine");
		String prezzoString = request.getParameter("prezzo");
		String quantitaString = request.getParameter("quantita");
		String genere = request.getParameter("genere");
		String categoria = request.getParameter("categoria");

		try {			
			PrintWriter out = response.getWriter();

			if (prodottoScelto.equals("") || prodottoScelto.equals("-seleziona un prodotto-")) {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "Invalid_prodotto");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
				return;
			}
			
		    if (nome.matches(".*[^a-zA-Z0-9 ].*")) {
		        HashMap<String, String> responseMap = new HashMap<>();
		        responseMap.put(STATUS, "Invalid_nome_caratteri_speciali");
		        String jsonResponse = json.toJson(responseMap);
		        response.setContentType(CONTENT_TYPE);
		        out.write(jsonResponse);
		        out.flush();
		        return;
		    }

			Pattern regex = Pattern.compile(PATTERN);
			Matcher matcher = regex.matcher(immagine);

			if (!immagine.equals("") && !(matcher.matches())) {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "Invalid_path");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
				return;
			}

			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			Prodotto prodotto = prodottoIDS.doRetrieveByNome(prodottoScelto);
			// Setta solo i valori non vuoti, gli altri rimangono come prima
			prodotto.setNotEmpty(nome, autore, descrizione, immagine, Double.parseDouble(prezzoString), Integer.parseInt(quantitaString), genere, categoria);
			boolean checkUpdate = prodottoIDS.doUpdateProdotto(prodotto);
			
			// Controlla che l'update del prodotto si sia verificato
			if (checkUpdate) {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "success");
				responseMap.put("url", "profilo.jsp");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
			} else {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "failed");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
			}

		} catch (SQLException | IOException | NumberFormatException e) {
			logger.log(Level.ALL, ERROR, e);
		} 
	}
	
	/*** MACRO ***/
	private static final String PATTERN = "\\./images/[^/]+\\.[a-zA-Z]{3,4}";
	private static final String STATUS = "status";
	private static final String CONTENT_TYPE = "application/json";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(UpdateProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
	

}
