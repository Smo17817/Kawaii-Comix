package view.acquisto;

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

import acquistoManagement.Ordine;
import acquistoManagement.OrdineComparator;
import acquistoManagement.OrdineIDS;

@WebServlet("/ControllaOrdiniServlet")
public class ControllaOrdiniServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		Gson json = new Gson();

		try {
			PrintWriter out = response.getWriter();
			OrdineIDS ordineIDS = new OrdineIDS(ds);
			ArrayList<Ordine> ordini = (ArrayList<Ordine>) ordineIDS.doRetrieveAllOrdini();
			
			if(!ordini.isEmpty()) {
				Collections.sort(ordini, new OrdineComparator());
				Collections.reverse(ordini);
			}
			
			out.write(json.toJson(ordini));

		} catch (SQLException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		} 
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doPost(request, response);
		} catch (ServletException | IOException e) {
			logger.log(Level.ALL, ERROR, e);
		}  
	}
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(ControllaOrdiniServlet.class.getName());
	private static final String ERROR = "Errore";

}
