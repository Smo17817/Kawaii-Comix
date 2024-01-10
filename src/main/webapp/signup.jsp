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
						name="nome" required placeholder="Nome" />
				</div>
				<div class="form-row">
					<label for="cognome">Cognome:</label> <input type="text"
						id="cognome" name="cognome" required placeholder="Cognome" />
				</div>
				<div class="form-row">
					<label for="email">Email:</label> <input type="email" id="email"
						name="email" required placeholder="E-mail" />
				</div>
				<div class="form-row">
					<label for="password">Password:</label> <input type="password"
						id="password" name="password" required placeholder="Password" />
				</div>
				<div class="form-row">
					<label for="indirizzo">Indirizzo:</label> <input type="text"
						id="indirizzo" required placeholder="Indirizzo"
						name="indirizzo">
				</div>
				<div class="form-row">
					<label for="città">Città:</label> <input type="text" id="città"
						required placeholder="Città" 
						name="città">
				</div>
				<div class="form-row">
					<label for="comune">Comune:</label> <input type="text" id="comune"
						required placeholder="Comune" 
						name="comune">
				</div>
				<div class="form-row">
					<label for="provincia">Provincia:</label> <input type="text"
						id="provincia" required placeholder="Provincia"
						name="provincia">
				</div>
				<div class="form-row">
					<label for="cap">CAP:</label> <input type="text" id="cap"
						required placeholder="CAP" pattern="^[0-9]{5}$"
						name="cap" placeholder="00000">
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