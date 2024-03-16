<%@ page import="catalogoManagement.Prodotto" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="it">
	<jsp:include page="./header.jsp" flush="true"/>
	<body>
		<script src="
	https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.all.min.js
	"></script>
		<link href="
	https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.min.css
	" rel="stylesheet">
		<script src="./Script/dynamicCode.js"></script>
		<script src="./Script/carrello.js"></script>

		<jsp:include page="./nav.jsp" flush="true"/>

		<% Prodotto p = (Prodotto) request.getAttribute("prodotto");%>

		<script>
			// Ottieni il genere e la categoria del prodotto
			var genere = "<%= p.getGenere() %>";
			var categoria = "<%= p.getCategoria() %>";
			var tags = genere + ", " + categoria; // Concatena genere e categoria

			// Chiamata al sistema di raccomandazione
			$.ajax({
				type: "POST",
				url: "http://127.0.0.1:5000/recommendations", // Percorso Flask per le raccomandazioni
				data: { tags: tags },
				success: function(response) {
					console.log(response);

					var contenutoHtml = "";
					for (const prodotto of response) {
						contenutoHtml += "<div class=\"scheda\">";
						contenutoHtml += "<a href=\"ProductServlet?isbn=" + prodotto.isbn + "\"><img src=\"" + prodotto.immagine_prod + "\"> </a>";
						contenutoHtml += "<div class=\"info\">";
						contenutoHtml += "<h4>" + prodotto.nome + "</h4>";
						contenutoHtml += "<p>&#8364 " + prodotto.prezzo.toFixed(2) + "</p>";
						contenutoHtml += "<a onclick=\"addCart(" + prodotto.quantita + ", '" + prodotto.isbn + "')\"> Carrello</a>";
						contenutoHtml += "</div> </div>";
					}

					$("#consigliati").append(contenutoHtml);
				}
			});
		</script>
		<script>
			var disponibilita = '';
			<%	if(p.getQuantita() == 0){%>
					disponibilita = "Esaurito";
			<%	}else if(p.getQuantita() <= 10){%>
					disponibilita = "Quasi Esaurito";
			<%	}else{%>
				disponibilita = "Disponibile";
			<%}%>

			$(document).ready(function(){
				document.getElementById("disponibilita").innerHTML = disponibilita ;
				if(disponibilita == "Esaurito")
					document.getElementById("disponibilita").style.color = 'red' ;
				else if(disponibilita == "Quasi Esaurito")
					document.getElementById("disponibilita").style.color = 'orange';
				else
					document.getElementById("disponibilita").style.color = 'green';
			});
			//TODO aggiugere modulo di IA  per sistema di raccomandazione
		</script>
		<main>
			<section id="prodotto">
				<div class = "product_wrapper">
					<div class="prod_img">
						<img src="<%=p.getImmagine()%>" alt=""/>
					</div>
					<div class="specs">
						<h3><%=p.getNome()%></h3>
						<h4>Prezzo: &#8364 <%=p.getPrezzo()%></h4>
						<p>Autore: <%=p.getAutore()%><p>
						<p>Genere: <%=p.getGenere()%><p>
						<p>Categoria: <%=p.getCategoria()%></p>
						<h3 id="disponibilita"></h3>
						<a onclick="addCart(<%=p.getQuantita()%>, '<%=p.getIsbn()%>')"> Aggiungi al Carrello</a>
					</div>
				</div>
				<div class = "descrizione">
					<div><h4><span>Descrizione</span></h4></div>
					<p><%=p.getDescrizione()%></p>
				</div>
			</section>

			<section id="bottom">
				<h4>Potrebbero interessarti anche...</h4>
				<div id="consigliati">

				</div>
			</section>

		</main>
	<jsp:include page="./footer.jsp" flush="true"/>
	</body>
</html>