<%@ page import="utenteManagement.User" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 16/01/24
  Time: 19:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Object user = session.getAttribute("user");
    if(user == null || (user instanceof User))
        response.sendRedirect("loginAdmin.jsp");
%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<script src="
https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.all.min.js
"></script>
<link href="
https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.min.css
" rel="stylesheet">
<script>
    function confermaEliminazione(event) {
        event.preventDefault(); // Impedisce l'invio automatico del modulo

        Swal.fire({
            title: 'Sei sicuro di voler effettuare i cambiamenti?',
            showDenyButton: true,
            confirmButtonText: 'Salva',
            denyButtonText: 'Non Salvare',
        }).then((result) => {
            if (result.isConfirmed) {
                var risposta = result.isConfirmed ? "conferma" : "annulla";
                var prodotto = document.getElementById('chooseProduct').value;

                // Esegui la chiamata AJAX per inviare i dati al server
                $.ajax({
                    url: '<%=request.getContextPath()%>/DeleteProdottoServlet',
                    type: 'POST',
                    data: {
                        risposta: risposta,
                        scelta: prodotto
                    },
                }).done(function (response) {
                    var status = response.status;
                    status = status.trim();
                    status = status.replace(/[\u0000-\u001F]+/g, '');
                    if(status == 'success'){
                        Swal.fire('Prodotto eliminato correttamente','','success')
                        setTimeout(function() {
                            window.location.assign(response.url);
                        }, 2000); // Ritardo di 2 secondi (2000 millisecondi)
                    }
                    else if(status =='Invalid_Manga'){
                        Swal.fire('Scegliere un Manga da eliminare!','Riprovare','error')
                    }
                    else{
                        Swal.fire('Il prodotto è già stato eliminato!','Riaggiornare','error')
                    }
                })
            } else if (result.isDenied) {
                Swal.fire('Modifiche non salvate', '', 'info');
            }
        });
    }

</script>
<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<script src="./Script/dynamicCode.js"></script>
<script>

    document.addEventListener("DOMContentLoaded", dynamicModificaProdotto("<%=request.getContextPath()%>/NameServlet"));

</script>

<section id="new_product">
    <div class="form-wrapper">
        <form  method="POST" onsubmit="confermaEliminazione(event)">
            <h2>Elimina un Prodotto</h2>
            <div class="form-row">
                <label for="scelta">Prodotto: </label>
                <select id="chooseProduct" name="scelta" required id="scelta">

                </select>
            </div>
            <div class="sub-class">
                <button type="submit">Elimina</button>
            </div>
        </form>
    </div>
</section>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
