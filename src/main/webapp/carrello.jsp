<%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 11/01/24
  Time: 22:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% if(session.getAttribute("user")==null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<script src="./Script/carrello.js"></script>
<script src="./Script/dynamicCode.js"></script>
<script>
  document.addEventListener("DOMContentLoaded", dynamicCart("<%=request.getContextPath()%>/CarrelloServlet?isbn=<%=request.getParameter("isbn")%>"));
</script>
<main>
  <section id="container">
    <h2>Carrello</h2>
    <table id="table">
      <thead>
      <tr>
        <th>Elimina</th>
        <th>Immagine</th>
        <th>Prodotto</th>
        <th>Prezzo</th>
        <th>Quantità</th>
        <th>Totale</th>
      </tr>
      </thead>
      <tbody id="dinamico">


      </tbody>
    </table>
  </section>

  <section id="bottom">
    <div id="cassa">
      <h5>TOTALE</h5>
      <div class="totale">
        <h6>Prodotti: </h6>
        <p class="tot">&#8364 totale</p>
      </div>
      <div class="totale">
        <h6>Spedizione: </h6>
        <p>&#8364 10</p>
      </div>
      <hr>
      <div class="totale">
        <h6>Totale: </h6>

        <p class="totCumul">&#8364 totale</p>
      </div>
      <button onclick="checkout()"> Procedi al Pagamento</button>
    </div>

  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
