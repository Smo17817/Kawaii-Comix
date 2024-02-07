function addCart(quantita, isbn) {
	let pathArray = window.location.pathname.split('/');
	let contextPath = '/' + pathArray[1];
	let url = contextPath + "/CarrelloServlet?isbn=" + isbn;

	$.ajax({
		url: "carrello.jsp",
		type: "POST",
		data: { isbn: isbn },
		success: function(response) {
		dynamicCart(url , quantita);
		}
	});

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

			dynamicCart(contextPath + "/CarrelloServlet", "2");
			// Aggiorna i totali
			totaleParziale();
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});
}

let validNumber;
let validCvv;
let validDate;
function checkCard() {
	var numberCardInput = document.getElementById("card-number");
	var numberCard = numberCardInput.value;
	var cardNumberError = document.getElementById("card-number-error");

	var cvvNumber = document.getElementById("cvv").value;
	var cvvNumberError = document.getElementById("cvv-number-error");

	var expiryDateInput = document.getElementById("expiry-date");
	var expiryDate = expiryDateInput.value;
	var expiryDateError = document.getElementById("expiry-date-error");

	// Controllo sul numero della carta
	var cleanCardNumber = numberCard.replace(/[^0-9]/g, ""); // Rimuovi caratteri non numerici
	numberCardInput.value = cleanCardNumber;
	var formattedCardNumber = cleanCardNumber.replace(/(.{4})/g, "$1-"); // Aggiungi "-" ogni 4 cifre

	if (cleanCardNumber.length !== 16 || !/^\d+$/.test(cleanCardNumber)) {
		document.getElementById("card-number").style.border = "2px solid red";
		cardNumberError.textContent =
			"Il numero della carta deve essere lungo 16 caratteri e contenere solo numeri.";
		validNumber = false;
	} else {
		document.getElementById("card-number").style.border = "2px solid green";
		cardNumberError.textContent = "";
		validNumber = true;
		// Rimuovi l'ultimo trattino nel numero della carta
		document.getElementById("card-number").value = formattedCardNumber.slice(0, -1);
	}


	// Controllo sul CVV
	if (cvvNumber.length !== 3 || !/^\d+$/.test(cvvNumber)) {
		document.getElementById("cvv").style.border = "2px solid red";
		cvvNumberError.textContent =
			"Il numero del CVV deve essere lungo 3 caratteri e contenere solo numeri.";
		validCvv = false;
	} else {
		document.getElementById("cvv").style.border = "2px solid green";
		cvvNumberError.textContent = "";
		validCvv = true;
	}

	// Controllo sulla data
	var currentDate = new Date();
	var currentYear = currentDate.getFullYear() % 100; // Prendi solo gli ultimi due numeri dell'anno

	// Rimuovi tutti i caratteri non numerici dalla data

	var cleanExpiryDate = expiryDate.replace(/[^\d/]/g, "");

	expiryDateInput.value = cleanExpiryDate;

	if (!/^\d{2}\/\d{2}$/.test(cleanExpiryDate)) {
		// La data non rispetta il formato MM/YY
		expiryDateInput.style.border = "2px solid red";
		expiryDateError.textContent = "Inserisci una data nel formato MM/YY valido.";
		validDate = false;
	} else {
		var enteredMonth = parseInt(cleanExpiryDate.slice(0, 2));
		var enteredYear = parseInt(cleanExpiryDate.slice(3, 5));



		if (enteredMonth < 1 || enteredMonth > 12 || enteredYear < currentYear) {
			expiryDateInput.style.border = "2px solid red";
			expiryDateError.textContent = "Inserisci una data di scadenza valida.";
			validDate = false;
		}else{
			expiryDateInput.style.border = "2px solid green";
			expiryDateError.textContent = "";
			validDate = true;
		}


	}

}

function checkout(url) {
	if(validDate && validNumber && validCvv) {
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
			// Simula un breve ritardo (5 secondi) prima di reindirizzare l'utente
			Swal.fire({
				title: 'Attendere',
				html: 'Stiamo elaborando il pagamento...',
				timer: 5000,
				timerProgressBar: true,
				allowOutsideClick: false,
				showConfirmButton: false
			}).then((result) => {
				if (result.dismiss === Swal.DismissReason.timer) {
					Swal.fire({
						icon: 'success',
						title: 'Pagamento Effettuato!',
						text: 'Il pagamento è stato elaborato con successo.',
						footer : 'Puoi vedere l\'ordine dentro la tua pagina personale degli ordini',
						allowOutsideClick: false
					}).then(() => {
						window.location.href = "AddOrdineServlet?totale=" + numericValue + "&quantita=" + quantita;
					});
				}
			});
	}else if(!(validDate || validNumber || validCvv)){
		Swal.fire('Errore!','Inserire i Dati Della Carta','error');
	}
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
	if ($("#dinamico tr").length === 0) {
		Swal.fire('Errore!', 'Inserire almeno un prodotto nel carrello', 'error');
		return; // Esce dalla funzione se non ci sono righe nel carrello
	}
	svuotaCampo();
	dynamicCheckout();
	toggleSummary();
}


