<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="./header.jsp" flush="true" />
<% String status = (String) request.getAttribute("status");%>
<body>
	<jsp:include page="./nav.jsp" flush="true" />
	<main>
		<section id="signup">
			<div id="signup-img">
				<img src="./images/anya-signup.png" alt="" />
			</div>
			<form action="SignupServlet" method="POST" class="signup-form">
				<h3>Iscrizione</h3>
				<div class="form-row">
					<label for="nome">Nome:</label> <input type="text" id="nome"
						name="nome" required />
				</div>
				<div class="form-row">
					<label for="cognome">Cognome:</label> <input type="text"
						id="cognome" name="cognome" required/>
				</div>
				<div class="form-row">
					<label for="email">Email:</label> <input type="email" id="email"
						name="email" required />
				</div>
				<div class="form-row">
					<label for="password">Password:</label> <input type="password"
						id="password" name="password" required />
				</div>
				<div class="form-row">
					<label for="indirizzo">Indirizzo:</label> <input type="text"
						id="indirizzo" required
						name="indirizzo">
				</div>
				<div class="form-row">
					<label for="citta">Città:</label> <input type="text" id="citta"
						required
						name="citta">
				</div>
				<div class="form-row">
					<label for="provincia">Provincia:</label> <input type="text"
						id="provincia" required
						name="provincia">
				</div>
				<div class="form-row">
					<label for="cap">CAP:</label> <input type="text" id="cap"
						required  pattern="^[0-9]{5}$"
						name="cap">
				</div>
				<div class="form-row">
					<label for="nazione">Nazione:</label>
					<select name="nazione" id="nazione">
						<option>-effettua una scelta-</option>
						<option>Italia<option>							
					</select>
				</div>
				<div class="sub-class">
					<button type="submit">Invia</button>
       			</div>
   			
			</form>
 		</section>
 	</main>
</body>
</html>