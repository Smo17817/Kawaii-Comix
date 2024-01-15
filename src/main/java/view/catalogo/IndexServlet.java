package view.catalogo;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoDAO;
import catalogoManagement.ProdottoIDS;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/IndexServlet")
public class IndexServlet  extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response)
            throws ServletException, IOException {
        Gson json = new Gson();
        DataSource ds = (DataSource)  getServletContext().getAttribute("DataSource");
        ProdottoDAO prodottoDAO = new ProdottoIDS(ds);     

        String tipo = request.getParameter("tipo");
        try {
        	PrintWriter out = response.getWriter();
        	
            if("lastSaved".equals(tipo)){
                ArrayList<Prodotto> lastSaved =  (ArrayList<Prodotto>) prodottoDAO.lastSaved();
                out.write(json.toJson(lastSaved));
            } else if ("bestSellers".equals(tipo)) {
                ArrayList<Prodotto> bestSellers = (ArrayList<Prodotto>) prodottoDAO.bestSellers();
                out.write(json.toJson(bestSellers));
            }
            
        } catch (SQLException | IOException e) {
        	logger.log(Level.ALL, ERROR, e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.ALL, ERROR, e);
        } 
    }

    /*** LOGGER ***/
    private static final Logger logger = Logger.getLogger(IndexServlet.class.getName());
    private static final String ERROR = "Errore";
}

