<%@ page import="utenteManagement.User" %>
<%@ page import="catalogoManagement.GestoreCatalogo" %>
<%@ page import="acquistoManagement.GestoreOrdini" %><%--
  Created by IntelliJ IDEA.
  User: davidedelfranconatale
  Date: 10/01/24
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Object user = session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  Boolean flag = (Boolean) session.getAttribute("BOTH");

%>
<jsp:include page="./header.jsp" flush="true"></jsp:include>
<body>
<script>
  let flag = <%=flag%>
  <%if (user instanceof  User) {%>
    $(document).ready(function() {
      let contenutoHtml = '';
      contenutoHtml += '<li> <a href="datipersonali.jsp"><img class="proimg" alt="" src="./icons/profile.ico">Dati personali</a> </li>';
      contenutoHtml += '<li> <a href="indirizzo.jsp"><img class="proimg" alt="" src="./icons/address.ico">Indirizzo</a></li>';
      contenutoHtml += '<li> <a href="carrello.jsp"><img class="proimg" alt="" src="./icons/cart.ico">Carrello</a> </li>';
      contenutoHtml += '<li> <a href="OrdineServlet"><img class="proimg" alt="" src="./icons/calendar.ico">I miei Ordini</a></li>';
      contenutoHtml += '<li> <a href="LogOutServlet"><img class="proimg" alt="" src="./icons/exit.ico">Esci</a></li>';
      document.getElementById("user").innerHTML = contenutoHtml;
    });
  <%}else if(flag != null && flag){%>
    $(document).ready(function() {
      let contenutoHtml = '';
      contenutoHtml += '<li> <a href="aggiungiProdotto.jsp"> <img class=\"adimg\" src="./icons/upload.ico"> Aggiungi Prodotto <p> (ADMIN) </p></a> </li>';
      contenutoHtml += '<li> <a href="modificaProdotto.jsp"> <img class=\"adimg\" src="./icons/edit.ico"> Modifica Prodotto <p> (ADMIN) </p></a> </li>';
      contenutoHtml += '<li> <a href="eliminaProdotto.jsp"> <img class=\"adimg\" src="./icons/delete.ico"> Elimina Prodotto <p> (ADMIN) </p></a> </li>';
      contenutoHtml += '<li> <a href="controllaordini.jsp"> <img class=\"adimg\" src="./icons/logistic.ico"> Controlla Ordini <p> (ADMIN) </p> </a> </li>';
      contenutoHtml += '<li> <a href="LogOutServlet"><img class="proimg" alt="" src="./icons/exit.ico">Esci</a></li>';
      document.getElementById("admin").innerHTML = contenutoHtml;
    });
  <%}else if(user instanceof  GestoreOrdini){%>
  $(document).ready(function() {
    let contenutoHtml = '';
    contenutoHtml += '<li> <a href="controllaOrdini.jsp"> <img class=\"adimg\" src="./icons/logistic.ico"> Controlla Ordini <p> (ADMIN) </p> </a> </li>';
    contenutoHtml += '<li> <a href="LogOutServlet"><img class="proimg" alt="" src="./icons/exit.ico">Esci</a></li>';
    document.getElementById("admin").innerHTML = contenutoHtml;
  });
  <%}else if(user instanceof GestoreCatalogo){%>
    $(document).ready(function() {
      let contenutoHtml = '';
      contenutoHtml += '<li> <a href="aggiungiProdotto.jsp"> <img class=\"adimg\" src="./icons/upload.ico"> Aggiungi Prodotto <p> (ADMIN) </p></a> </li>';
      contenutoHtml += '<li> <a href="modificaProdotto.jsp"> <img class=\"adimg\" src="./icons/edit.ico"> Modifica Prodotto <p> (ADMIN) </p></a> </li>';
      contenutoHtml += '<li> <a href="eliminaProdotto.jsp"> <img class=\"adimg\" src="./icons/delete.ico"> Elimina Prodotto <p> (ADMIN) </p></a> </li>';
      contenutoHtml += '<li> <a href="LogOutServlet"><img class="proimg" alt="" src="./icons/exit.ico">Esci</a></li>';
      document.getElementById("admin").innerHTML = contenutoHtml;
    });
  <%}%>
</script>
  <main>
    <jsp:include page="./nav.jsp" flush="true"></jsp:include>
    <section class="mid" id="mid">
      <h2>Il mio Account</h2>
      <div class="banner">
        <video autoplay>
          <source src="./video/banner.mp4" type="video/mp4">
        </video>
      </div>
      <div class="account">
        <ul id="user">
        </ul>
        <ul id="admin">
        </ul>
      </div>
    </section>
    <jsp:include page="./footer.jsp" flush="true"></jsp:include>
  </main>
</body>
</html>
