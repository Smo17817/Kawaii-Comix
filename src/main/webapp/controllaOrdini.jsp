<%@ page import="acquistoManagement.OrdineSingolo"%>
<%@ page import="acquistoManagement.Ordine"%>
<%@ page import="utenteManagement.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collections"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
	<button id="torna-indietro" onclick="window.location.assign('areapersonale.jsp')">
		<div class="bar"></div>
	</button>
	<script src="./Script/dynamicCode.js"></script>
	<script>
	document.addEventListener("DOMContentLoaded", dynamicCheckOrders("<%=request.getContextPath()%>/ControllaOrdiniServlet"));
	</script>

	<section>
		<h2>Controlla Ordini</h2>
		<div id="filtri">
			<h5>Filtri</h5>
			<div class="user-search">
				<label>Id: </label> <input type="text" id="searchInput"
					placeholder="inserire l'id utente..." onkeyup="filterRows()">
			</div>
			<div>
				<label>da </label> <input type="date" id="startDateInput"
					onchange="filterRows()" /> <label>a </label> <input type="date"
					id="endDateInput" onchange="filterRows()" /> <label>Stato
				</label> <select name="statoOrdine" id="statoOrdine" onchange="filterRows()">
					<option>-scegliere stato-</option>
					<option>In lavorazione</option>
					<option>Confermato</option>
					<option>Spedito</option>
					<option>Annullato</option>
				</select>
			</div>
		</div>
		<table>
			<thead>
				<tr>
					<th>Data</th>
					<th>Utente</th>
					<th>Codice Ordine</th>
					<th>Nome</th>
					<th>Totale</th>
					<th>Stato</th>
					<th>Salva</th>
				</tr>
			</thead>
			<tbody id="container">

			</tbody>
		</table>
	</section>
<script src="./Script/ordini.js"></script>
</body>
</html>