<%@ page import="utenteManagement.User" %>
<%@ page import="acquistoManagement.Carrello" %>
<%@ page import="catalogoManagement.Prodotto" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 11/01/24
  Time: 22:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
<% if(session.getAttribute("user")==null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<script src="
https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.all.min.js
"></script>
<link href="
https://cdn.jsdelivr.net/npm/sweetalert2@11.7.12/dist/sweetalert2.min.css
" rel="stylesheet">
<script src="./Script/carrello.js"></script>
<%
  User user = (User) session.getAttribute("user");
%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
<div id="dark-gradient"></div>
<jsp:include page="./nav.jsp" flush="true"></jsp:include>
<script src="./Script/dynamicCode.js"></script>
<script>
  document.addEventListener("DOMContentLoaded", dynamicCart("<%=request.getContextPath()%>/CarrelloServlet?isbn=<%=request.getParameter("isbn")%>", "2"));
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
      <button onclick="onClickHandler()"> Procedi al Pagamento</button>
    </div>

    <div id="summary">
      <div id="summary-userdata">
        <header>
          <h3>CheckOut</h3>
          <h4><%= ((User) request.getSession().getAttribute("user")).getNome()%> <%= ((User) request.getSession().getAttribute("user")).getCognome()%> </h4>
        </header>
        <hr>
        <div id="card-form-wrapper">
          <form>
            <input type="text"  maxlength="16" id="card-number" placeholder="Numero Carta" onkeyup="checkCard()">
            <input type="text"  maxlength="3" id="cvv" placeholder="CVV" onkeyup="checkCard()">
            <input type="text" maxlength=5 id="expiry-date"placeholder="Data di Scadenza" onkeyup="checkCard()">
          </form>
          <div id="erro-section">
            <div id="card-number-error"></div>
            <div id="cvv-number-error"></div>
            <div id="expiry-date-error"></div>
          </div>
        </div>
        <hr>
        <div id="summary-userdata-address">
          <div class="summary-userdata-address-row">
          <p>Indirizzo:</p>
          <h4><%=user.getIndirizzo()%></h4>
          </div>
          <div class="summary-userdata-address-row">
          <p>Città:</p>
          <h4><%=user.getCitta()%></h4>
          </div>
          <div class="summary-userdata-address-row">
          <p>CAP:</p>
          <h4><%=user.getCap()%></h4>
          </div>
          <div class="summary-userdata-address-row">
          <p>Nazione:</p>
          <h4><%=user.getNazione()%></h4>
          </div>
        </div>
      </div>
      <div id="summary-orderdata">
        <header>
          <h5>Riepilogo Ordine</h5>
          <i  id="closeIcon" class="ri-close-line search__close"></i>
        </header>
        <div id="summary-product">
        </div>
        <footer>
          <button onclick="checkout()">Procedi al Pagamento</button>
        </footer>
      </div>
    </div>

  </section>
</main>
<jsp:include page="./footer.jsp" flush="true"></jsp:include>
</body>
</html>
