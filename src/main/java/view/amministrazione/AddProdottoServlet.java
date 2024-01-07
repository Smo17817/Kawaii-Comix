package view.amministrazione;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;

@MultipartConfig
@WebServlet("/AddProdottoServlet")
public class AddProdottoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = null;

		try {
			String isbn = request.getParameter(ISBN);
			String autore = request.getParameter(AUTORE);
			String nome = request.getParameter(NOME);
			String descrizione = request.getParameter(DESCRIZIONE);
			String immagine = request.getParameter(IMMAGINE);
			String prezzoString = (request.getParameter(PREZZO));
			String quantitaString = (request.getParameter(QUANTITA));
			String genere = request.getParameter(GENERE);
			String categoria = request.getParameter(CATEGORIA);
			
			Prodotto prodotto = new Prodotto(isbn, autore, nome, descrizione, immagine, Double.parseDouble(prezzoString), Integer.parseInt(quantitaString), genere, categoria);
			
			//Salva l'immagine nella directory finale
			Part imagePart = request.getPart("file");
			if (imagePart.getSize() != 0) {
				String file = getFileName(imagePart);
				// Percorso per salvare l'immagine
				String saveDirectory = DIRECTORY;
				String imagePath = saveDirectory + File.separator + file; 

				imagePart.write(imagePath);
			}
			
			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			prodottoIDS.doSaveProdotto(prodotto);

			dispatcher = request.getRequestDispatcher(URL);
			dispatcher.forward(request, response);
		} catch (SQLException | ServletException | IOException | NumberFormatException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	
	private String getFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		String[] tokens = contentDisposition.split(";");

		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "";
	}
	
	/*** MACRO ***/
	private static final String URL = ""; //TODO aggiungere la jsp
	private static final String DIRECTORY = "/Users/davidedelfranconatale/Desktop/Eclipse/ProgettoTsw/src/main/webapp/images";

	private static final String ISBN = "isbn";
	private static final String NOME = "nome";
	private static final String AUTORE = "autore";
	private static final String DESCRIZIONE = "descrizione";
	private static final String IMMAGINE = "immagine";
	private static final String PREZZO = "prezzo";
	private static final String QUANTITA = "quantita";
	private static final String GENERE = "genere";
	private static final String CATEGORIA = "categoria";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(AddProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
}
