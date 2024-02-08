<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 12/01/24
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  User user = (User) session.getAttribute("user");
  if(user == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<link href="
    https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.min.css
    " rel="stylesheet">
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
<script src="
https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.all.min.js
"></script>

<script>
  function confermaDatiPersonali(event){
    event.preventDefault();

    var nome = document.getElementById('nome').value;
    var cognome = document.getElementById('cognome').value;
    var email = document.getElementById('email').value;
    var password1 = document.getElementById('password').value;
    var password2 = document.getElementById('conferma-pass').value;

    $.ajax({
      url: '<%=request.getContextPath()%>/DatiPersonaliServlet',
      type: 'POST',
      data:{
        nome : nome,
        cognome : cognome,
        email : email,
        password1 : password1,
        password2 : password2
      },
    }).done(function (response){
      var status = response.status;
      if(status === 'Invalid_Mail'){
        Swal.fire("E-MAIL NON VALIDA ", "L'email inserita non è in un formato corretto", "error");
      }else if(status == 'Blank'){
        Swal.fire("ATTENZIONE", "Inserire almeno un valore in un campo che si desidera modificare", "error");
      }else if(status === 'Invalid_Password_length'){
        Swal.fire("PASSWORD TROPPO CORTA", "La password deve essere almeno di 8 caratteri" , "error");
      }else if(status === 'Invalid_Password'){
          Swal.fire("PASSWORD DIVERSE", "Controllare che le password siano uguali", "error");
      }else if(status === 'Mail_Presente'){
        Swal.fire("ATTENZIONE", "Non puoi registrarti con una E-mail già presente", "error");
      }else if(status === 'failed'){
        Swal.fire("ERRORE", "Non è stato possibile aggiornare i dati, riprovare più tardi", "error");
      }else if(status === 'success'){
        Swal.fire("COMPLIMENTI", "Cambio dati avvenuta con successo", "success");
        setTimeout(function() {
          window.location.assign(response.url);
        }, 3500); // Ritardo di 3,5 secondi (3500 millisecondi)
      }
    })

  }
</script>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
  <section id="personal-info">
    <div class="form-wrapper">
      <form onsubmit="confermaDatiPersonali(event)" method="POST">
        <h2>Informazioni personali</h2>
        <div class="form-row">
          <label for="nome">Nome:</label>
          <input type="text" id="nome" name="nome"  placeholder="<%=user.getNome()%>"/>
        </div>
        <div class="form-row">
          <label for="cognome">Cognome:</label>
          <input type="text" id="cognome" name="cognome"  placeholder="<%=user.getCognome()%>"/>
        </div>
        <div class="form-row">
          <label for="email">Email:</label>
          <input id="email"name="email"  placeholder="<%=user.getEmail()%>"/>
        </div>
        <div class="form-row">
          <label for="password">Nuova Password:</label>
          <input type="password" id="password" name="password1" />
        </div>
        <div class="form-row">
          <label for="conferma-pass">Conferma Password:</label>
          <input type="password" id="conferma-pass" name="password2" />
        </div>
        <div class= "sub-class">
          <button type="submit">Invia</button>
        </div>
      </form>
    </div>

  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
