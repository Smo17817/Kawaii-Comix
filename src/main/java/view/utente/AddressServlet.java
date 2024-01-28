package view.utente;


import com.google.gson.Gson;
import utenteManagement.User;
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

@WebServlet("/AddressServlet")
public class AddressServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher = null;

        User user = (User) session.getAttribute("user");

        HashMap<String, String> responseMap = new HashMap<>();
        Gson json = new Gson();
        PrintWriter out = response.getWriter();

        UserIDS userIDS = new UserIDS(ds);

        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        String nazione = request.getParameter("nazione");


        if((indirizzo == null || indirizzo.trim().isEmpty()) && (citta == null || citta.trim().isEmpty()) && (cap == null || cap.trim().isEmpty()) && (provincia == null || provincia.trim().isEmpty()) && (nazione == null || nazione.trim().isEmpty() || nazione.equals(user.getNazione()))){
            setStatus(response, responseMap, json, out , "Blank");
            return;
        }

        // Verifica che l'indirizzo contenga solo lettere, numeri e spazi
        if(!(indirizzo == null || indirizzo.trim().isEmpty())) {
            if (!(indirizzo.matches("[a-zA-Z0-9\\s]+"))) {
                setStatus(response, responseMap, json, out , "Invalid_Indirizzo");
                return;
            }
        }

        // Verifica che ci siano anche caratteri alfabetici nell'indirizzo quando ci sono solo numeri e caratteri speciali
        if(!(indirizzo == null || indirizzo.trim().isEmpty())) {
            if (indirizzo.matches("[0-9.\\s]+") && !indirizzo.matches(".*[a-zA-Z].*")) {
                setStatus(response, responseMap, json, out , "Indirizzo_Solo_Numeri");
                return;
            }
        }

        // Verifica che l'indirizzo contenga almeno un numero (numero civico)
        if(!(indirizzo == null || indirizzo.trim().isEmpty())) {
            if (!indirizzo.matches(".*\\d+.*")) {
                setStatus(response, responseMap, json, out , "Numero_Civico_Mancante");
                return;
            }
        }


        if(!(citta == null || citta.trim().isEmpty())) {
            if (!(citta.matches("[a-zA-Z]+"))) {
                setStatus(response, responseMap, json, out , "Invalid_Citta");
                return;
            }
        }

        if(!(cap == null || cap.trim().isEmpty())) {
            if (!(cap.matches("\\d{5}"))) {
                setStatus(response, responseMap, json, out , "Invalid_Cap");
                return;
            }
        }

        if(!(provincia == null || provincia.trim().isEmpty())) {
            if (!(provincia.matches("[a-zA-Z]{2}"))) {
                setStatus(response, responseMap, json, out , "Invalid_Provincia");
                return;
            }
        }



        try {

            if(!(indirizzo == null || indirizzo.trim().isEmpty())){
                indirizzo = formattedIndirizzo(indirizzo);
            }

            if(!(citta == null || citta.trim().isEmpty())){
                citta = formattedCitta(citta);
            }
            //Aggiorna solo i valori inseriti
            user.setNotEmpty("", "", "", "", indirizzo ,citta,cap,provincia,nazione);
            boolean checkUpdate = userIDS.doUpdateUser(user);

            if (checkUpdate) {
                setStatusAndUrl(response, responseMap, json, out , "success" , "areapersonale.jsp");
            }
            else {
                setStatus(response, responseMap, json, out , "failed");
            }




        } catch (SQLException e) {
            logger.log(Level.ALL, ERROR, e);
        }



    }

    private static void setStatus(HttpServletResponse response, HashMap<String, String> responseMap, Gson json, PrintWriter out, String stato) {
        responseMap.put(STATUS, stato);
        String jsonResponse = json.toJson(responseMap);
        response.setContentType(contentType);
        out.write(jsonResponse);
        out.flush();
    }

    private static void setStatusAndUrl(HttpServletResponse response, HashMap<String, String> responseMap, Gson json, PrintWriter out, String stato , String url) {
        responseMap.put(STATUS, stato);
        responseMap.put(URL , url);
        String jsonResponse = json.toJson(responseMap);
        response.setContentType(contentType);
        out.write(jsonResponse);
        out.flush();
    }

    private String formattedIndirizzo(String indirizzo){
        // Rimuovi eventuali spazi bianchi all'inizio e alla fine dell'indirizzo
        indirizzo = indirizzo.trim();

        // Converte la prima lettera di ogni parola in maiuscolo
        indirizzo = indirizzo.substring(0, 1).toUpperCase() + indirizzo.substring(1).toLowerCase();

        // Se l'indirizzo non inizia con "Via"
        if (!indirizzo.startsWith("Via")) {
            // Aggiungi "Via" all'inizio dell'indirizzo
            indirizzo = "Via " + indirizzo;
        }

        // Se l'indirizzo non contiene "N." prima del numero civico
        if (!indirizzo.matches(".*\\bN\\.[0-9]+")) {
            // Trova il numero civico nella stringa dell'indirizzo
            String numeroCivico = indirizzo.replaceAll("[^0-9]", "");

            // Aggiungi "N." prima del numero civico
            indirizzo = indirizzo.replace(numeroCivico, "N." + numeroCivico);
        }

        return indirizzo;
    }

    private String formattedCitta(String citta){
        citta = citta.trim();
        // Imposta la prima lettera della città in maiuscolo
        citta = citta.substring(0, 1).toUpperCase() + citta.substring(1).toLowerCase();
        return citta;
    }


    private static final long serialVersionUID = 1L;

    private static final String STATUS = "status";
    private static final String URL = "url";

    /*** LOGGER ***/
    private static final Logger logger = Logger.getLogger(DatiPersonaliServlet.class.getName());
    private static final String ERROR = "Errore";
    private static final String contentType = "application/json";
}
