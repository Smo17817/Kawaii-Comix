package view.catalogo;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import com.google.gson.Gson;

import catalogoManagement.CategoriaIDS;
import catalogoManagement.GenereIDS;
import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;

@WebServlet("/UpdateProdottoServlet")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024,
		maxFileSize = 1024 * 1024 * 10,
		maxRequestSize = 1024 * 1024 * 11
)
public class UpdateProdottoServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		Gson json = new Gson();


		String prodottoScelto = request.getParameter("scelta");
		String nome = request.getParameter("nome");
		String autore = request.getParameter("autore");
		String descrizione = request.getParameter("descrizione");
		String prezzoString = request.getParameter("prezzo");
		String quantitaString = request.getParameter("quantita");
		String genere = request.getParameter("genere");
		String categoria = request.getParameter("categoria");

		try {			
			PrintWriter out = response.getWriter();

			if (prodottoScelto.equals("") || prodottoScelto.equals("-seleziona un prodotto-")) {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "Invalid_prodotto");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
				return;
			}
			
		    if (nome.matches(".*[^a-zA-Z0-9 ].*")) {
		        HashMap<String, String> responseMap = new HashMap<>();
		        responseMap.put(STATUS, "Invalid_nome_caratteri_speciali");
		        String jsonResponse = json.toJson(responseMap);
		        response.setContentType(CONTENT_TYPE);
		        out.write(jsonResponse);
		        out.flush();
		        return;
		    }

			Part imagePart = request.getPart("file");
			String fileName =  imagePart.getSubmittedFileName();
			String imagePath = "./images/" + fileName;


			InputStream is = imagePart.getInputStream();
			String tempPath = getServletContext().getRealPath("/" +"images"+ File.separator + fileName);
			//boolean test1 = uploadFile(is , tempPath);
			String partedaRimuovere = "target/kawaii-Comix/";
			String realPath = tempPath.replace(partedaRimuovere , "src/main/webapp/");


			boolean test = uploadFile(is,realPath);
			if(test){
				//stampa alert avvenuta con successo;
			}else{
				out.println("something wrong");//stampa alert verificatosi un errore
			}

			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			Prodotto prodotto = prodottoIDS.doRetrieveByNome(prodottoScelto);
			
			GenereIDS genereIDS = new GenereIDS(ds);
			CategoriaIDS categoriaIDS = new CategoriaIDS(ds);
			
			if(!genereIDS.checkGenereName(genere))
				genere = "";
			if(!categoriaIDS.checkCategoriaName(categoria))
				categoria = "";
			if(imagePath.equals("./images/null"))
				imagePath = "";
			
			// Setta solo i valori non vuoti, gli altri rimangono come prima
			prodotto.setNotEmpty(nome, autore, descrizione, imagePath, Double.parseDouble(prezzoString), Integer.parseInt(quantitaString), genere, categoria);
			
			boolean checkUpdate = prodottoIDS.doUpdateProdotto(prodotto);
			
			
			// Controlla che l'update del prodotto si sia verificato
			if (checkUpdate) {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "success");
				responseMap.put("url", "profilo.jsp");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
			} else {
				HashMap<String, String> responseMap = new HashMap<>();
				responseMap.put(STATUS, "failed");
				String jsonResponse = json.toJson(responseMap);
				response.setContentType(CONTENT_TYPE);
				out.write(jsonResponse);
				out.flush();
			}

		} catch (SQLException | IOException | NumberFormatException e) {
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
	
	/*** MACRO ***/
	private static final String STATUS = "status";
	private static final String CONTENT_TYPE = "application/json";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(UpdateProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
	

}
