<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 12/01/24
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
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
    function confermaDatiIndirizzo(event){
        event.preventDefault();

        var indirizzo = document.getElementById('indirizzo').value;
        var citta = document.getElementById('citta').value;
        var provincia = document.getElementById('provincia').value;
        var cap = document.getElementById('cap').value;
        var nazione = document.getElementById('nazione').value;
        console.log(nazione);

        $.ajax({
            url: '<%=request.getContextPath()%>/AddressServlet',
            type: 'POST',
            data:{
                indirizzo : indirizzo,
                citta : citta,
                provincia : provincia,
                cap : cap,
                nazione : nazione
            },
        }).done(function (response){
            var status = response.status;
            if(status == 'Blank'){
                Swal.fire("ATTENZIONE", "Inserire almeno un valore in un campo che si desidera modificare", "error");
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
            }else if(status === 'success'){
                Swal.fire("COMPLIMENTI", "Cambio dati avvenuto con successo", "success");
                setTimeout(function() {
                    window.location.assign(response.url);
                }, 3500); // Ritardo di 3,5 secondi (3500 millisecondi)
            }
        })

    }
</script>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
    <section id= "address-info">
        <div class="form-wrapper">
            <h2>I tuoi dati</h2>
            <form onsubmit="confermaDatiIndirizzo(event)" name="login" method="POST">
                <div class="form-row">
                    <label for="indirizzo">Indirizzo:</label>
                    <input type="text"   id= "indirizzo" placeholder="<%=user.getIndirizzo()%>" name="indirizzo">
                </div>
                <div  class="form-row">
                    <label for="citta">Città:</label>
                    <input type="text" id ="citta" placeholder="<%=user.getCitta()%>" name="citta">
                </div>
                <div  class="form-row">
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" maxlength="2" minlength="2" placeholder="<%=user.getProvincia()%>" name="provincia">
                </div>
                <div  class="form-row">
                    <label for="cap">CAP:</label>
                    <input type="text" id="cap" placeholder="<%=user.getCap()%>" maxlength="5" minlength="5" name="cap">
                </div>
                <div  class="form-row">
                    <label for="nazione">Nazione:</label>
                    <select name="nazione" id="nazione">
                        <option value="<%=user.getNazione()%>"><%=user.getNazione()%><option>
                    </select>
                </div>
                <div class="sub-class">
                    <button type="submit">Invia</button>
                </div>
            </form>
        </div>
    </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
