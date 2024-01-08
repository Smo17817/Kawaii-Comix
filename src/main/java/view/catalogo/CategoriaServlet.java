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

import catalogoManagement.CategoriaIDS;

@WebServlet("/CategoriaServlet")
public class CategoriaServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource)  getServletContext().getAttribute("DataSource");
		Gson json = new Gson();
		try {
			PrintWriter out = response.getWriter();

			CategoriaIDS categoriaIDS = new CategoriaIDS(ds);
			ArrayList<String> categorie = (ArrayList<String>) categoriaIDS.doRetrieveAll();
			// Per averle in ordine alfabetico
			Collections.sort(categorie);
			out.write(json.toJson(categorie));

		} catch (SQLException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		} 
	}
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(CategoriaServlet.class.getName());
	private static final String ERROR = "Errore";

}
