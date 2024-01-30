<%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 10/01/24
  Time: 19:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
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

        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;
        var jspName = document.getElementById('jspName').value;
        $.ajax({
            url: '<%=request.getContextPath()%>/LoginServlet',
            type: 'POST',
            data:{
                email : email,
                password : password,
                jspName : jspName
            },
        }).done(function (response){
            var status = response.status;
            if(status === 'Invalid_Mail'){
                Swal.fire("E-MAIL NON VALIDA ", "L'email inserita non è in un formato corretto", "error");
            }else if(status == 'Blank'){
                Swal.fire("CAMPO VUOTO", "Inserire un valore nel campo", "error");
            }else if(status === 'failed'){
                Swal.fire("CREDENZIALI NON VALIDE", "E-mail o Password errati", "error");
            }else if(status === 'success'){
                window.location.assign(response.url);
            }
        })

    }
</script>
<main>
    <section id="login">
        <div class="login-img">
            <img src="./images/anya-admin3.jpg" alt="anya image (fantasy character from anime Spy x Family)">
        </div>
        <div class="form-wrapper">
            <h3>Accedi al tuo Account <br> Da Gestore</h3>
            <form onsubmit="confermaLogin(event)" method="post">
                <input type="hidden" name="jspName" value="loginAdmin" id="jspName">
                <input type="email" name="email" placeholder="E-mail" id="email">
                <input type="password" name="password" placeholder="Password" id="password">
                <button type="submit">Invia</button>
            </form>
        </div>
    </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>