package view.catalogo;

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
import javax.sql.DataSource;

import com.google.gson.Gson;

import catalogoManagement.ProdottoIDS;

@WebServlet("/DeleteProdottoServlet")
public class DeleteProdottoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		String prodottoScelto = request.getParameter("scelta");
		Gson json = new Gson();
		HashMap<String, String> responseMap = new HashMap<>();

		try {
			PrintWriter out = response.getWriter();
			
			if (prodottoScelto.equals("") || prodottoScelto.equals("-seleziona un prodotto-")) {
				setStatus(response , responseMap , json , out , "Invalid_Manga");
				return;
			}
			
			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			String isbn = prodottoIDS.doRetrieveByNome(prodottoScelto).getIsbn();
			boolean checkDelete = prodottoIDS.doDeleteProdotto(isbn);

			if (request.getParameter("risposta").equals("conferma")) {
				if (checkDelete) {
					setStatusAndUrl(response , responseMap , json , out , "success" , "eliminaProdotto.jsp");
				} else {
					setStatus(response , responseMap , json , out ,"failed");
				}
			}

		} catch (SQLException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	private static void setStatus(HttpServletResponse response, HashMap<String, String> responseMap, Gson json, PrintWriter out, String stato) {
		responseMap.put(STATUS, stato);
		String jsonResponse = json.toJson(responseMap);
		response.setContentType(contentType);
		out.write(jsonResponse);
		out.flush();
	}

	private static void setStatusAndUrl(HttpServletResponse response, HashMap<String, String> responseMap, Gson json, PrintWriter out, String stato , String url) {
		responseMap.put(STATUS, stato);
		responseMap.put(URL , url);
		String jsonResponse = json.toJson(responseMap);
		response.setContentType(contentType);
		out.write(jsonResponse);
		out.flush();
	}
	
	/*** MACRO ***/
	private static final String STATUS = "status";
	private static final String contentType = "application/json";

	private  static  final  String URL = "url";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(DeleteProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
}
