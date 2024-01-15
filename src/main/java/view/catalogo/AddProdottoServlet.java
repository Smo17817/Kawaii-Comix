package view.catalogo;

import java.io.*;
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

import static view.catalogo.AddProdottoServlet.DIRECTORY;

@MultipartConfig(
		fileSizeThreshold = 1024 * 1024,
		maxFileSize = 1024 * 1024 * 10,
		maxRequestSize = 1024 * 1024 * 11
)
@WebServlet("/AddProdottoServlet")
public class AddProdottoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		RequestDispatcher dispatcher = null;

		try(PrintWriter out = response.getWriter();) {
			String isbn = request.getParameter(ISBN);
			String nome = request.getParameter(NOME);
			String autore = request.getParameter(AUTORE);
			String descrizione = request.getParameter(DESCRIZIONE);
			String prezzoString = (request.getParameter(PREZZO));
			String quantitaString = (request.getParameter(QUANTITA));
			String genere = request.getParameter(GENERE);
			String categoria = request.getParameter(CATEGORIA);


			//TODO aggiunta controlli sui vari o con java o solo con javascript
			
			//Salva l'immagine nella directory finale
			Part imagePart = request.getPart("file");
			String fileName =  imagePart.getSubmittedFileName();
			String imagePath = "./images/" + fileName;

			Prodotto prodotto = new Prodotto(isbn, autore, nome, descrizione, imagePath, Double.parseDouble(prezzoString), Integer.parseInt(quantitaString), genere, categoria);

			InputStream is = imagePart.getInputStream();
			String tempPath = getServletContext().getRealPath("/" +"images"+ File.separator + fileName);
			boolean test1 = uploadFile(is , tempPath);
			String partedaRimuovere = "target/kawaii-Comix/";
			String realPath = tempPath.replace(partedaRimuovere , "src/main/webapp/");


			boolean test = uploadFile(is,realPath);
			if(test){
				//stampa alert avvenuta con successo;
			}else{
				out.println("something wrong");//stampa alert verificatosi un errore
			}

			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			prodottoIDS.doSaveProdotto(prodotto);

			dispatcher = request.getRequestDispatcher("aggiungiProdotto.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException | ServletException | IOException | NumberFormatException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	


	public boolean uploadFile(InputStream is, String path){
		boolean test = false;
		try{
			byte[] byt = new byte[is.available()];
			is.read(byt);

			FileOutputStream fops = new FileOutputStream(path);
			fops.write(byt);
			fops.flush();
			fops.close();

			test = true;

		}catch(Exception e){
			e.printStackTrace();
		}

		return test;
	}
	
	/*** MACRO ***/
	public static final String DIRECTORY = "/Users/davidedelfranconatale/Downloads/kawaii-Comix/src/main/webapp/images";
	private static final String ISBN = "isbn";
	private static final String NOME = "nome";
	private static final String AUTORE = "autore";
	private static final String DESCRIZIONE = "descrizione";
	private static final String PREZZO = "prezzo";
	private static final String QUANTITA = "quantita";
	private static final String GENERE = "genere";
	private static final String CATEGORIA = "categoria";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(AddProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
}
