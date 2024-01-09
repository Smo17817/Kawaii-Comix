package view.catalogo;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoDAO;
import catalogoManagement.ProdottoIDS;
import com.google.gson.Gson;
import view.utente.LoginServlet;

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
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/IndexServlet")
public class IndexServlet  extends HttpServlet {

    private static final String STATUS = "status";

    /*** LOGGER ***/
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final String ERROR = "Errore";

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response)
            throws ServletException, IOException {
        Gson json = new Gson();
        DataSource ds = (DataSource)  getServletContext().getAttribute("DataSource");
        ProdottoDAO prodottoDAO = new ProdottoIDS(ds);
        PrintWriter out = response.getWriter();

        try {
            ArrayList<Prodotto> lastSaved =  (ArrayList<Prodotto>) prodottoDAO.lastSaved();
            out.write(json.toJson(lastSaved));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            doGet(request, response);
        } catch (ServletException e) {
            logger.log(Level.ALL, ERROR, e);
        } catch (IOException e) {
            logger.log(Level.ALL, ERROR, e);
        }
    }
}

