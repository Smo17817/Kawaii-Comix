package view.utente;

import acquistoManagement.Carrello;
import acquistoManagement.CarrelloDAO;
import acquistoManagement.CarrelloIDS;
import utenteManagement.User;
import utenteManagement.UserDAO;
import utenteManagement.UserIDS;
import view.site.DbManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final String error = "Errore";



    private Connection connection = null;

    @Override
    protected void doPost(HttpServletRequest request , HttpServletResponse response)
            throws ServletException , IOException{
        DataSource ds = (DataSource)  getServletContext().getAttribute("DataSource");

        UserDAO userDAO = new UserIDS(ds);
        CarrelloDAO carrelloDAO = new CarrelloIDS(ds);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session  = request.getSession();
        RequestDispatcher requestDispatcher = null;


        try{
            User user =  userDAO.doRetrieveUser(email , password);
            System.out.println(user);
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
            throw new RuntimeException(e);
        }


    }

}


