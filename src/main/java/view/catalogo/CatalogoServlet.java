package view.catalogo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoComparator;
import catalogoManagement.ProdottoIDS;

@WebServlet("/CatalogoServlet")
public class CatalogoServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		Gson json = new Gson();

		try {
			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			ArrayList<Prodotto> catalogo = (ArrayList<Prodotto>) prodottoIDS.doRetreiveAllProdotti();
			PrintWriter out = response.getWriter();
			
			// Ordine alfabetico dei prodotti
			Collections.sort(catalogo, new ProdottoComparator());
			out.write(json.toJson(catalogo));
			
		} catch (IOException | SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			doPost(request, response);
		} catch (IOException | ServletException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(CatalogoServlet.class.getName());
	private static final String ERROR = "Errore";
	
}
