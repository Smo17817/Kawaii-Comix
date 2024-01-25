<%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 17/01/24
  Time: 01:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body><script src="
    https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.all.min.js
    "></script>
<link href="
    https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.min.css
    " rel="stylesheet">
<script>
  function confermaLogin(event){
    event.preventDefault();

    var email = document.getElementById('email').value;
    var password1 = document.getElementById('password').value;
    var password2 = document.getElementById('conf-password').value;
    $.ajax({
      url: '<%=request.getContextPath()%>/ForgotPasswordServlet',
      type: 'POST',
      data:{
        email : email,
        password1 : password1,
        password2 : password2
      },
    }).done(function (response){
      var status = response.status;
      console.log(status);
      if(status === 'Invalid_Mail'){
        Swal.fire("E-MAIL NON VALIDA ", "L'email inserita non è in un formato corretto", "error");
      }else if(status == 'Blank'){
        Swal.fire("CAMPO VUOTO", "Inserire un valore nel campo", "error");
      }else if(status === 'Invalid_Password'){
        Swal.fire("PASSWORD DIVERSE", "Controllare che le password siano uguali", "error");
      }else if(status === 'Invalid_Password_length'){
        Swal.fire("PASSWORD TROPPO CORTA", "La password deve essere almeno di 8 caratteri" , "error");
      }else if(status === 'failed'){
        Swal.fire("SI È VERIFICATO UN ERRORE", "Tornare più tardi", "error");
      }else if(status === 'success'){
        Swal.fire("COMPLIMENTI", "Cambio Password avvenuto con successo", "success");
        setTimeout(function() {
          window.location.assign(response.url);
        }, 2000); // Ritardo di 2 secondi (2000 millisecondi)
      }
    })

  }
</script>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
  <section id ="reset-pass">
    <div id="reset-img"><img src="./images/forgot.jpg" alt=""/></div>
    <form  onsubmit="confermaLogin(event)" method = "POST" class = "reset-form">
      <h3>Reset password</h3>
      <div class="form-row">
        <label for="email">Email:</label>
        <input id="email" name = "email" placeholder="E-mail"/>
      </div>
      <div class="form-row">
        <label for="password">Password:</label>
        <div class="password-row">
        <input type="password" id="password" name="password" placeholder="Password"/>
          <img src="./icons/eye-close.png" alt="" class="eye-icon">
        </div>
      </div>
      <div class="form-row">
        <label for="conf-password">Conferma Password:</label>
        <div class="password-row">
          <input type="password" id="conf-password" name="conf-password" placeholder="Password"/>
          <img src="./icons/eye-close.png" alt="" class="eye-icon">
        </div>
      </div>
      <div class= "sub-class">
        <button type="submit">Invia</button>
      </div>
    </form>
  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
<script>
  let eyeIcons = document.querySelectorAll(".eye-icon");
  let passwords = document.querySelectorAll("[type='password']");

  eyeIcons.forEach(function(eyeIcon, index) {
    eyeIcon.onclick = function() {
      if (passwords[index].type === "password") {
        passwords[index].type = "text";
        eyeIcon.src = "./icons/eye-open.png";
      } else {
        passwords[index].type = "password";
        eyeIcon.src = "./icons/eye-close.png";
      }
    };
  });
</script>

</html>
