package view.utente;

import acquistoManagement.*;
import catalogoManagement.GestoreCatalogo;
import catalogoManagement.GestoreCatalogoDAO;
import catalogoManagement.GestoreCatalogoIDS;
import com.google.gson.Gson;
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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
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

        Gson json = new Gson();
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String jspFileName =request.getParameter("jspName");
        HttpSession session  = request.getSession();
        RequestDispatcher requestDispatcher = null;

        if ((email == null || email.trim().isEmpty() || (password == null || password.trim().isEmpty()))) {
            HashMap<String, String> responseMap = new HashMap<>();
            responseMap.put(STATUS, "Blank");
            String jsonResponse = json.toJson(responseMap);
            response.setContentType(contentType);
            out.write(jsonResponse);
            out.flush();
            return;
        } else if (!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))){
            HashMap<String, String> responseMap = new HashMap<>();
            responseMap.put(STATUS, "Invalid_Mail");
            String jsonResponse = json.toJson(responseMap);
            response.setContentType(contentType);
            out.write(jsonResponse);
            out.flush();
            return;
        }


        try{
            if(jspFileName.equals("login")) {
                User user = userDAO.doRetrieveUser(email, password);
                if (user != null) {
                    Carrello carrello = carrelloDAO.doRetrieveCarrello(user.getId());
                    carrelloDAO.doRetrieveProdottiCarrello(carrello);

                    carrelloDAO.doDeleteProdottiCarrello(carrello);

                    session.setAttribute("user", user);
                    session.setAttribute("carrello", carrello);

                    HashMap<String, String> responseMap = new HashMap<>();
                    responseMap.put("status", "success");
                    responseMap.put("url", "index.jsp");
                    String jsonResponse = json.toJson(responseMap);
                    response.setContentType(contentType);
                    out.write(jsonResponse);
                    out.flush();
                } else {
                    HashMap<String, String> responseMap = new HashMap<>();
                    responseMap.put("status", "failed");
                    String jsonResponse = json.toJson(responseMap);
                    response.setContentType(contentType);
                    out.write(jsonResponse);
                    out.flush();
                }
            } else if (jspFileName.equals("loginAdmin")) {
                GestoreCatalogo gestoreCatalogo = gestoreCatalogoDAO.doRetrieveByAuthentication(email , password);
                GestoreOrdini gestoreOrdini = gestoreOrdiniDAO.doRetrieveByAuthentication(email ,password);

                if(gestoreCatalogo != null && gestoreOrdini !=null) {
                    session.setAttribute("user", gestoreCatalogo);
                    session.setAttribute("BOTH" , true);
                    HashMap<String, String> responseMap = new HashMap<>();
                    responseMap.put("status", "success");
                    responseMap.put("url", "areapersonale.jsp");
                    String jsonResponse = json.toJson(responseMap);
                    response.setContentType(contentType);
                    out.write(jsonResponse);
                    out.flush();
                }else if(gestoreCatalogo != null){
                    session.setAttribute("user", gestoreCatalogo);
                    HashMap<String, String> responseMap = new HashMap<>();
                    responseMap.put("status", "success");
                    responseMap.put("url", "areapersonale.jsp");
                    String jsonResponse = json.toJson(responseMap);
                    response.setContentType(contentType);
                    out.write(jsonResponse);
                    out.flush();
                } else if (gestoreOrdini != null) {
                    session.setAttribute("user", gestoreOrdini);
                    HashMap<String, String> responseMap = new HashMap<>();
                    responseMap.put("status", "success");
                    responseMap.put("url", "areapersonale.jsp");
                    String jsonResponse = json.toJson(responseMap);
                    response.setContentType(contentType);
                    out.write(jsonResponse);
                    out.flush();
                }else{
                    HashMap<String, String> responseMap = new HashMap<>();
                    responseMap.put("status", "failed");
                    responseMap.put("url", "loginAdmin.jsp");
                    String jsonResponse = json.toJson(responseMap);
                    response.setContentType(contentType);
                    out.write(jsonResponse);
                    out.flush();
                }
            }
        } catch (SQLException | NullPointerException e) {
        	logger.log(Level.ALL, ERROR, e);
        }

    }


    /*** MACRO ***/
    private static final String STATUS = "status";
    private static final String INDEX = "index.jsp";
    private static final String LOGIN = "login.jsp";
    private static final String LOGIN_ADMIN = "loginAdmin.jsp";
    
    /*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private static final String ERROR = "Errore";

    private static final String contentType = "application/json";

}


