function dynamicIndex(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHtml = "";

		for (const prodotto of response) {
			contenutoHtml += "<div class=\"scheda\">";
			contenutoHtml += "<div class=\"nuovo\"> <h6> NUOVO </h6> </div>";
			contenutoHtml += "<a href=\"ProdottoServlet?isbn=" + prodotto.isbn + "\"><img src=\"" + prodotto.immagine + "\"> </a>";
			contenutoHtml += "<div class=\"info\">";
			contenutoHtml += "<h4>" + prodotto.nome + "</h4>";
			contenutoHtml += "<p>&#8364 " + prodotto.prezzo.toFixed(2) + "</p>";
			contenutoHtml += "</div> </div>";
		}


		$("#schedeUltimeAggiunte").append(contenutoHtml);
	});
}

function dynamicIndex2(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHtml = "";

		let contatore = 1;

		for (const prodotto of response) {
			const medalClass = getMedalClass(contatore);
			contenutoHtml += "<div class=\"scheda\">";
			contenutoHtml += "<div class=\"nuovo " + medalClass + "\"> <h6> " + contatore + " </h6> </div>";
			contenutoHtml += "<a href=\"ProdottoServlet?isbn=" + prodotto.isbn + "\"><img src=\"" + prodotto.immagine + "\"> </a>";
			contenutoHtml += "<div class=\"info\">";
			contenutoHtml += "<h4>" + prodotto.nome + "</h4>";
			contenutoHtml += "<p>&#8364 " + prodotto.prezzo.toFixed(2) + "</p>";
			contenutoHtml += "</div> </div>";

			contatore++;
		}

		$("#schedeBestSeller").append(contenutoHtml);
	});

	function getMedalClass(position) {
		// Assegna classi in base alla posizione
		if (position === 1) {
			return "gold";
		} else if (position === 2) {
			return "silver";
		} else if (position === 3) {
			return "bronze";
		} else {
			return ""; // Nessuna classe aggiuntiva per posizioni successive
		}
	}
}

function dynamicCart(url, quantita) {
	$.ajax({
		url: url,
		type: 'POST',
		data:{
			quantita :quantita
		},
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		const listaProdotti = response.listaProdotti;
		let contenutoHtml = "";

		console.log(window.location);
		console.log(response.user);
		if(response.user === null){
			Swal.fire({
				title: 'Attenzione',
				text: 'Per aggiungere un prodotto al carrello devi essere loggato!',
				icon: 'warning',
				showCancelButton: false,
				allowOutsideClick: false,
				confirmButtonText: 'OK'
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.assign(response.url);
				}
			});
			return;
		}else if(response.quantita === 'Invalid_quantita'){
			Swal.fire('SIAMO SPIACENTI =(' , 'Il prodotto è momentaneamente esaurito , faremo scorte al più presto!!!', 'error');
			return;
		}

		if (response.url === undefined) {
			for (const p of listaProdotti) {
				contenutoHtml += "<tr class=\"row\">";
				contenutoHtml += "<td> <button data-isbn='" + p.isbn + "'onclick=eliminaRiga(this)><img src=\"./icons/trash.ico\" class=trash></button>";
				contenutoHtml += "<td> <img class=thumbnail src=\"" + p.immagine + "\"></td>";
				contenutoHtml += "<td>" + p.nome + "</td>";
				contenutoHtml += "<td> <p class=costo>&#8364 " + p.prezzo.toFixed(2) + "</p> </td>";
				contenutoHtml += "<td> <h5> <input type=number min=1 max=" + p.quantita + " class=\"quantita\" onchange=totaleParziale() value=\"1\"> </h5> </td>";
				contenutoHtml += "<td> <h5 class=totProd> totale </h5> </td>";
				contenutoHtml += "</tr>";

				updateCounter(response)
			}
			$("#dinamico").append(contenutoHtml);
			totaleParziale();
		} else {
			window.location.assign(response.url);
		}
	});
}

function updateCounter(response) {
	try {
		var numeroProdotti = response.numeroProdotti;
		$('#counter p').text(numeroProdotti);
	} catch (e) {
		console.error('Errore nell\'aggiornamento del contatore:', e);
	}
}

function dynamicCheckout() {

	let rows = document.getElementsByClassName("row");
	let contenutoHtml = "";

	for (const row of rows) {
		let immagineProdotto = row.getElementsByClassName("thumbnail")[0].src;
		let nomeProdotto = row.getElementsByTagName("td")[2].textContent;
		let quantita = row.getElementsByClassName("quantita")[0].value;
		let totParziale = row.getElementsByTagName("td")[5].textContent;

		contenutoHtml += "<div id=\"product-row\">";
		contenutoHtml += "<div id=\"img-product\"> <img src=\"" + immagineProdotto + "\"></div>";
		contenutoHtml += "<div id=\"info-product\">";
		contenutoHtml += "<p> " + nomeProdotto + " x " + quantita + "</p> <br>";
		contenutoHtml += "<p>" + totParziale + "</p>";
		contenutoHtml += "</div>";
		contenutoHtml += "</div>"

	}

	let totale = document.getElementsByClassName("totCumul")[0].textContent;
	contenutoHtml += "<hr>";
	contenutoHtml += "<div id=\"final-price\">";
	contenutoHtml += "<div>"
	contenutoHtml += "<p>Spedizione: </p> <p>&#8364 10</p>"
	contenutoHtml += "</div>"
	contenutoHtml += "<div>"
	contenutoHtml += "<p>Totale:</p>";
	contenutoHtml += "<p>" + totale + "</p>";
	contenutoHtml += "</div>"
	contenutoHtml += "</div>";

	$("#summary-product").append(contenutoHtml);

}


/*** PAGINATION ***/
let itemsPerPage = 20;
let catalogo = []; // Variabile globale per memorizzare il catalogo di prodotti
let filteredProducts = []; // Prodotti filtrati
let totalPages = 0; // Numero totale di pagine dei prodotti filtrati
let currentPage = 1; // Pagina corrente

// Funzione per creare le schede dei prodotti per una pagina specifica
let isFirstLoad = true; // Imposta la variabile isFirstLoad a true all'inizio

function createProductCards(pageNumber) {
	const startIndex = (pageNumber - 1) * itemsPerPage;
	const endIndex = Math.min(startIndex + itemsPerPage, catalogo.length);

	let newHtml = "";

	for (let i = startIndex; i < endIndex; i++) {
		const p = catalogo[i];
		newHtml += createProductCard(p);
	}

	// Verifica se è il primo caricamento del catalogo
	if (isFirstLoad) {
		// Se è il primo caricamento, aggiungi semplicemente l'HTML senza effetto fade in e fade out
		$("#schedeProdotto").html(newHtml);
		isFirstLoad = false; // Imposta isFirstLoad a false dopo il primo caricamento
	} else {
		// Se non è il primo caricamento, applica l'effetto fade in e fade out
		$("#schedeProdotto").fadeOut(400, function() {
			$(this).html(newHtml);
			$(this).fadeIn(400);
		});
	}
}
function createProductCards2(pageNumber, products) {
		currentPage = pageNumber;
		const startIndex = (pageNumber - 1) * itemsPerPage;
		const endIndex = Math.min(startIndex + itemsPerPage, filteredProducts.length);

		let newHtml = "";

		for (let i = startIndex; i < endIndex; i++) {
			const p = filteredProducts[i];
			newHtml += createProductCard(p);
		}

		$("#schedeProdotto").fadeOut(400, function () {
			$(this).html(newHtml);
			$(this).fadeIn(400);
		});
}
function createProductCard(product) {
	let cardHtml = "<div class=\"scheda\" data-categoria=\"" + product.categoria + "\" data-genere=\"" + product.genere + "\">";
	cardHtml += "<a href=\"ProdottoServlet?isbn=" + product.isbn + "\"><img src=\"" + product.immagine + "\" class=\"trash\"></a>";
	cardHtml += "<div class=\"info\">";
	cardHtml += "<h4 class=\"pname\">" + product.nome + "</h4>";
	cardHtml += "<p> &#8364 " + product.prezzo.toFixed(2) + "</p>";
	cardHtml += "<a onclick=\"addCart(" + product.quantita + ", '" + product.isbn + "')\"> Carrello</a>";
	cardHtml += "</div></div>";
	return cardHtml;
}

// Funzione per creare i link per la navigazione tra le pagine
function createPaginationLinks(totalPages) {
	let paginationHtml = "";
	for (let i = 1; i <= totalPages; i++) {
		paginationHtml += `<a href="#" onclick="createProductCards(${i})">${i}</a>`;
	}
	$("#pagination-container").html(paginationHtml);
}

// Funzione per creare i link per la navigazione tra le pagine
function createPaginationLinks2(totalPages) {
	let paginationHtml = "";
	for (let i = 1; i <= totalPages; i++) {
		paginationHtml += `<a href="#" onclick="createProductCards2(${i}, filteredProducts)">${i}</a>`;
	}
	$("#pagination-container").html(paginationHtml);
}

/***FINE PAGINATION***/
function dynamicCatalog(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		catalogo = JSON.parse(response);
		let currentPage = 1;

		const totalPages = Math.ceil(catalogo.length / itemsPerPage);

		createProductCards(currentPage);
		createPaginationLinks(totalPages);
	});
}




function dynamicCategorie(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);

		let filtriCategoria = "<tr> <td> <h4> Categoria </h4> </td> </tr>";
		for (const categoria of response) {
			filtriCategoria += "<tr> <td>";
			filtriCategoria += "<input type=\"checkbox\" class=\"cat\"  value=\"" + categoria + "\"name=\"categoria\" onchange=\"searchAndFilter()\">";
			filtriCategoria += "<label class=\"secondset\">" + categoria + "</label>";
			filtriCategoria += "</td> </tr>";
		}
		$("#categorie").append(filtriCategoria);
	});
}

function dynamicGeneri(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);

		let filtriGenere = "<tr> <td> <h4> Genere </h4> </td> </tr>";
		for (const genere of response) {
			filtriGenere += "<tr> <td>";
			filtriGenere += "<input type=\"checkbox\" class=\"gen\" name=\"genere\" value=\"" + genere + "\" onchange=\"searchAndFilter()\">";
			filtriGenere += "<label class=\"firstset\">" + genere + "</label>";
			filtriGenere += "</td> </tr>";
		}
		$("#generi").append(filtriGenere);
	});
}

function dynamicConsigliati(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHtml = "";

		for (const prodotto of response) {
			contenutoHtml += "<div class=\"scheda\">";
			contenutoHtml += "<a href=\"ProductServlet?isbn=" + prodotto.isbn + "\"><img src=\"" + prodotto.img + "\"> </a>";
			contenutoHtml += "<div class=\"info\">";
			contenutoHtml += "<h4>" + prodotto.nome + "</h4>";
			contenutoHtml += "<p>&#8364 " + prodotto.prezzo.toFixed(2) + "</p>";
			contenutoHtml += "<a onclick=\"addCart(" + prodotto.quantita + ", '" + prodotto.isbn + "')\"> Carrello</a>";
			contenutoHtml += "</div> </div>";
		}

		$("#consigliati").append(contenutoHtml);
	});
}

function dynamicShowOrders(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHtml = "";

		if (response.length === 0)
			contenutoHtml += "<div id='no-item'><img src='./images/noOrdini-anya1.jpg' alt='Nessun ordine disponibile'></div><h3>Non ci sono ancora ordini...</h3>";
		else {
			for (const ordine of response) {
				contenutoHtml += "<div class=\"ordine\">";
				
				if (ordine.stato == 1) stato = "Confermato";
				else if (ordine.stato == 2) stato = "Spedito";
				else if(ordine.stato == 3) stato = "Annullato";
				else stato = "In Lavorazione"
				
				contenutoHtml += "<h3> ID: " + ordine.id + " - Data: " + formatDateIta(ordine.data) + " (" + stato + ") </h3>";
				for (const os of ordine.ordiniSingoli) {
					contenutoHtml += "<div class=\"product\">";
					contenutoHtml += "<img class=\"orderImg\" src=\"" + os.prodotto.immagine + "\">";
					contenutoHtml += "<ul class=\"info\">";
					contenutoHtml += "<li> Nome: " + os.prodotto.nome + " x" + os.quantita + "</li>";
					contenutoHtml += "<li> Totale Prodotti: &#8364 " + os.totParziale.toFixed(2) + "</li>";
					contenutoHtml += "</ul> </div>";
				}
				contenutoHtml += "<h4> Totale: &#8364 " + ordine.totale.toFixed(2) + "</li>";
				contenutoHtml += "</div>";
			}
		}

		$("#container").append(contenutoHtml);
	});
}

function dynamicCheckOrders(url) {
	$.ajax({
		url: url,
		type: 'POST',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHtml = "";
		let statoMostrato = "";
		let stato1 = "Confermato";
		let stato2 = "Spedito";
		let stato3 = "Annullato";
		let stato4 = "In Lavorazione";		
		
		for (const o of response) {
			//Scelta dinamica dello stato			
			switch(o.stato) {
				case 1:
					statoMostrato = stato1;
					break;
				case 2:
					statoMostrato = stato2;
					break;
				case 3:
					statoMostrato = stato3;
					break;
				default:
					statoMostrato = stato4;
					break;
			}
			
			contenutoHtml += "<tr data-utente='" + o.userId + "' data-giorno ='" + formatDate(o.data) + "' stato ='" + statoMostrato + "'>";
			contenutoHtml += "<td> <h4>" + formatDateIta(o.data) + "</h4> </td>";
			contenutoHtml += "<td> <h4>" + o.userId + "</h4> </td>";
			contenutoHtml += "<td> <h4>" + o.id + "</h4> </td>";
			contenutoHtml += "<td>";
			for (const os of o.ordiniSingoli)
				contenutoHtml += "<p>" + os.prodotto.nome + "</p>";
			contenutoHtml += "</td>";
			contenutoHtml += "<td> &#8364 " + o.totale.toFixed(2) + "</td>";

			contenutoHtml += "<td> <select class=\"newStato\"> <option>" + statoMostrato + "</option>";	
			//Evita ridondanza nella scelta degli stati		
			if(statoMostrato != stato1)contenutoHtml += "<option>" + stato1 + "</option>";
			if(statoMostrato != stato2)contenutoHtml += "<option>" + stato2 + "</option>";
			if(statoMostrato != stato3)contenutoHtml += "<option>" + stato3 + "</option> </select> </td>";
			contenutoHtml += "<td> <button onclick=\"cambiaStatoOrdine(this)\"> <img src=\"./icons/save.ico\" alt=\"\"> </button> </td> </tr>";
		}

		$("#container").append(contenutoHtml);
	});
}

function dynamicModificaProdotto(url) {
	$.ajax({
		url: url,
		type: 'GET',
		contentType: 'application/json; charset=utf-8'
	}).done((response) => {
		response = JSON.parse(response);
		let contenutoHtml = "<option  disabled selected value> -seleziona un prodotto- </option>";

		for (const n of response)
			contenutoHtml += '<option>' + n + '</option>'

		$("#chooseProduct").append(contenutoHtml);
	});
}

/*** FORMATTAZIONE DATA ITALIANO***/
function formatDateIta(dataString) {
	let data = new Date(dataString);

	if (isNaN(data.getTime())) {
		console.error("Stringa di data non valida");
		return null;
	}

	let opzioniFormattazione = { year: 'numeric', month: '2-digit', day: '2-digit' };
	let formatoData = new Intl.DateTimeFormat('it-IT', opzioniFormattazione);

	return formatoData.format(data);
}