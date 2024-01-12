package view.utente;

import acquistoManagement.*;
import catalogoManagement.GestoreCatalogo;
import catalogoManagement.GestoreCatalogoDAO;
import catalogoManagement.GestoreCatalogoIDS;
import utenteManagement.User;
import utenteManagement.UserDAO;
import utenteManagement.UserIDS;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request , HttpServletResponse response)
            throws ServletException , IOException{
    	DataSource ds = (DataSource)  getServletContext().getAttribute("DataSource");
        UserDAO userDAO = new UserIDS(ds);
        GestoreCatalogoDAO gestoreCatalogoDAO = new GestoreCatalogoIDS(ds);
        GestoreOrdiniDAO gestoreOrdiniDAO = new GestoreOrdiniIDS(ds);
        CarrelloDAO carrelloDAO = new CarrelloIDS(ds);


        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String jspFileName =request.getParameter("jspName");
        HttpSession session  = request.getSession();
        RequestDispatcher requestDispatcher = null;


        try{
            if(jspFileName.equals("login")) {
                User user = userDAO.doRetrieveUser(email, password);
                if (user != null) {
                    Carrello carrello = carrelloDAO.doRetrieveCarrello(user.getId());

                    carrelloDAO.doRetrieveProdottiCarrello(carrello);

                    session.setAttribute("user", user);
                    session.setAttribute("carrello", carrello);

                    requestDispatcher = request.getRequestDispatcher("index.jsp");
                } else {
                    request.setAttribute(STATUS, "failed");
                    requestDispatcher = request.getRequestDispatcher("login.jsp");
                }
            } else if (jspFileName.equals("loginAdmin")) {
                GestoreCatalogo gestoreCatalogo = gestoreCatalogoDAO.doRetrieveByAuthentication(email , password);
                GestoreOrdini gestoreOrdini = gestoreOrdiniDAO.doRetrieveByAuthentication(email ,password);

                if(gestoreCatalogo != null && gestoreOrdini !=null) {
                    session.setAttribute("user", gestoreCatalogo);
                    session.setAttribute("BOTH" , true);
                    requestDispatcher = request.getRequestDispatcher("index.jsp");
                }else if(gestoreCatalogo != null){
                    session.setAttribute("user", gestoreCatalogo);
                    requestDispatcher = request.getRequestDispatcher("index.jsp");
                } else if (gestoreOrdini != null) {
                    session.setAttribute("user", gestoreOrdini);
                    requestDispatcher = request.getRequestDispatcher("index.jsp");
                }else{
                    requestDispatcher = request.getRequestDispatcher("loginAdmin.jsp");
                }
            }
            requestDispatcher.forward(request ,response);
        } catch (SQLException e) {
        	logger.log(Level.ALL, ERROR, e);
        }
    }
    
    /*** MACRO ***/
    private static final String STATUS = "status";
    
    /*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final String ERROR = "Errore";

}


