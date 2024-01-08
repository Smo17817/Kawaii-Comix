package view.catalogo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import catalogoManagement.ProdottoIDS;

public class DeleteProdottoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		String prodottoScelto = request.getParameter("scelta");
		Gson json = new Gson();

		try {
			PrintWriter out = response.getWriter();
			
			if (prodottoScelto.equals("") || prodottoScelto.equals("-seleziona un prodotto-")) {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "Invalid_Manga");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
				return;
			}
			
			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			String isbn = prodottoIDS.doRetrieveByNome(prodottoScelto).getIsbn();
			boolean checkDelete = prodottoIDS.doDeleteProdotto(isbn);

			if (request.getParameter("risposta").equals("conferma")) {
				if (checkDelete) {
					HashMap<String, String> responseMap = new HashMap<>();
					responseMap.put(STATUS, "success");
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
			}

		} catch (SQLException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	
	/*** MACRO ***/
	private static final String STATUS = "status";
	private static final String CONTENT_TYPE = "application/json";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(DeleteProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
}
