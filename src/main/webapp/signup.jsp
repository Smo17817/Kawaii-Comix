<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="./header.jsp" flush="true" />
<body>
<script src="
https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.all.min.js
"></script>
<link href="
    https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.min.css
    " rel="stylesheet">
<script>
	function confermaLogin(event){
		event.preventDefault();

		var nome = document.getElementById('nome').value;
		var cognome = document.getElementById('cognome').value;
		var email = document.getElementById('email').value;
		var password = document.getElementById('password').value;
		var indirizzo = document.getElementById('indirizzo').value;
		var citta = document.getElementById('citta').value;
		var provincia = document.getElementById('provincia').value;
		var cap = document.getElementById('cap').value;
		var nazione = document.getElementById('nazione').value;

		$.ajax({
			url: '<%=request.getContextPath()%>/SignupServlet',
			type: 'POST',
			data:{
				nome : nome,
				cognome : cognome,
				email : email,
				password : password,
				indirizzo : indirizzo,
				citta : citta,
				provincia : provincia,
				cap : cap,
				nazione : nazione
			},
		}).done(function (response){
			var status = response.status;
			console.log(status);
			if(status === 'Invalid_Mail'){
				Swal.fire("E-MAIL NON VALIDA ", "L'email inserita non è in un formato corretto", "error");
			}else if(status == 'Blank'){
				Swal.fire("CAMPO VUOTO", "Inserire un valore nel campo", "error");
			}else if(status === 'Invalid_Password_length'){
				Swal.fire("PASSWORD TROPPO CORTA", "La password deve essere almeno di 8 caratteri" , "error");
			}else if(status === 'Invalid_Indirizzo'){
				Swal.fire("ERRORE DI FORMATO", "Inserire solo il nome della via ed il numero civico", "error");
			}else if(status === 'Indirizzo_Solo_Numeri') {
				Swal.fire("ERRORE DI FORMATO", "Inserire il nome della Via", "error");
			}else if(status === 'Numero_Civico_Mancante'){
				Swal.fire("ERRORE", "Inserire il numero civico nell'indirizzo", "error");}
			else if(status === 'Invalid_Citta') {
				Swal.fire("ERRORE DI FORMATO", "Non sono ammessi caratteri speciali o numeri nella città", "error");
			}else if(status === 'Invalid_Cap'){
				Swal.fire("ERRORE DI FORMATO", "Non sono ammessi caratteri speciali o lettere nel CAP", "error");
			}else if(status === 'Invalid_Provincia'){
				Swal.fire("ERRORE DI FORMATO", "Non sono ammessi caratteri speciali o lettere nella Provincia", "error");
			}else if(status === 'Invalid_Nazione') {
				Swal.fire("ERRORE", "Scegliere una nazione", "error");
			} else if(status === 'Duplicate'){
					Swal.fire("ERRORE", "E-mail già presente nel sito, cambiare e-mail", "error");
			}else if(status === 'success'){
				Swal.fire("COMPLIMENTI", "Registrazione avvenuta con successo", "success");
				setTimeout(function() {
					window.location.assign(response.url);
				}, 2000); // Ritardo di 2 secondi (2000 millisecondi)
			}
		})

	}
</script>
	<jsp:include page="./nav.jsp" flush="true" />
	<main>
		<section id="signup">
			<div id="signup-img">
				<img src="./images/anya-signup.png" alt="" />
			</div>
			<form onsubmit="confermaLogin(event)" method="POST" class="signup-form">
				<h3>Iscrizione</h3>
				<div class="form-row">
					<label for="nome">Nome:</label>
					<input type="text" id="nome" name="nome"/>
				</div>
				<div class="form-row">
					<label for="cognome">Cognome:</label>
					<input type="text" id="cognome" name="cognome"/>
				</div>
				<div class="form-row">
					<label for="email">Email:</label>
					<input  id="email" name="email"/>
				</div>
				<div class="form-row">
					<label for="password">Password:</label>
					<div class="password-row">
						<input type="password" id="password" name="password"/>
						<img src="./icons/eye-close.png" alt="" id="eye-icon">
					</div>
				</div>
				<div class="form-row">
					<label for="indirizzo">Indirizzo:</label>
					<input type="text" id="indirizzo"  name="indirizzo">
				</div>
				<div class="form-row">
					<label for="citta">Città:</label>
					<input type="text" id="citta"  name="citta">
				</div>		
				<div class="form-row">
					<label for="provincia">Provincia:</label>
					<input type="text" id="provincia"  name="provincia" maxlength="2" minlength="2">
				</div>
				<div class="form-row">
					<label for="cap">CAP:</label>
					<input type="text" id="cap"  maxlength="5" minlength="5" name="cap">
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
<script>
	let eyeicon = document.getElementById("eye-icon");
	let password = document.getElementById("password");
	eyeicon.onclick = function(){
		if(password. type == "password" ) {
			password.type = "text";
			eyeicon.src = "./icons/eye-open.png";
		}
		else{
			password. type = "password";
			eyeicon.src = "./icons/eye-close.png";
		}
	}
</script>
</html>