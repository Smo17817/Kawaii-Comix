package  view.utente;
import com.google.gson.Gson;
import utenteManagement.UserIDS;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
            Gson json = new Gson();
            HashMap<String, String> responseMap = new HashMap<>();
            PrintWriter out = response.getWriter();

            String email = request.getParameter("email");
            String password1 = request.getParameter("password1");
            String password2 = request.getParameter("password2");

            UserIDS userIDS = new UserIDS(ds);

        try {
            if ((email == null || email.trim().isEmpty()) || (password1 == null || password1.trim().isEmpty()) || (password2 == null || password2.trim().isEmpty())) {
                responseMap.put(STATUS, "Blank");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
                return;
            }

            if (!(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
                responseMap.put(STATUS, "Invalid_Mail");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
                return;
            }

            if (!userIDS.emailExists(email)) {
                responseMap.put(STATUS, "Mail_Non_Presente");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
                return;
            }

            if (password1.length() < 8) {
                responseMap.put(STATUS, "Invalid_Password_length");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
                return;
            }

            if (!(password1.equals(password2))) {
                responseMap.put(STATUS, "Invalid_Password");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
                return;
            }


            if (userIDS.doUpdateUserPassword(email, password1)) {
                responseMap.put(STATUS, "success");
                responseMap.put(URL, "login.jsp");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
            } else {
                responseMap.put(STATUS, "failed");
                String jsonResponse = json.toJson(responseMap);
                response.setContentType(contentType);
                out.write(jsonResponse);
                out.flush();
            }
        }catch (SQLException e){
            logger.log(Level.ALL, error ,e);
        }

        }


    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ForgotPasswordServlet.class.getName());
    private static final String error = "Errore";
    private static final String STATUS = "status";
    private static final String contentType = "application/json";
    private static final String URL = "url";

    }

