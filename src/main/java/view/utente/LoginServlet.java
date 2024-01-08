package view.utente;

import acquistoManagement.Carrello;
import acquistoManagement.CarrelloDAO;
import acquistoManagement.CarrelloIDS;
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
    private final DataSource ds = (DataSource)  getServletContext().getAttribute("DataSource");

    @Override
    protected void doPost(HttpServletRequest request , HttpServletResponse response)
            throws ServletException , IOException{
      
        UserDAO userDAO = new UserIDS(ds);
        CarrelloDAO carrelloDAO = new CarrelloIDS(ds);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session  = request.getSession();
        RequestDispatcher requestDispatcher = null;


        try{
            User user =  userDAO.doRetrieveUser(email , password);
            if(user != null){
                Carrello carrello = carrelloDAO.doRetrieveCarrello(user.getId());

                carrelloDAO.doRetrieveProdottiCarrello(carrello);

                session.setAttribute("user"  , user);
                session.setAttribute("carrello" , carrello);

                requestDispatcher = request.getRequestDispatcher("index.jsp");
           }else {
               request.setAttribute("status" , "failed");
               requestDispatcher = request.getRequestDispatcher("login.jsp");
                System.out.println("è null");
           }
           requestDispatcher.forward(request ,response);
        } catch (SQLException e) {
        	logger.log(Level.ALL, ERROR, e);
        }
    }
    
    /*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final String ERROR = "Errore";

}


