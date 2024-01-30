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