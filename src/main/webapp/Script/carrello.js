function addCart(quantita, isbn) {
	if (quantita != 0)
		window.location.href = "carrello.jsp?isbn=" + isbn;

}

function totaleParziale() {
	let product, elem1, elem2, costo, quantita, totParz, tot = 0;

	product = document.getElementById("dinamico");
	elem1 = product.getElementsByClassName("costo");
	elem2 = document.querySelectorAll('.quantita');

	for (let i = 0; i < elem1.length; i++) {
		totParz = 0;
		costo = parseFloat(elem1[i].textContent.split(' ')[1]);
		quantita = parseInt(elem2[i].value)
		totParz += costo * quantita;
		tot += totParz;

		product.getElementsByClassName("totProd")[i].innerHTML = "&#8364 " + totParz.toFixed(2);
	}

	let cassa, spedizione = 10;

	cassa = document.getElementById("cassa");
	cassa.getElementsByClassName("tot")[0].innerHTML = "&#8364 " + tot.toFixed(2);
	if (tot == 0) // se non ci sono elementi nel carrello il totale è 0
		spedizione = 0;
	cassa.getElementsByClassName("totCumul")[0].innerHTML = "&#8364 " + (tot + spedizione).toFixed(2);
}

function eliminaRiga(button) {
	let row = button.parentNode.parentNode;
	let idProdotto = button.getAttribute("data-isbn");
	let pathArray = window.location.pathname.split('/');
	let contextPath = '/' + pathArray[1];
	let url = contextPath + "/RimuoviProdotto";

	$.ajax({
		url: url,
		type: 'POST',
		data: { isbn: idProdotto },
		success: function(response) {
			// Rimuovi la riga del prodotto dal carrello nell'interfaccia utente
			row.parentNode.removeChild(row);

			dynamicCart(contextPath + "/CarrelloServlet");
			// Aggiorna i totali
			totaleParziale();
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});
}


function checkout(url) {
	let element = document.getElementsByClassName("totCumul")[0];
	let text = element.textContent;
	let numericValue = parseFloat(text.split(' ')[1]);

	let query = document.querySelectorAll('.quantita');
	let quantita = "";
	for (let i = 0; i < query.length; i++) {
		quantita += query[i].value;
		if (query[i + 1] != null)
			quantita += ",";
	}

	if (numericValue > 0)
		window.location.href = "AddOrdineServlet?totale=" + numericValue + "&quantita=" + quantita;
}

function toggleSummary() {
	var summary = document.getElementById('summary');
	var darkGradient = document.getElementById('dark-gradient');
	var closeIcon = document.getElementById('closeIcon');

	// Verifica lo stato attuale di visibilità
	var isVisible = darkGradient.style.visibility === 'visible';

	// Inverti lo stato di visibilità
	darkGradient.style.visibility = isVisible ? 'hidden' : 'visible';
	summary.style.visibility = isVisible ? 'hidden' : 'visible';

	// Aggiungi l'event listener all'icona
	if (closeIcon) {
		closeIcon.addEventListener('click', toggleSummary);
	}
}

function svuotaCampo() {
	let mioElemento = document.getElementById("summary-product");
	mioElemento.innerHTML = "";
}

function onClickHandler() {
	svuotaCampo();
	dynamicCheckout();
	toggleSummary();
}