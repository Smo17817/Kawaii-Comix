package view.catalogo;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
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
import com.google.gson.Gson;
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
		ProdottoIDS prodottoIDS = new ProdottoIDS(ds);

		HashMap<String, String> responseMap = new HashMap<>();
		Gson json = new Gson();
		try(PrintWriter out = response.getWriter();) {
			String isbn = request.getParameter(ISBN);
			String nome = request.getParameter(NOME);
			String autore = request.getParameter(AUTORE);
			String descrizione = request.getParameter(DESCRIZIONE);
			String prezzoString = (request.getParameter(PREZZO));
			String quantitaString = (request.getParameter(QUANTITA));
			String genere = request.getParameter(GENERE);
			String categoria = request.getParameter(CATEGORIA);
			Part imagePart = request.getPart("file");
			String fileName =  imagePart.getSubmittedFileName();
			Prodotto prodotto = null;

			if((isbn == null || isbn.trim().isEmpty()) || (nome == null || nome.trim().isEmpty()) || (autore == null || autore.trim().isEmpty()) || (descrizione == null || descrizione.trim().isEmpty()) || (prezzoString == null || prezzoString.trim().isEmpty()) || (quantitaString == null || quantitaString.trim().isEmpty()) || (genere == null || genere.trim().isEmpty()) || (categoria == null || categoria.trim().isEmpty()) || (fileName == null || fileName.trim().isEmpty())){
				setStatus(response , responseMap , json , out , "Blank");
				return;
			}
			if(!(isbn.matches("^\\d{17}$"))){
				setStatus(response , responseMap ,json , out, "Invalid_isbn" );
				return;
			}

			 prodotto = prodottoIDS.doRetrieveByIsbn(isbn);
			if(!(prodotto == null)){
				setStatus(response , responseMap , json , out , "Already_Registered");
				return;
			}

			if(!(nome.matches("^[a-zA-Z0-9\\s]+$"))){
				setStatus(response , responseMap ,json , out, "Invalid_nome" );
				return;
			}
			if(!(autore.matches("^[a-zA-Z0-9\\s]+$"))){
				setStatus(response , responseMap ,json , out, "Invalid_autore" );
				return;
			}

			if(Double.parseDouble(prezzoString) <= 0.00){
				setStatus(response , responseMap ,json , out, "Invalid_prezzo" );
				return;
			}

			if(Integer.parseInt(quantitaString) < 0){
				setStatus(response , responseMap ,json , out, "Invalid_quantita" );
				return;
			}

			if(genere.equals("-scegliere genere-")){
				setStatus(response , responseMap ,json , out, "Invalid_genere" );
				return;
			}

			if(categoria.equals("-scegliere categoria-")){
				setStatus(response , responseMap ,json , out, "Invalid_categoria" );
				return;
			}



			
			//Salva l'immagine nella directory finale
			String imagePath = "./images/" + fileName;
			prodotto = new Prodotto(isbn, nome, autore, descrizione, imagePath, Double.parseDouble(prezzoString), Integer.parseInt(quantitaString), genere, categoria);

			InputStream is = imagePart.getInputStream();
			String tempPath = getServletContext().getRealPath("/" +"images"+ File.separator + fileName);
			boolean test1 = uploadFile(is , tempPath);
			String partedaRimuovere = "target/kawaii-Comix/";
			String realPath = tempPath.replace(partedaRimuovere , "src/main/webapp/");

			boolean test = uploadFile(is,realPath);
			if(!test)
				setStatus(response , responseMap ,json , out, "File_Non_Caricato" );


			prodottoIDS.doSaveProdotto(prodotto);

			setStatusAndUrl(response , responseMap ,json , out, "success" , "aggiungiProdotto.jsp");
		} catch (SQLException | ServletException | IOException | NumberFormatException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	


	public boolean uploadFile(InputStream is, String path){
		boolean test = false;
		try(FileOutputStream fops = new FileOutputStream(path);){
			byte[] byt = new byte[is.available()];
			is.read(byt);
	
			fops.write(byt);
			fops.flush();

			test = true;

		}catch(Exception e){
			e.printStackTrace();
		}

		return test;
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
	
	/*** MACRO ***/

	private static final String ISBN = "isbn";
	private static final String NOME = "nome";
	private static final String AUTORE = "autore";
	private static final String DESCRIZIONE = "descrizione";
	private static final String PREZZO = "prezzo";
	private static final String QUANTITA = "quantita";
	private static final String GENERE = "genere";
	private static final String CATEGORIA = "categoria";

	private static final String STATUS = "status";

	private static final String URL = "url";

	private static final String contentType = "application/json";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(AddProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
}
