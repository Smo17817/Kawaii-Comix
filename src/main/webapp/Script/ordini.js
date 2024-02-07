
function cambiaStatoOrdine(button) {
	let tr = button.parentNode.parentNode;
	let orderId = tr.getElementsByTagName("td")[2].getElementsByTagName("h4")[0].innerText;

	let select = tr.getElementsByClassName("newStato")[0];
	let stato = select.value;

	window.location.replace("ModificaStatoOrdineServlet?stato=" + stato + "&orderId=" + orderId);
}

function filterRows() {
    let input = document.getElementById("searchInput").value.toLowerCase();
    let startDateString = document.getElementById("startDateInput").value;
    let endDateString = document.getElementById("endDateInput").value;
    let statoFilter = document.getElementById("statoOrdine").value;
    let rows = document.querySelectorAll("#container tr");

    let startDate = new Date(startDateString);
    let endDate = new Date(endDateString);
    let formattedStartDate = formatDate(startDate);
    let formattedEndDate = formatDate(endDate);

    for (const row of rows) {
        let userId = row.getAttribute("data-utente");
        let giorno = formatDate(row.getAttribute("data-giorno"));
        let stato = row.getAttribute("stato");
        let showRow = true;

        if (input && userId !== input) {
            showRow = false;
        }
        if (startDateString && giorno < formattedStartDate) {
            showRow = false;
        }
        if (endDateString && giorno > formattedEndDate) {
            showRow = false;
        }
        if (statoFilter !== "-scegliere stato-" && statoFilter !== stato) {
            showRow = false;
        }
        row.style.display = showRow ? "" : "none";
    }
}


/*** FORMATTAZIONE DATA PER CONFRONTO ***/
function formatDate(dataString) {
	let date = new Date(dataString);
	
	if (!(date instanceof Date)) 
        return 'Formato data non valido';
    
	let day = String(date.getDate()).padStart(2, '0');
	let month = String(date.getMonth() + 1).padStart(2, '0');
	let year = date.getFullYear();

	return `${year}/${month}/${day}`;
}