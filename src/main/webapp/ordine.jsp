<%@ page import="acquistoManagement.OrdineSingolo" %>
<%@ page import="acquistoManagement.Ordine" %>
<%@ page import="utenteManagement.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 12/01/24
  Time: 12:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  User user = (User) session.getAttribute("user");
  if(user == null)
      response.sendRedirect("login.jsp");

%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<%
    ArrayList<Ordine> ordini = (ArrayList<Ordine>) session.getAttribute("ordini");
    if (ordini.isEmpty()) {
%><script>
    $(document).ready(function () {
        $("#container").html("<div id='no-item'><img src='./images/noOrdini-anya1.jpg' alt='Nessun ordine disponibile'></div><h3>Non ci sono ancora ordini...</h3>");
    });
</script>
<%
} else {

        Collections.reverse(ordini);
        String stato = "Annullato";

        String contenutoHtml = "";
        for (Ordine o : ordini) {
            if (o.getUserId() == user.getId()) {
                contenutoHtml += "<div class=\"ordine\">";
                if (o.getStato() == 1) stato = "Completato";
                contenutoHtml += "<h3> ID: " + o.getId() + " - Data: " + o.getData() + " (" + stato + ") </h3>";
                for (OrdineSingolo os : o.getOrdiniSingoli()) {
                    System.out.println(os.getProdotto().getNome());
                    contenutoHtml += "<div class=\"product\">";
                    contenutoHtml += "<img class=\"orderImg\" src=\"" + os.getProdotto().getImmagine() + "\">";
                    contenutoHtml += "<ul class=\"info\">";
                    contenutoHtml += "<li> Nome: " + os.getProdotto().getNome() + " x" + os.getQuantita() + "</li>";
                    contenutoHtml += "<li> Totale Prodotti: &#8364 " + String.format("%.2f", os.getTotParziale()) + "</li>";
                    contenutoHtml += "</ul> </div>";
                }
                contenutoHtml += "<h4> Totale: &#8364 " + String.format("%.2f", o.getTotale()) + "</li>";
                contenutoHtml += "</div>";
            }
        }
%>
    <script>
        const content = '<%=contenutoHtml.replace("'", "\\'").replace("\n", "\\n")%>';
        $(document).ready(function(){
            document.getElementById("container").innerHTML = content;
        });
    </script>
<%
    }
%>
<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<main>
  <h2>I miei ordini</h2>
  <section id="container">
  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
