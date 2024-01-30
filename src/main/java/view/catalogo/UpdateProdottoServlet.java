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
		HashMap<String, String> responseMap = new HashMap<>();

		String prodottoScelto = request.getParameter("scelta");
		String nome = request.getParameter("nome");
		String autore = request.getParameter("autore");
		String descrizione = request.getParameter("descrizione");
		String prezzoString = request.getParameter("prezzo");
		String quantitaString = request.getParameter("quantita");
		String genere = request.getParameter("genere");
		String categoria = request.getParameter("categoria");

		Part imagePart = request.getPart("file");
		String fileName =  imagePart.getSubmittedFileName();
		try {			
			PrintWriter out = response.getWriter();

			if (prodottoScelto.equals("") || prodottoScelto.equals("-seleziona un prodotto-")) {
				setStatus(response , responseMap , json , out , "Invalid_prodotto");
				return;
			}

			if((nome == null || nome.trim().isEmpty()) && (autore == null || autore.trim().isEmpty()) && (descrizione == null || descrizione.trim().isEmpty()) && (prezzoString == null || prezzoString.trim().isEmpty()) && (quantitaString == null || quantitaString.trim().isEmpty()) && (genere == null || genere.trim().isEmpty() || genere.equals("-scegliere genere-")) && (categoria == null || categoria.trim().isEmpty() || categoria.equals("-scegliere categoria-")) && (fileName == null || fileName.trim().isEmpty())){
				setStatus(response , responseMap , json , out , "Blank");
				return;
			}

			if(!(nome == null || nome.trim().isEmpty())){
				if (!(nome.matches("^[a-zA-Z0-9\\s]+$"))) {
					setStatus(response, responseMap, json, out, "Invalid_nome");
					return;
				}
			}

			if(!(autore == null || autore.trim().isEmpty())) {
				if (!(autore.matches("^[a-zA-Z0-9\\s]+$"))) {
					setStatus(response, responseMap, json, out, "Invalid_autore");
					return;
				}
			}

			if(!(prezzoString == null || prezzoString.trim().isEmpty())) {
				if (Double.parseDouble(prezzoString) <= 0.00) {
					setStatus(response, responseMap, json, out, "Invalid_prezzo");
					return;
				}
			}

			if(!(quantitaString == null || quantitaString.trim().isEmpty())) {
				if (Integer.parseInt(quantitaString) < 0) {
					setStatus(response, responseMap, json, out, "Invalid_quantita");
					return;
				}
			}

			if(!(genere == null || genere.trim().isEmpty())){
				if (genere.equals("-scegliere genere-")) {
					setStatus(response, responseMap, json, out, "Invalid_genere");
					return;
				}
			}

			if(!(categoria == null || categoria.trim().isEmpty())) {
				if (categoria.equals("-scegliere categoria-")) {
					setStatus(response, responseMap, json, out, "Invalid_categoria");
					return;
				}
			}



			String imagePath = "";
			if(!(fileName == null || fileName.trim().isEmpty())) {
				imagePath = "./images/" + fileName;

				InputStream is = imagePart.getInputStream();
				String tempPath = getServletContext().getRealPath("/" + "images" + File.separator + fileName);
				//boolean test1 = uploadFile(is , tempPath);
				String partedaRimuovere = "target/kawaii-Comix/";
				String realPath = tempPath.replace(partedaRimuovere, "src/main/webapp/");


				boolean test = uploadFile(is, realPath);
				if (!test)
					setStatus(response, responseMap, json, out, "File_Non_Caricato");

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


			if(quantitaString.isEmpty()){
				quantitaString = "0";
			}

			if(prezzoString.isEmpty()){
				prezzoString = "0";
			}
			// Setta solo i valori non vuoti, gli altri rimangono come prima
			prodotto.setNotEmpty(nome, autore, descrizione, imagePath, Double.parseDouble(prezzoString), prodotto.sommaQuantita(Integer.parseInt(quantitaString)), genere, categoria);

			boolean checkUpdate = prodottoIDS.doUpdateProdotto(prodotto);

			// Controlla che l'update del prodotto si sia verificato
			if (checkUpdate) {
				setStatusAndUrl(response ,responseMap , json ,out , "success" , "modificaProdotto.jsp");

			} else {
				setStatus(response ,responseMap , json ,out , "failed");
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
	private static final String STATUS = "status";

	private static  final  String URL = "url";
	private static final String contentType = "application/json";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(UpdateProdottoServlet.class.getName());
	private static final String ERROR = "Errore";
	

}
