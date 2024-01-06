package view.utente;

import acquistoManagement.Carrello;
import acquistoManagement.CarrelloIDS;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/LogOutServlet")
public class LogOutServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LogOutServlet.class.getName());
    private static final String error = "Errore";

    private DataSource ds = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        CarrelloIDS carrelloIDS = new CarrelloIDS(ds);

        try {
            Carrello carrello = (Carrello) session.getAttribute("carrello");
            carrelloIDS.doSvuotaCarrello(carrello);

            if (carrello.getListaProdotti().isEmpty()) {
                session.invalidate();
                response.sendRedirect("login.jsp");
            }
        }catch (Exception exception){
            logger.log(Level.ALL, error, exception);
        }
    }
}
